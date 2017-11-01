package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.managers.GuiManager;
import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.properties.EnumControllerTier;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.util.Util;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReactorController extends BlockReactorComponentDirectional
{
	private EnumControllerTier tier;
	
	public BlockReactorController(EnumControllerTier tier)
	{
		super(Material.IRON, "reactor_controller_" + tier.getName());
		this.tier = tier;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorController(tier);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(getInterfaceId() <= -1)
			return false;
		
		if(!world.isRemote)
		{
			((TileEntityReactorController)world.getTileEntity(pos)).getMultiblock().checkValidity(world, pos);
			player.openGui(TinyReactors.instance, getInterfaceId(), world, pos.getX(), pos.getY(), pos.getZ());
		}
		
		return true;
	}
	
	@Override
	public int getInterfaceId()
	{
		return GuiManager.REACTOR_CONTROLLER;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return tier == EnumControllerTier.II;
	}
	
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		if(!canProvidePower(state))
			return 0;
		
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorController))
			return 0;
		
		EnumFacing facing = state.getValue(BlockReactorComponentDirectional.FACING);
		
		if(facing != side.getOpposite())
			return 0;
		
		TileEntityReactorController controller = (TileEntityReactorController)tile;
		return Util.getTemperatureScaled(controller.getTemperature(), 15);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if(!world.isRemote)
			ReactorManager.removeReactor(world, pos);
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(String.format("Tier: %s", tier.getName()));
	}
	
	public EnumControllerTier getTier()
	{
		return tier;
	}
}
