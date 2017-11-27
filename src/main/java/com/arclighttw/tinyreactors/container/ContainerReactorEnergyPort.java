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
			
			listener.sendWindowProperty(this, 2, Integer.valueOf((int)energyPort.getEnergy().getCurrentReceive()));
			listener.sendWindowProperty(this, 3, Integer.valueOf((int)energyPort.getEnergy().getMaxReceive()));
			listener.sendWindowProperty(this, 4, Integer.valueOf((int)energyPort.getEnergy().getCurrentExtract()));
			listener.sendWindowProperty(this, 5, Integer.valueOf((int)energyPort.getEnergy().getMaxExtract()));
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
		case 2:
			energyPort.getEnergy().setCurrentReceive(Long.valueOf((long)data));
			break;
		case 3:
			energyPort.getEnergy().setMaxReceive(Long.valueOf((long)data));
			break;
		case 4:
			energyPort.getEnergy().setCurrentExtract(Long.valueOf((long)data));
			break;
		case 5:
			energyPort.getEnergy().setMaxExtract(Long.valueOf((long)data));
			break;
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(energyPort.getPos()) <= 64.0;
	}
}
