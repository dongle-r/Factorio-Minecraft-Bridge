package com.dongle.proxy;

import com.dongle.FMCBridge.FMCBridge;
import com.dongle.FMCBridge.ModBlocks;
import com.dongle.TCRChest.TCRBlock;
import com.dongle.TCRChest.TCREntity;
import com.dongle.TCRManager.TCRManagerBlock;
import com.dongle.TCRManager.TCRManagerEntity;
import com.dongle.TCSChest.TCSBlock;
import com.dongle.TCSChest.TCSEntity;
import com.dongle.TCSManager.TCSManagerBlock;
import com.dongle.TCSManager.TCSManagerEntity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(FMCBridge.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    	event.getRegistry().register(new TCSBlock());
    	GameRegistry.registerTileEntity(TCSEntity.class, FMCBridge.MODID + "_tcsblock");
    	
    	event.getRegistry().register(new TCRBlock());
    	GameRegistry.registerTileEntity(TCREntity.class, FMCBridge.MODID + "_tcrblock");
    	
    	event.getRegistry().register(new TCSManagerBlock());
    	GameRegistry.registerTileEntity(TCSManagerEntity.class, FMCBridge.MODID + "_tcsmanagerentity");
    	
    	event.getRegistry().register(new TCRManagerBlock());
    	GameRegistry.registerTileEntity(TCRManagerEntity.class, FMCBridge.MODID + "_tcrmanagerentity");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	event.getRegistry().register(new ItemBlock(ModBlocks.tcsblock).setRegistryName(ModBlocks.tcsblock.getRegistryName())); 
    	event.getRegistry().register(new ItemBlock(ModBlocks.tcrblock).setRegistryName(ModBlocks.tcrblock.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.tcsManagerBlock).setRegistryName(ModBlocks.tcsManagerBlock.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.tcrManagerBlock).setRegistryName(ModBlocks.tcrManagerBlock.getRegistryName()));
    }
    
}