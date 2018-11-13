package com.dongle.TCRManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.TCRChest.TCREntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TCRManagerEntity  extends TileEntity implements ITickable{
	
	int ticks = 0;
	@Override
	public void update() {
		if(!world.isRemote){
			if(ticks%20 == 0){
				Map<Integer, TCREntity> entList = FMCBridge.instance.tcrEntityList;
				try {
					FileReader fileRead = new FileReader("fromFactorio.dat");
					BufferedReader br = new BufferedReader(fileRead);
					String str = "";
					ItemStackHandler tempInv = null;
					while((str=br.readLine())!= null){
						//Split initial string into item name + data and count. 0 is item name + data, 1 is count 
						//Then split item name into its data. 0 is item name, 1 is metadata, 2 is NBT data if any 
						String[] split = str.split("~");
						String[] itemSplit = split[0].split("\\|");
						String[] nbtSplit = null;
						int _count = Integer.parseInt(split[1]);

						if(itemSplit.length == 3){
							String strippedNBT = itemSplit[2].replaceAll("[\"{}]", "");
							System.out.println(strippedNBT);
							nbtSplit = strippedNBT.split(":", 2);
							System.out.println(nbtSplit[0] + " " + nbtSplit[1]);

						}
						
						//Check Chest lock, if its not null, get the first available empty chest
						//Else return and print out all chests are locked.
						tempInv = checkLock(entList, itemSplit[0]);
						if(tempInv == null){
							for(TCREntity ent : entList.values()){
								if(ent.getItemLock() == "" && !ent.checkFull()){
									tempInv = (ItemStackHandler) ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
								}
							}
						}
						if(tempInv == null){
							System.out.println("cannot insert, all chests locked or full");
							break;
						}
						
						
						//Item insertion handling.
						for(int i = 0; i < tempInv.getSlots(); i++){
							if(tempInv.getStackInSlot(i) != ItemStack.EMPTY){
								String itemInSlotName = tempInv.getStackInSlot(i).getItem().getRegistryName().toString();
								if((tempInv.getStackInSlot(i).getCount() < 64) && check_slot_lock(tempInv, itemInSlotName, itemSplit, i) || checkNBT(tempInv, itemSplit, itemInSlotName, nbtSplit, i)){
									Item tempItem = Item.getByNameOrId(itemSplit[0]);
									ItemStack remainStack = new ItemStack(tempItem, _count);
									remainStack.setItemDamage(Integer.parseInt(itemSplit[1]));
									if(nbtSplit != null){
										remainStack.setTagCompound(new NBTTagCompound());
										remainStack.getTagCompound().setString(nbtSplit[0], nbtSplit[1]);
									}
									remainStack = tempInv.insertItem(i, remainStack, false);
									while(remainStack.getCount() > 0){
										for(int j = i+1; j < tempInv.getSlots(); j++){
											itemInSlotName = tempInv.getStackInSlot(j).getItem().getRegistryName().toString();
											if(tempInv.getStackInSlot(j) != ItemStack.EMPTY && tempInv.getStackInSlot(j).getCount() < 64 && check_slot_lock(tempInv, itemInSlotName, itemSplit, i) || checkNBT(tempInv, itemSplit, itemInSlotName, nbtSplit, i)){
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
								Item tempItem = Item.getByNameOrId(itemSplit[0]);
								ItemStack tempStack = new ItemStack(tempItem, _count);
								tempStack.setItemDamage(Integer.parseInt(itemSplit[1]));

								if(nbtSplit != null){
									tempStack.setTagCompound(new NBTTagCompound());
									tempStack.getTagCompound().setString(nbtSplit[0], nbtSplit[1]);
								}
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
	
	public static boolean check_slot_lock(ItemStackHandler tempInv, String itemInSlot, String[] item_data, int slot){
		return itemInSlot.equals(item_data[0]) && tempInv.getStackInSlot(slot).getItem().getDamage(tempInv.getStackInSlot(slot)) == Integer.parseInt(item_data[1]);
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
