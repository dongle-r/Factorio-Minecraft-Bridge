package com.dongle.Events;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.FMCBridge.ItemQueues;
import com.dongle.TCRChest.TCREntity;
import com.dongle.TCSChest.TCSEntity;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldLoadEvent {

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load e){
		if(!e.getWorld().isRemote){
			FMCBridge.instance.tcrEntityList = new LinkedHashMap<Integer, TCREntity>();
			FMCBridge.instance.tcsEntityList = new HashMap<Integer, TCSEntity>();

			if(!FMCBridge.instance.queues_logged_in) {
				FMCBridge.instance.queues_logged_in = false;
				FMCBridge.instance.queues = new ItemQueues();
				//FMCBridge.instance.sendTicket = ForgeChunkManager.requestTicket(FMCBridge.instance, e.getWorld(), ForgeChunkManager.Type.NORMAL);
				//FMCBridge.instance.receiveTicket = ForgeChunkManager.requestTicket(FMCBridge.instance, e.getWorld(), ForgeChunkManager.Type.NORMAL);
			}
		}
	}
}
