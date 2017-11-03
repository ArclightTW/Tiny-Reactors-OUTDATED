package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorWastePort;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockReactorWastePort extends BlockReactorComponentDirectional
{
	public BlockReactorWastePort()
	{
		super(Material.IRON, "reactor_waste_port");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorWastePort();
	}
}
