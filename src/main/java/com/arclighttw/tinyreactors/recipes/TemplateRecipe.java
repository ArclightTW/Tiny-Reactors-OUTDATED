package com.arclighttw.tinyreactors.recipes;

import java.util.List;

import com.arclighttw.tinyreactors.inits.Registry;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TemplateRecipe extends Registry.ShapedRecipe
{
	private final int limit;
	private final int capacity;
	
	public TemplateRecipe(Block result, int limit, int capacity, Object... recipe) { this(new ItemStack(result), limit, capacity, recipe); }
	public TemplateRecipe(Item result, int limit, int capacity, Object... recipe) { this(new ItemStack(result), limit, capacity, recipe); }
	
	public TemplateRecipe(ItemStack result, int limit, int capacity, Object... recipe)
	{
		super(result, recipe);
		this.limit = limit;
		this.capacity = capacity;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting)
	{
		List<ItemStack> ingredients = Lists.newArrayList();
		
		for(int i = 0; i < crafting.getSizeInventory(); i++)
		{
			ItemStack ingredient = crafting.getStackInSlot(i);
			
			if(ingredient.isEmpty())
				continue;
			
			ingredients.add(ingredient);
		}
		
		NBTTagCompound compound = output.getTagCompound();
		
		if(compound == null)
			compound = new NBTTagCompound();
		
		compound.setInteger("limit", limit);
		compound.setInteger("capacity", capacity);
		
		output.setTagCompound(compound);
		return output.copy();
	}
}