package com.arclighttw.tinyreactors.container;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

public class ContainerReactorController extends Container
{
	private final TileEntityReactorController controller;
	
	public ContainerReactorController(EntityPlayer player, TileEntityReactorController controller)
	{
		this.controller = controller;
		
		// TODO: Player Inventory
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for(IContainerListener listener : listeners)
		{
			listener.sendWindowProperty(this, 0, controller.getStructure().getEnergy().getEnergyStored());
			listener.sendWindowProperty(this, 1, controller.getStructure().getEnergy().getMaxEnergyStored());
			
			listener.sendWindowProperty(this, 2, (int)(controller.getStructure().getTemperature().getCurrentTemperature() * 100));
			listener.sendWindowProperty(this, 3, (int)(controller.getStructure().getTemperature().getMaxTemperature() * 100));

			listener.sendWindowProperty(this, 4, controller.isActive() ? 1 : 0);
			listener.sendWindowProperty(this, 5, controller.getStructure().isValid() ? 1 : 0);
			
			listener.sendWindowProperty(this, 6, controller.getStructure().getAvailableYield());
			listener.sendWindowProperty(this, 7, (int)(controller.getStructure().getHeatProduced() * 100));
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void updateProgressBar(int id, int data)
	{
		switch(id)
		{
		case 0:
			controller.getStructure().getEnergy().setEnergyStored(data);
			break;
		case 1:
			controller.getStructure().getEnergy().setMaxEnergyStored(data);
			break;
		case 2:
			controller.getStructure().getTemperature().setCurrentTemperature((double)(data / 100.0));
			break;
		case 3:
			controller.getStructure().getTemperature().setMaxTemperature((double)(data / 100.0));
			break;
		case 4:
			controller.setActive(data == 1);
			break;
		case 5:
			controller.getStructure().setValid(data == 1);
			break;
		case 6:
			controller.getStructure().setAvailableYield(data);
			break;
		case 7:
			controller.getStructure().setHeatProduced((double)(data / 100.0));
			break;
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(controller.getPos()) <= 64.0;
	}
}
