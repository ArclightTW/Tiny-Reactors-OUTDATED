package com.arclighttw.tinyreactors.storage;

import com.arclighttw.tinyreactors.util.Function3;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryStorage extends ItemStackHandler
{
	private Function3<InventoryStorage, Integer, ItemStack, Boolean> insertionLogic;
	private Function3<InventoryStorage, Integer, Integer, Boolean> extractionLogic;
	
	public InventoryStorage(int size)
	{
		super(size);
	}
	
	public InventoryStorage setInsertionLogic(Function3<InventoryStorage, Integer, ItemStack, Boolean> insertionLogic)
	{
		this.insertionLogic = insertionLogic;
		return this;
	}
	
	public InventoryStorage setExtractionLogic(Function3<InventoryStorage, Integer, Integer, Boolean> extractionLogic)
	{
		this.extractionLogic = extractionLogic;
		return this;
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if(insertionLogic != null && !insertionLogic.invoke(this, slot, stack))
			return stack;
			
		return super.insertItem(slot, stack, simulate);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if(extractionLogic != null && !extractionLogic.invoke(this, slot, amount))
			return ItemStack.EMPTY;
		
		return super.extractItem(slot, amount, simulate);
	}
}
