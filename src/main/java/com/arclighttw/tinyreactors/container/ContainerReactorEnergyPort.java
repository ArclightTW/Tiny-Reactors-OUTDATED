package com.arclighttw.tinyreactors.container;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerReactorEnergyPort extends ContainerBase
{
	private final TileEntityReactorEnergyPort energyPort;
	
	public ContainerReactorEnergyPort(TileEntityReactorEnergyPort energyPort, InventoryPlayer player)
	{
		super(null, player, 8, 81);
		this.energyPort = energyPort;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(energyPort.getPos()) <= 64.0;
	}
}
