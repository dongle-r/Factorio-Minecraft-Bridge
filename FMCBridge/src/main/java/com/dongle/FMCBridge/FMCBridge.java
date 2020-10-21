package com.dongle.FMCBridge;


import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dongle.Events.ChunkUnloadEvent;
import com.dongle.Events.WorldLoadEvent;
import com.dongle.TCRChest.TCREntity;
import com.dongle.TCSChest.TCSEntity;
import com.dongle.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = FMCBridge.MODID, name = FMCBridge.MODNAME, version = FMCBridge.VERSION)
public class FMCBridge
{
    public static final String MODID = "fmcbridge";
    public static final String MODNAME = "FMC Bridge";
    public static final String VERSION = "1.0";
    
    @Mod.Instance
    public static FMCBridge instance = new FMCBridge();
    public static CreativeTabs fmcTab = new CreativeTabs("FMC Bridge"){

		@Override
		public ItemStack createIcon() {
			// TODO Auto-generated method stub
    		return new ItemStack(Items.BLAZE_ROD);

		}
    };
    
    
    @SidedProxy(clientSide = "com.dongle.proxy.ClientProxy", serverSide = "com.dongle.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        //ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkLoadingCallback());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        tcsEntityList = new HashMap<Integer, TCSEntity>();
        tcrEntityList = new LinkedHashMap<Integer, TCREntity>();
    }

    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	MinecraftForge.EVENT_BUS.register( new WorldLoadEvent());
    	MinecraftForge.EVENT_BUS.register(new ChunkUnloadEvent());
        proxy.postInit(e);
    }
    
	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		System.out.println("Reseting Values");
		this.tcrEntityList.clear();
		this.tcsEntityList.clear();
		this.sendManager = null;
		this.receieveManager = null;
		tcsEntId = 0;
		tcrEntId = 0;

		if(FMCBridge.instance.queues_logged_in) {
			FMCBridge.instance.queues.threadInput.stop();
			FMCBridge.instance.queues.threadOutput.stop();
			try {
				FMCBridge.instance.queues.networkQueue.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
    
    public ItemQueues queues;
    public boolean queues_logged_in;
    public Ticket sendTicket;
    public Ticket receiveTicket;
    public TCSEntity sendManager = null;
    public TCREntity receieveManager = null;
    
	
    ///TCS Entity Handling
    ///Below is the handling for adding and updating the Transfer Chest Sender (TCS) Logic 
    ///It uses a map and an entId to assign the TCSEntities to a map with a specific id.
    public Map<Integer, TCSEntity> tcsEntityList;
    public int tcsEntId = 0;

    //This add deals with adding a newly placed block to the list.
    public int addTCS(TCSEntity ent){
    	do{
    		tcsEntId++;
    	}while(tcsEntityList.containsKey(tcsEntId));
    	tcsEntityList.put(tcsEntId, ent);
    	return tcsEntId;
    }
    
    //This add is the ensure that the already placed blocks are added back to the list.
    public int addTCS(int id, TCSEntity ent){
    	tcsEntityList.put(id, ent);
    	ent.setAdded(true);
    	return tcsEntId;
    }
    
   //Removes the TCSEntity from the map on block destroy
    public void removeTCS(int x){
    	System.out.println("Ent to remove: " + x);
    	TCSEntity temp = tcsEntityList.remove(x);
    }
    
    ///TCR Entity Handling
    ///Below is the handling for adding and updating the Transfer Chest Sender (TCS) Logic 
    ///It uses a map and an entId to assign the TCSEntities to a map with a specific id.
    public Map<Integer, TCREntity> tcrEntityList;
    public int tcrEntId = 0;
    
    //This add deals with adding a newly placed block to the list.
    public int addTCR(TCREntity ent){
    	do{
    		tcrEntId++;
    	}while(tcrEntityList.containsKey(tcrEntId));
    	tcrEntityList.put(tcrEntId, ent);
    	return tcrEntId;
    }
    
    //This add is the ensure that the already placed blocks are added back to the list.
    public int addTCR(int id, TCREntity ent){
    	tcrEntityList.put(id, ent);
    	ent.setAdded(true);
    	return tcrEntId;
    }
    
   //Removes the TCSEntity from the map on block destroy
    public void removeTCR(int x){
    	System.out.println("Ent to remove: " + x);
    	TCREntity temp = tcrEntityList.remove(x);
    }
    
}
