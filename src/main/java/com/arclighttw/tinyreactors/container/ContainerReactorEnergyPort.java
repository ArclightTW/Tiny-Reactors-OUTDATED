package com.arclighttw.tinyreactors.container;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerReactorEnergyPort extends Container
{
	private final TileEntityReactorEnergyPort energyPort;
	
	public ContainerReactorEnergyPort(TileEntityReactorEnergyPort energyPort, InventoryPlayer player)
	{
		this.energyPort = energyPort;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(energyPort.getPos()) <= 64.0;
	}
}
