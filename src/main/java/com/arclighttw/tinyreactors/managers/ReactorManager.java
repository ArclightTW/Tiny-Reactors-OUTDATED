package com.arclighttw.tinyreactors.managers;

import java.util.List;

import com.arclighttw.tinyreactors.blocks.BlockReactorController;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

public class ReactorManager
{
	private static final List<BlockPos> CONTROLLERS = Lists.newArrayList();
	
	public static void addReactor(BlockSnapshot snapshot)
	{
		if(!(snapshot.getCurrentBlock().getBlock() instanceof BlockReactorController))
			return;
		
		if(!CONTROLLERS.contains(snapshot.getPos()))
			CONTROLLERS.add(snapshot.getPos());
	}
	
	public static void addReactor(BlockPos pos, TileEntityReactorController controller)
	{
		if(controller == null)
			return;
		
		if(!CONTROLLERS.contains(pos))
			CONTROLLERS.add(pos);
	}
	
	public static void validateAll(World world, BlockPos added, IBlockState addedBlock, BlockPos removed)
	{
		List<BlockPos> remove = Lists.newArrayList();
		
		for(BlockPos pos : CONTROLLERS)
		{
			if(validate(world, pos, added, addedBlock, removed))
				continue;
			
			remove.add(pos);
		}
		
		for(BlockPos pos : remove)
			removeReactor(pos);
	}
	
	private static boolean validate(World world, BlockPos pos, BlockPos added, IBlockState addedBlock, BlockPos removed)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorController))
			return false;
		
		((TileEntityReactorController)tile).validateController(added, addedBlock, removed);
		return true;
	}
	
	private static void removeReactor(BlockPos pos)
	{
		CONTROLLERS.remove(pos);
	}
}
