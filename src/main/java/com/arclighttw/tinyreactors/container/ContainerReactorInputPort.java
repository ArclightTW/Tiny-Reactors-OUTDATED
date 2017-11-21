package com.arclighttw.tinyreactors.container;

import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorInputPort;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerReactorInputPort extends ContainerBase
{
	private final TileEntityReactorInputPort input;
	
	public ContainerReactorInputPort(TileEntityReactorInputPort input, InventoryPlayer player)
	{
		super(input, player, 8, 81);
		this.input = input;
		
		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 3; x++)
				addSlotToContainer(new Slot(input, x + y * 3, 62 + x * 18, 15 + y * 18) {
					@Override
					public boolean isItemValid(ItemStack stack)
					{
						return ReactorManager.isReactant(stack);
					}
				});
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(input.getPos()) <= 64.0;
	}
}
