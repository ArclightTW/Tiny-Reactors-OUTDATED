package com.arclighttw.tinyreactors.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container
{
	public ContainerBase(InventoryPlayer player, int inventoryX, int inventoryY)
	{
		this(player, inventoryX, inventoryY + 58, inventoryY);
	}
	
	public ContainerBase(InventoryPlayer player, int inventoryX, int hotbarY, int inventoryY)
	{
		this(player, inventoryX, hotbarY, inventoryX, inventoryY);
	}
	
	public ContainerBase(InventoryPlayer player, int hotbarX, int hotbarY, int inventoryX, int inventoryY)
	{
		for(int x = 0; x < 9; x++)
			addSlotToContainer(new Slot(player, x, hotbarX + x * 18, hotbarY));
		
		for(int y = 0; y < 3; y++)
			for(int x = 0; x < 9; x++)
				addSlotToContainer(new Slot(player, 9 + x + y * 9, inventoryX + x * 18, inventoryY + y * 18));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack returned = ItemStack.EMPTY;
		Slot slot = getSlot(index);
		
		if(slot == null || !slot.getHasStack())
			return returned;
		
		ItemStack itemstack = slot.getStack();
		returned = itemstack.copy();
		
		if(index >= 9 && index < 36)
		{
			if(!mergeItemStack(itemstack, 0, 9, false))
				return ItemStack.EMPTY;
		}
		else if(!mergeItemStack(itemstack, 9, 36, false))
			return ItemStack.EMPTY;
		
		if(itemstack.isEmpty())
			slot.putStack(ItemStack.EMPTY);
		else
			slot.onSlotChanged();
		
		if(itemstack.getCount() == returned.getCount())
			return ItemStack.EMPTY;
		
		return returned;
	}
}
