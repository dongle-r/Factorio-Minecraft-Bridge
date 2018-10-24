package com.dongle.FMCBridge;

import com.dongle.TCSChest.TCSItem;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	@GameRegistry.ObjectHolder("fmcbridge:tcsitem")
	public static TCSItem tcsitem;
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
		tcsitem.initModel();
	}
}
