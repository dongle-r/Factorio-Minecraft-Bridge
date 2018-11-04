package com.dongle.TCRManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.TCRChest.TCREntity;
import com.dongle.TCSChest.TCSEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
						String[] split = str.split("[~|]");
						int _count = Integer.parseInt(split[2]);
						for(TCREntity ent : entList.values()){
							if(ent.getItemLock().equals(split[0])){
								tempInv = (ItemStackHandler) ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
							}
						}
						if(tempInv == null){
							for(TCREntity ent : entList.values()){
								if(ent.getItemLock() == ""){
									tempInv = (ItemStackHandler) ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
								}
							}
						}
						if(tempInv == null){
							System.out.println("cannot insert, all chests locked");
							break;
						}
						for(int i = 0; i < tempInv.getSlots(); i++){
							if(tempInv.getStackInSlot(i) != ItemStack.EMPTY){
								String itemInSlotName = tempInv.getStackInSlot(i).getItem().getRegistryName().toString();
								if((tempInv.getStackInSlot(i).getCount() < 64) && itemInSlotName.equals(split[0]) && tempInv.getStackInSlot(i).getItem().getDamage(tempInv.getStackInSlot(i)) == Integer.parseInt(split[1])){
									Item tempItem = Item.getByNameOrId(split[0]);
									ItemStack remainStack = new ItemStack(tempItem, _count);
									remainStack.setItemDamage(Integer.parseInt(split[1]));
									remainStack = tempInv.insertItem(i, remainStack, false);
									while(remainStack.getCount() > 0){
										for(int j = i+1; j < tempInv.getSlots(); j++){
											itemInSlotName = tempInv.getStackInSlot(j).getItem().getRegistryName().toString();
											if(tempInv.getStackInSlot(j) != ItemStack.EMPTY && tempInv.getStackInSlot(j).getCount() < 64 && itemInSlotName.equals(split[0]) && tempInv.getStackInSlot(i).getItem().getDamage(tempInv.getStackInSlot(i)) == Integer.parseInt(split[1])){
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
								ItemStack tempStack = new ItemStack(tempItem, Integer.parseInt(split[2]));
								tempStack.setItemDamage(Integer.parseInt(split[1]));
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
