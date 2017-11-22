package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockReactorController extends BlockTiny implements ITileEntityProvider
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
}
