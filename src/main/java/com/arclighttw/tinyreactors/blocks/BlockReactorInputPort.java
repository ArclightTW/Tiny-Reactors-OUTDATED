package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.managers.GuiManager;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorInputPort;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockReactorInputPort extends BlockReactorComponentDirectional
{
	public BlockReactorInputPort()
	{
		super(Material.IRON, "reactor_input_port", false);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorInputPort();
	}
	
	@Override
	public int getInterfaceId()
	{
		return GuiManager.REACTOR_INPUT_PORT;
	}
}
