package com.dongle.Events;

import com.dongle.TCRChest.TCREntity;
import com.dongle.TCSChest.TCSEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChunkUnloadEvent {
	
	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event) {
		if(!event.getWorld().isRemote) {
		    for (Object o : event.getChunk().getTileEntityMap().values()) {
		        TileEntity te = (TileEntity) o;
		        if (te instanceof TCSEntity) {
		            ((TCSEntity) te).remove();
		        }
		        if (te instanceof TCREntity) {
		            ((TCREntity) te).remove();
		        }
		    }
		}
	}
}
