package com.arclighttw.tinyreactors.managers;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryManager
{
	public static boolean addItemStackToInventory(IInventory inventory, ItemStack itemstack)
	{
		for(int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack slot = inventory.getStackInSlot(i);
			
			if(slot.isEmpty())
			{
				inventory.setInventorySlotContents(i, itemstack);
				return true;
			}
			
			if(slot.isStackable() && slot.getItem() == itemstack.getItem())
			{
				int count = slot.getCount() + itemstack.getCount();
				
				if(count <= slot.getMaxStackSize() && count <= inventory.getInventoryStackLimit())
				{
					ItemStack toAdd = slot.copy();
					toAdd.setCount(count);
					
					inventory.setInventorySlotContents(i, toAdd);
					return true;
				}
			}
		}
		
		return false;
	}
}
