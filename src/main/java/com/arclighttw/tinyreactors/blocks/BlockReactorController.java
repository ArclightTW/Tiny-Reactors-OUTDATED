package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

public class BlockReactorController extends BlockReactorComponent
{
	public BlockReactorController(String name)
	{
		super(name, Material.IRON);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorController();
	}
	
	@Override
	public void onReactorComponentPlaced(World world, BlockSnapshot snapshot)
	{
		ReactorManager.addReactor(snapshot);
		super.onReactorComponentPlaced(world, snapshot);
	}
	
	@Override
	public void onReactorComponentRemoved(World world, BlockPos pos)
	{
		ReactorManager.removeReactor(pos);
		super.onReactorComponentRemoved(world, pos);
	}
}
