package com.dongle.TCRChest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.TCSChest.TCSEntity;
import com.dongle.utils.MinecraftItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TCREntity extends TileEntity implements ITickable{
	
	public static final int SIZE = 27;
	private int entId;
	boolean added = false;
	boolean manager = false;
	int ticks = 0;
	private String itemLocked = "";
	public boolean receiving = false;
	
	public void setEntId(int x){
		entId = x;
	}
	
	public int getEntId(){
		return entId;
	}
	
	public void setAdded(boolean flag){
		added = flag;
	}
	
	public void setItemLock(String itemStack){
		itemLocked = itemStack;
	}
	
	public String getItemLock(){
		return itemLocked;
	}
	
	public void setManager(boolean _manager) {
		manager = _manager;
	}
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE){
		@Override
		protected void onContentsChanged(int slot){
			TCREntity.this.markDirty();
		}
	};
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if(compound.hasKey("entId")){
        	entId = compound.getInteger("entId");
        }
        if(compound.hasKey("lockedItem")){
        	itemLocked = compound.getString("lockedItem");
        	System.out.println("Read the locked item[" + itemLocked + "]");
        }
    }
	
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setInteger("entId", entId);
        
    	System.out.println("Writing the locked item : " + itemLocked);
        compound.setString("lockedItem", itemLocked);
        
        return compound;
    }
	
    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
    
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }
    
	@Override
	public void update() {
		if(!world.isRemote){
			if(!added) {
				add();
			}
			if(manager) {
				if(ticks%20 == 0){
					if(!receiving) {
						receiving = true;
						if(FMCBridge.instance.queues.threadInput == null || !FMCBridge.instance.queues.networkQueue.isConnected()) {
							return;
						}
						FMCBridge.instance.queues.threadInput.try_receive(receiving);
					}
				}
				ticks++;
			}
		}
	}
	
	public void receive(ArrayList<MinecraftItem> items) {
		Map<Integer, TCREntity> entList = FMCBridge.instance.tcrEntityList;
		for(int i = 0; i < items.size(); i++) {
			MinecraftItem item = items.get(i);
			ItemStackHandler tempInv = null;
			//0 is item name, 1 is metadata, 2 is NBT data if any
			//Check Chest lock, if its not null, get the first available empty chest
			//Else return and print out all chests are locked.
			tempInv = checkLock(entList, item.itemName);
			if(tempInv == null){
				for(TCREntity ent : entList.values()){
					if(ent.getItemLock() == "" && !ent.checkFull()){
						tempInv = (ItemStackHandler) ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					}
				}
			}
			if(tempInv == null){
				continue;
			}
			
			
			//Item insertion handling.
			for(int k = 0; k < tempInv.getSlots(); k++){
				if(tempInv.getStackInSlot(k) != ItemStack.EMPTY){
					String itemInSlotName = tempInv.getStackInSlot(k).getItem().getRegistryName().toString();
					if((tempInv.getStackInSlot(k).getCount() < 64) && check_slot_lock(tempInv, itemInSlotName, item, k) /*|| checkNBT(tempInv, item, itemInSlotName, nbtSplit, k)*/){
						Item tempItem = Item.getByNameOrId(item.itemName);
						ItemStack remainStack = new ItemStack(tempItem, item.count);
						remainStack.setItemDamage(item.metadata);
						remainStack = tempInv.insertItem(k, remainStack, false);
						while(remainStack.getCount() > 0){
							for(int j = k+1; j < tempInv.getSlots(); j++){
								itemInSlotName = tempInv.getStackInSlot(j).getItem().getRegistryName().toString();
								if(tempInv.getStackInSlot(j) != ItemStack.EMPTY && tempInv.getStackInSlot(j).getCount() < 64 && check_slot_lock(tempInv, itemInSlotName, item, k) /*|| checkNBT(tempInv, item, itemInSlotName, nbtSplit,k)*/){
									remainStack = tempInv.insertItem(j, remainStack, false);
									break;
								}
								else{
									tempInv.insertItem(j, remainStack, false);
									break;
								}
							}
							break;
						}
						break;
					}
				}
				else{
					Item tempItem = Item.getByNameOrId(item.itemName);
					ItemStack tempStack = new ItemStack(tempItem, item.count);
					tempStack.setItemDamage(item.metadata);

					tempInv.insertItem(k, tempStack, false);
					break;
				}
			}
		}
		this.receiving = false;
	}
	
	public void add() {
		if(FMCBridge.instance.tcrEntityList.size() <= 0 && FMCBridge.instance.receieveManager == null) {
			this.manager = true;
			FMCBridge.instance.receieveManager = this;
			//ForgeChunkManager.forceChunk(FMCBridge.instance.sendTicket, new ChunkPos(pos));
		}
		System.out.println("adding from add()");
		int _entId = FMCBridge.instance.addTCR(this);
		this.setEntId(_entId);
		this.setAdded(true);
		System.out.println(_entId);
		added = true;
	}
	
	public void remove() {
		System.out.println("removing from remove()");
    	FMCBridge.instance.removeTCR(this.getEntId());
		if(this.manager) {
			this.manager = false;
			FMCBridge.instance.receieveManager = null;
			if(FMCBridge.instance.tcrEntityList.size() > 0) {
				Map.Entry<Integer, TCREntity> entry = FMCBridge.instance.tcrEntityList.entrySet().iterator().next();
				entry.getValue().setManager(true);
				System.out.println("Assigning new manager to ent id: " + entry.getKey() );
			}
		}
	}
	
	public boolean checkFull(){
		ItemStackHandler tempInv = (ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for(int i = 0; i < SIZE; i++){
			if(tempInv.getStackInSlot(i).getCount() < 64){
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean check_slot_lock(ItemStackHandler tempInv, String itemInSlot, MinecraftItem item, int slot){
		return itemInSlot.equals(item.itemName) && tempInv.getStackInSlot(slot).getItem().getDamage(tempInv.getStackInSlot(slot)) == item.metadata;
	}
	
	public static boolean checkNBT(ItemStackHandler stack, String[] item_data, String itemInSlot, String[] nbtData, int slot){
		if(nbtData != null){
			return itemInSlot.equals(item_data[0]) && stack.getStackInSlot(slot).getTagCompound().hasKey(nbtData[0]);
		}
		return false;
	}
	
	public static ItemStackHandler checkLock(Map<Integer, TCREntity> entList, String lock){
		for(TCREntity ent : entList.values()){
			if(ent.getItemLock().equals(lock) && !ent.checkFull()){
				return (ItemStackHandler) ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			}
		}
		return null;
	}
}
