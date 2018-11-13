package com.dongle.TCSManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.TCSChest.TCSEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;

public class TCSManagerEntity  extends TileEntity implements ITickable{
	
	int ticks = 0;
	@Override
	public void update() {
		if(!world.isRemote){
			if(ticks%20 == 0){
				ticks = 0;
				Map<Integer, TCSEntity> entList = FMCBridge.instance.tcsEntityList;
				Map<ItemStack, Integer> itemList = new HashMap<ItemStack, Integer>();
				Map<ItemStack, Integer> metaList = new HashMap<ItemStack, Integer>();
				Map<ItemStack, String> nbtList = new HashMap<ItemStack, String>();
				try {
					for(TCSEntity ent : entList.values()){
						ItemStack tempInv = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
						if(itemListContains(itemList, tempInv)){
							itemList.put(tempInv, itemList.get(tempInv.getItem())+tempInv.getCount());
						}
						else{
							if(tempInv.getCount() > 0){
								itemList.put(tempInv, tempInv.getCount());
								metaList.put(tempInv, tempInv.getItem().getMetadata(tempInv));
								try{
									nbtList.put(tempInv, tempInv.getTagCompound().toString());
								}
								catch(Exception e){
									//There wasn't nbt data found on the item. This is purely to catch that. Nothing needs to be done about it
								}
							}
						}
						//tempInv.setCount(0);
					}
					File fileToWrite = new File("toFactorio.dat");
					while(!fileToWrite.canWrite()){
					}
					FileWriter fileWriter = new FileWriter("toFactorio.dat", true);
					for(ItemStack t : itemList.keySet()){
						String writeString = "";
						if(nbtList.get(t) != null ){
							writeString = t.getItem().getRegistryName().toString() + "|" + metaList.get(t) + "|" + nbtList.get(t) + "~" + itemList.get(t) + "\n";
						}
						else{
							writeString = t.getItem().getRegistryName().toString() + "|" + metaList.get(t) + "~" + itemList.get(t) + "\n";
						}
						t.setCount(0);
						fileWriter.write(writeString);
					}
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ticks++;
	}
	
	public static boolean itemListContains(Map<ItemStack, Integer> itemList, ItemStack compStack){
		for(ItemStack stack : itemList.keySet()){
			if(stack.areItemStacksEqual(stack, compStack)){
				return true;
			}
		}
		return false;
	}
}

