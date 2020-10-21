package com.dongle.TCSChest;

import com.dongle.FMCBridge.FMCBridge;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TCSBlock extends Block implements ITileEntityProvider {
	
	public static final int GUI_ID = 1;
	
	public TCSBlock(){
		super(Material.ROCK);
		setTranslationKey(FMCBridge.MODID + ".tcsblock");
		setRegistryName("tcsblock");
		setCreativeTab(FMCBridge.fmcTab);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state){
		return true;
	}
	
	public TCSEntity getTE(World world, BlockPos pos) {
		return (TCSEntity)world.getTileEntity(pos);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTE(world, pos).setPos(pos);
			System.out.println("set block pos: " + getTE(world,pos).getPos());
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta){
		TCSEntity temp = new TCSEntity();
		return temp;
	}
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (hasTileEntity(state))
        { 
        	if(!worldIn.isRemote){
        		TileEntity tileEnt = worldIn.getTileEntity(pos);
        		if(tileEnt instanceof TCSEntity){
        			TCSEntity tcsEnt = (TCSEntity)tileEnt;
        			tcsEnt.remove();
        		}
        	}
            worldIn.removeTileEntity(pos);
        }
    }
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(worldIn.isRemote){
			return true;
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if(!(te instanceof TCSEntity)){
			return false;
		}
		playerIn.openGui(FMCBridge.instance, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
