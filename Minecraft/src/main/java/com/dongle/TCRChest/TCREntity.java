package com.dongle.TCRChest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
	
	public void setEntId(int x){
		entId = x;
	}
	
	public int getEntId(){
		return entId;
	}
	
	public void setAdded(boolean flag){
		added = flag;
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
    
	int ticks = 0;

	@Override
	public void update() {
		if(!world.isRemote){
			if(ticks%20 == 0){
				try {
					FileReader fileRead = new FileReader("fromFactorio.dat");
					BufferedReader br = new BufferedReader(fileRead);
					String str = "";
					ItemStackHandler tempInv = (ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					while((str=br.readLine())!= null){
						String[] split = str.split("~");
						for(int i = 0; i < tempInv.getSlots(); i++){
							if(tempInv.getStackInSlot(i) != null){
								ItemStack remainStack;
								if(tempInv.getStackInSlot(i).getItem().getRegistryName().equals(split[0])){
									ItemStack tempStack = tempInv.getStackInSlot(i);
									if(tempStack.getCount() >= 64){
										continue;
									}
									else{
										tempStack.setCount(Integer.parseInt(split[1]));
										remainStack = tempInv.insertItem(i, tempStack, false);
										//Do something with overflow
									}
								}
							}
							else{
								Item tempItem = Item.getByNameOrId(split[0]);
								ItemStack tempStack = new ItemStack(tempItem, Integer.parseInt(split[1]));
								tempInv.insertItem(i, tempStack, false);
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
