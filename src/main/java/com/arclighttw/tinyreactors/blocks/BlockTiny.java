package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.managers.ReactorManager;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTiny extends Block implements ITileEntityProvider
{
	public BlockTiny(Material material, String name)
	{
		super(material);
		setUnlocalizedName(name);
		setCreativeTab(TinyReactors.tab);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(getInterfaceId() <= -1)
			return false;
		
		if(!world.isRemote)
			player.openGui(TinyReactors.instance, getInterfaceId(), world, pos.getX(), pos.getY(), pos.getZ());
		
		return true;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		if(!world.isRemote)
		{
			if(state.getBlock() instanceof BlockReactorController)
				ReactorManager.addReactor(pos);
			
			ReactorManager.validateAllReactors(world);
		}
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if(!world.isRemote)
			ReactorManager.validateAllReactors(world);
		
		super.breakBlock(world, pos, state);
	}
	
	public int getInterfaceId()
	{
		return -1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}	
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}