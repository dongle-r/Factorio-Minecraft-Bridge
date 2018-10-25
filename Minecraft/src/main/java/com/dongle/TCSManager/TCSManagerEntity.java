package com.dongle.TCSManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.TCSChest.TCSEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TCSManagerEntity  extends TileEntity implements ITickable{
	
	int ticks = 0;
	@Override
	public void update() {
		if(!world.isRemote){
			if(ticks%20 == 0){
				ticks = 0;
				Map<Integer, TCSEntity> entList = FMCBridge.instance.tcsEntityList;
				try {
					FileWriter fileWriter = new FileWriter("testFile.dat");
					for(Integer key : entList.keySet()){
						fileWriter.write("Ent ID: " + key.toString() + "\n");
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

