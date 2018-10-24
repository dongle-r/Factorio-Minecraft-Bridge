package com.dongle.proxy;

import com.dongle.TCSChest.TCS;
import com.dongle.TCSChest.TCSEntity;
import com.dongle.TCSChest.TCSGui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler{
	
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TCSEntity) {
            return new TCS(player.inventory, (TCSEntity) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TCSEntity) {
        	TCSEntity containerTileEntity = (TCSEntity) te;
            return new TCSGui(containerTileEntity, new TCS(player.inventory, containerTileEntity));
        }
        return null;
    }

}
