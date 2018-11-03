package com.dongle.FMCBridge;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.dongle.TCRChest.TCREntity;
import com.dongle.TCSChest.TCSEntity;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoadEvent {

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load e){
		if(!e.getWorld().isRemote){
			FMCBridge.instance.tcrEntityList = new LinkedHashMap<Integer, TCREntity>();
			FMCBridge.instance.tcsEntityList = new HashMap<Integer, TCSEntity>();
		}
	}
}
