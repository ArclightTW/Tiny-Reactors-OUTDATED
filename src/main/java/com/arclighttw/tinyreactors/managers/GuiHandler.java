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
import net.minecraftforge.fml.relauncher.Side;

public class GuiHandler implements IGuiHandler
{
	public static final int REACTOR_CONTROLLER = 0;
	public static final int REACTOR_ENERGY_PORT = 1;
	
	public Object getGuiElement(int ID, EntityPlayer player, TileEntity tile, Side side)
	{
		switch(ID)
		{
		case REACTOR_CONTROLLER:
			return side == Side.SERVER ? new ContainerReactorController(player, (TileEntityReactorController)tile) : new GuiReactorController(player, (TileEntityReactorController)tile);
		case REACTOR_ENERGY_PORT:
			return side == Side.SERVER ? new ContainerReactorEnergyPort(player, (TileEntityReactorEnergyPort)tile) : new GuiReactorEnergyPort(player, (TileEntityReactorEnergyPort)tile);
		}
		
		return null;
	}
	
	public Object getGuiElement(int ID, EntityPlayer player, Side side)
	{
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if(tile == null)
			return getGuiElement(ID, player, Side.CLIENT);
		else
			return getGuiElement(ID, player, tile, Side.CLIENT);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if(tile == null)
			return getGuiElement(ID, player, Side.SERVER);
		else
			return getGuiElement(ID, player, tile, Side.SERVER);
	}
}
