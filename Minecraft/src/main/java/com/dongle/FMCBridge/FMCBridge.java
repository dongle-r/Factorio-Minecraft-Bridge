package com.dongle.FMCBridge;


import java.util.HashMap;
import java.util.Map;

import com.dongle.TCSChest.TCSEntity;
import com.dongle.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

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
    }

    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
    
    //Below is the handling for adding and updating the Transfer Chest Sender (TCS) Logic 
    //It uses a map and an entId to assign the TCSEntities to a map with a specific id.
    public Map<Integer, TCSEntity> tcsEntityList;
    public int entId = 0;
    
    //This add deals with adding a newly placed block to the list.
    public int addTCS(TCSEntity ent){
    	do{
    		entId++;
    	}while(tcsEntityList.containsKey(entId));
    	tcsEntityList.put(entId, ent);
    	return entId;
    }
    
    //This add is the ensure that the already placed blocks are added back to the list.
    public int addTCS(int id, TCSEntity ent){
    	tcsEntityList.put(id, ent);
    	ent.setAdded(true);
    	return entId;
    }
    
   //Removes the TCSEntity from the map on block destroy
    public void removeTCS(int x){
    	System.out.println("Ent to remove: " + x);
    	TCSEntity temp = tcsEntityList.remove(x);
    }
}
