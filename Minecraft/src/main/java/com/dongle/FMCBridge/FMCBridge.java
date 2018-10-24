package com.dongle.FMCBridge;


import java.util.ArrayList;

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
    	public ItemStack getTabIconItem(){
    		return new ItemStack(Items.BLAZE_ROD);
    	}
    };
    public ArrayList<TCSEntity> tcsEntityList;
    
    @SidedProxy(clientSide = "com.dongle.proxy.ClientProxy", serverSide = "com.dongle.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        tcsEntityList = new ArrayList<TCSEntity>();
    }
}
