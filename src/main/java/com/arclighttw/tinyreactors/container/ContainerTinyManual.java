package com.arclighttw.tinyreactors.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerTinyManual extends Container
{
	public ContainerTinyManual(EntityPlayer player)
	{
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
}
