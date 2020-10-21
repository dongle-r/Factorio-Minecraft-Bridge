package com.dongle.TCSChest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.utils.MinecraftItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TCSEntity extends TileEntity implements ITickable {

	public static final int SIZE = 1;
	private int entId;
	boolean added = false;
	boolean manager = false;
	int ticks = 0;
	
	public void setEntId(int x){
		entId = x;
	}
	
	public int getEntId(){
		return entId;
	}
	
	public void setAdded(boolean flag){
		added = flag;
	}
	
	public void setManager(boolean _manager) {
		manager = _manager;
	}
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE){
		@Override
		protected void onContentsChanged(int slot){
			TCSEntity.this.markDirty();
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
    }
	
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setInteger("entId", entId);
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
					send();
				}
				ticks++;
			}
		}
	}
	
	public void add() {
		if(FMCBridge.instance.tcsEntityList.size() <= 0 && FMCBridge.instance.sendManager == null) {
			this.manager = true;
			FMCBridge.instance.sendManager = this;
			//ForgeChunkManager.forceChunk(FMCBridge.instance.sendTicket, new ChunkPos(pos));
		}
		System.out.println("adding from add()");
		int _entId = FMCBridge.instance.addTCS(this);
		this.setEntId(_entId);
		this.setAdded(true);
		System.out.println(_entId);
		added = true;
	}
	
	public void remove() {
		System.out.println("removing from remove()");
    	FMCBridge.instance.removeTCS(this.getEntId());
		if(this.manager) {
			this.manager = false;
			FMCBridge.instance.sendManager = null;
			if(FMCBridge.instance.tcsEntityList.size() > 0) {
				Map.Entry<Integer, TCSEntity> entry = FMCBridge.instance.tcsEntityList.entrySet().iterator().next();
				entry.getValue().setManager(true);
				System.out.println("Assigning new manager to ent id: " + entry.getKey() );
			}
		}
	}
	
	public void send() {
		if(FMCBridge.instance.queues.threadOutput == null || !FMCBridge.instance.queues.networkQueue.isConnected()) {
			return;
		}
		ticks = 0;
		Map<Integer, TCSEntity> entList = FMCBridge.instance.tcsEntityList;
		Map<ItemStack, MinecraftItem> items = new HashMap<ItemStack, MinecraftItem>();
		Map<ItemStack, Integer> itemList = new HashMap<ItemStack, Integer>();
		Map<ItemStack, Integer> metaList = new HashMap<ItemStack, Integer>();
		Map<ItemStack, String> nbtList = new HashMap<ItemStack, String>();
		ArrayList<MinecraftItem> outputItems = new ArrayList<MinecraftItem>();
		try {
			for(TCSEntity ent : entList.values()){
				ItemStack tempInv = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0).copy();
				if(itemListContains(itemList, tempInv)){
					///COME BACK AND LOOK AT THIS. YOU ARE DEFINITELY WRONG AND NEED TO REDO THIS
					items.get(tempInv).count += tempInv.getCount();
				}
				else{
					if(tempInv.getCount() > 0){
						MinecraftItem itemStack = new MinecraftItem();
						itemStack.itemName = tempInv.getItem().getRegistryName().toString();
						itemStack.count = tempInv.getCount();
						itemStack.metadata = tempInv.getItem().getMetadata(tempInv);
						itemStack.nbt = new ArrayList<String>();
						items.put(tempInv, itemStack);
						outputItems.add(itemStack);
					}
				}
				ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0).setCount(0);
			}
			//Notify thread it is good to send items out
			if(outputItems.size() > 0) {
				FMCBridge.instance.queues.threadOutput.set_items(outputItems);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean itemListContains(Map<ItemStack, Integer> itemList, ItemStack compStack){
		for(ItemStack stack : itemList.keySet()){
			ItemStack listStack = stack.copy();
			ItemStack _compStack = compStack.copy();
			listStack.setCount(1);
			_compStack.setCount(1);
			if(stack.areItemStacksEqual(listStack, _compStack)){
				return true;
			}
		}
		return false;
	}
}
