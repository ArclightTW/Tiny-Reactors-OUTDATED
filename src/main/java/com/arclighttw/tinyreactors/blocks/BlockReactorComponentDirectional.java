package com.arclighttw.tinyreactors.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReactorComponentDirectional extends BlockReactorComponent
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	protected boolean placeFacing;
	
	public BlockReactorComponentDirectional(Material material, String name)
	{
		this(material, name, true);
	}
	
	public BlockReactorComponentDirectional(Material material, String name, boolean placeFacing)
	{
		super(material, name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		
		this.placeFacing = placeFacing;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		
		if(!world.isRemote)
		{
			IBlockState north = world.getBlockState(pos.north());
			IBlockState south = world.getBlockState(pos.south());
			IBlockState west = world.getBlockState(pos.west());
			IBlockState east = world.getBlockState(pos.east());
			EnumFacing facing = (EnumFacing)state.getValue(FACING);
			
			if(facing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock())
				facing = placeFacing ? EnumFacing.SOUTH : EnumFacing.NORTH;
			else if(facing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock())
				facing = placeFacing ? EnumFacing.NORTH : EnumFacing.SOUTH;
			else if(facing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock())
				facing = placeFacing ? EnumFacing.EAST : EnumFacing.WEST;
        	else if(facing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock())
        		facing = placeFacing ? EnumFacing.WEST : EnumFacing.EAST;
			
			world.setBlockState(pos, state.withProperty(FACING, facing), 2);
    	}
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		EnumFacing direction = placer.getHorizontalFacing();
		return getDefaultState().withProperty(FACING, placeFacing ? direction.getOpposite() : direction);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		EnumFacing direction = placer.getHorizontalFacing();
		world.setBlockState(pos, state.withProperty(FACING, placeFacing ? direction.getOpposite() : direction), 2);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing facing = EnumFacing.getFront(meta);
		
		if(facing.getAxis() == EnumFacing.Axis.Y)
			facing = EnumFacing.NORTH;
		
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}
}
