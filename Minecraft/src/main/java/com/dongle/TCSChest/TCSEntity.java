package com.dongle.TCSChest;

import com.dongle.FMCBridge.FMCBridge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TCSEntity extends TileEntity implements ITickable {

	public static final int SIZE = 1;
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
			if(!added){
				System.out.println("Adding from update");
				if(!FMCBridge.instance.tcsEntityList.containsKey(entId)){
					FMCBridge.instance.addTCS(entId, this);
					added = true;
				}
			}
		}
	}
}
