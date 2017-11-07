package com.arclighttw.tinyreactors.managers;

import com.arclighttw.tinyreactors.client.gui.GuiReactantCombiner;
import com.arclighttw.tinyreactors.client.gui.GuiReactorController;
import com.arclighttw.tinyreactors.client.gui.GuiReactorEnergyPort;
import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.container.ContainerReactantCombiner;
import com.arclighttw.tinyreactors.container.ContainerReactorController;
import com.arclighttw.tinyreactors.container.ContainerReactorEnergyPort;
import com.arclighttw.tinyreactors.container.ContainerTinyManual;
import com.arclighttw.tinyreactors.tiles.TileEntityReactantCombiner;
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
	public static final int REACTANT_COMBINER = 2;
	
	public static final int TINY_MANUAL = 3;
	
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
		case REACTANT_COMBINER:
			if(tile != null && tile instanceof TileEntityReactantCombiner)
				return new GuiReactantCombiner((TileEntityReactantCombiner)tile, player.inventory);
		
		case TINY_MANUAL:
			return new GuiTinyManual(player);
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
		case REACTANT_COMBINER:
			if(tile != null && tile instanceof TileEntityReactantCombiner)
				return new ContainerReactantCombiner((TileEntityReactantCombiner)tile, player.inventory);
			
		case TINY_MANUAL:
			return new ContainerTinyManual(player);
		}
		
		return null;
	}
}
