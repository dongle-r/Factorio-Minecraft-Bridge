package com.dongle.TCSManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.TCSChest.TCSEntity;

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
				Map<Item, Integer> itemList = new HashMap<Item, Integer>();
				try {
					for(TCSEntity ent : entList.values()){
						ItemStack tempInv = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
						if(itemList.containsKey(tempInv.getItem())){
							itemList.put(tempInv.getItem(), itemList.get(tempInv.getItem())+tempInv.getCount());
						}
						else{
							if(tempInv.getCount() > 0){
								itemList.put(tempInv.getItem(), tempInv.getCount());
							}
						}
					}
					FileWriter fileWriter = new FileWriter("toFactorio.dat");
					for(Item t : itemList.keySet()){
						String writeString = t.getRegistryName() + " ~ " + itemList.get(t) + "\n";
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
}

