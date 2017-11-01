package com.arclighttw.tinyreactors.managers;

import com.arclighttw.tinyreactors.client.gui.GuiReactorController;
import com.arclighttw.tinyreactors.client.gui.GuiReactorEnergyPort;
import com.arclighttw.tinyreactors.container.ContainerReactorController;
import com.arclighttw.tinyreactors.container.ContainerReactorEnergyPort;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler
{
	public static final int REACTOR_CONTROLLER = 0;
	public static final int REACTOR_ENERGY_PORT = 1;
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		switch(ID)
		{
		case REACTOR_CONTROLLER:
			if(tile != null && tile instanceof TileEntityReactorController)
				return new GuiReactorController((TileEntityReactorController)tile, player.inventory);
		case REACTOR_ENERGY_PORT:
			if(tile != null && tile instanceof TileEntityReactorEnergyPort)
				return new GuiReactorEnergyPort((TileEntityReactorEnergyPort)tile, player.inventory);
		}
		
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		switch(ID)
		{
		case REACTOR_CONTROLLER:
			if(tile != null && tile instanceof TileEntityReactorController)
				return new ContainerReactorController((TileEntityReactorController)tile, player.inventory);
		case REACTOR_ENERGY_PORT:
			if(tile != null && tile instanceof TileEntityReactorEnergyPort)
				return new ContainerReactorEnergyPort((TileEntityReactorEnergyPort)tile, player.inventory);
		}
		
		return null;
	}
}
