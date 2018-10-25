package com.dongle.FMCBridge;

import com.dongle.TCSChest.TCSBlock;
import com.dongle.TCSManager.TCSManagerBlock;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	@GameRegistry.ObjectHolder("fmcbridge:tcsblock")
	public static TCSBlock tcsblock;
	
	@GameRegistry.ObjectHolder("fmcbridge:tcsmanagerblock")
	public static TCSManagerBlock tcsManagerBlock;
	
	@SideOnly(Side.CLIENT)
	public static void initModels(){
		tcsblock.initModel();
		tcsManagerBlock.initModel();
	}
	
}
