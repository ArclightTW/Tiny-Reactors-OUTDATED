package com.arclighttw.tinyreactors.container;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerReactorController extends ContainerBase
{
	private final TileEntityReactorController controller;
	
	public ContainerReactorController(TileEntityReactorController controller, InventoryPlayer player)
	{
		super(null, player, 8, 81);
		this.controller = controller;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(controller.getPos()) <= 64.0;
	}
	
	public TileEntityReactorController getController()
	{
		return controller;
	}
}
