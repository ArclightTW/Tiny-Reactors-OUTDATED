package com.arclighttw.tinyreactors.container;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

public class ContainerReactorEnergyPort extends Container
{
	private final TileEntityReactorEnergyPort energyPort;
	
	public ContainerReactorEnergyPort(EntityPlayer player, TileEntityReactorEnergyPort energyPort)
	{
		this.energyPort = energyPort;
		
		// TODO: Player Inventory
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for(IContainerListener listener : listeners)
		{
			listener.sendWindowProperty(this, 0, energyPort.getEnergy().getEnergyStored());
			listener.sendWindowProperty(this, 1, energyPort.getEnergy().getMaxEnergyStored());
		}
	}
	
	@Override
	public void updateProgressBar(int id, int data)
	{
		switch(id)
		{
		case 0:
			energyPort.getEnergy().setEnergyStored(data);
			break;
		case 1:
			energyPort.getEnergy().setMaxEnergyStored(data);
			break;
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(energyPort.getPos()) <= 64.0;
	}
}
