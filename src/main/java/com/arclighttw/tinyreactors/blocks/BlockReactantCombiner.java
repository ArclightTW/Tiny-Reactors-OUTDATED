package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.managers.GuiManager;
import com.arclighttw.tinyreactors.tiles.TileEntityReactantCombiner;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockReactantCombiner extends BlockReactorComponentDirectional
{
	public BlockReactantCombiner()
	{
		super(Material.IRON, "reactant_combiner");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactantCombiner();
	}
	
	@Override
	public int getInterfaceId()
	{
		return GuiManager.REACTANT_COMBINER;
	}
}
