package com.dongle.FMCBridge;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dongle.TCRChest.TCREntity;
import com.dongle.TCSChest.TCSEntity;
import com.dongle.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
        proxy.postInit(e);
    }
    
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
    public int tcrEntID = 0;
    
    //This add deals with adding a newly placed block to the list.
    public int addTCR(TCREntity ent){
    	do{
    		tcrEntID++;
    	}while(tcrEntityList.containsKey(tcrEntID));
    	tcrEntityList.put(tcrEntID, ent);
    	return tcrEntID;
    }
    
    //This add is the ensure that the already placed blocks are added back to the list.
    public int addTCR(int id, TCREntity ent){
    	tcrEntityList.put(id, ent);
    	ent.setAdded(true);
    	return tcrEntID;
    }
    
   //Removes the TCSEntity from the map on block destroy
    public void removeTCR(int x){
    	System.out.println("Ent to remove: " + x);
    	TCREntity temp = tcrEntityList.remove(x);
    }
    
}
