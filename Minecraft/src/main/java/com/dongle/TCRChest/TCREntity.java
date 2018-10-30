package com.dongle.TCRChest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

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
						int _count = Integer.parseInt(split[1]);
						for(int i = 0; i < tempInv.getSlots(); i++){

							if(tempInv.getStackInSlot(i) != ItemStack.EMPTY){
								String itemInSlotName = tempInv.getStackInSlot(i).getItem().getRegistryName().toString();
								if((tempInv.getStackInSlot(i).getCount() < 64) && itemInSlotName.equals(split[0])){
									Item tempItem = Item.getByNameOrId(split[0]);
									ItemStack remainStack = new ItemStack(tempItem, _count);
									remainStack = tempInv.insertItem(i, remainStack, false);
									while(remainStack.getCount() > 0){
										for(int j = i+1; j < tempInv.getSlots(); j++){
											itemInSlotName = tempInv.getStackInSlot(j).getItem().getRegistryName().toString();
											if(tempInv.getStackInSlot(j) != ItemStack.EMPTY && tempInv.getStackInSlot(j).getCount() < 64 && itemInSlotName.equals(split[0])){
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
								Item tempItem = Item.getByNameOrId(split[0]);
								ItemStack tempStack = new ItemStack(tempItem, Integer.parseInt(split[1]));
								tempInv.insertItem(i, tempStack, false);
								break;
							}
						}
					}
					br.close();
					fileRead.close();
					PrintWriter pw = new PrintWriter("fromFactorio.dat");
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
