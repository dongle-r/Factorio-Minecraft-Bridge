package com.dongle.proxy;

import java.io.File;

import com.dongle.FMCBridge.Config;
import com.dongle.FMCBridge.FMCBridge;
import com.dongle.FMCBridge.ModBlocks;
import com.dongle.TCRChest.TCRBlock;
import com.dongle.TCRChest.TCREntity;
import com.dongle.TCSChest.TCSBlock;
import com.dongle.TCSChest.TCSEntity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	 // Config instance
    public static Configuration config;
    
    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "fmcbridge.cfg"));
        Config.readConfig();
    }

    public void init(FMLInitializationEvent e) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(FMCBridge.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    	event.getRegistry().register(new TCSBlock());
    	GameRegistry.registerTileEntity(TCSEntity.class, FMCBridge.MODID + "_tcsblock");
    	
    	event.getRegistry().register(new TCRBlock());
    	GameRegistry.registerTileEntity(TCREntity.class, FMCBridge.MODID + "_tcrblock");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	event.getRegistry().register(new ItemBlock(ModBlocks.tcsblock).setRegistryName(ModBlocks.tcsblock.getRegistryName())); 
    	event.getRegistry().register(new ItemBlock(ModBlocks.tcrblock).setRegistryName(ModBlocks.tcrblock.getRegistryName()));

    }
    
}