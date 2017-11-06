package com.arclighttw.tinyreactors.recipes;

import java.util.List;

import com.arclighttw.tinyreactors.inits.Registry;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class UpgradeRecipe extends Registry.ShapelessRecipe
{
	private final TemplateRecipe template;
	
	public UpgradeRecipe(Block result, TemplateRecipe template) { this(new ItemStack(result), template, new Object[] { result, result }); }
	public UpgradeRecipe(Item result, TemplateRecipe template) { this(new ItemStack(result), template, new Object[] { result, result }); }
	
	public UpgradeRecipe(ItemStack result, TemplateRecipe template, Object... recipe)
	{
		super(result, recipe);
		this.template = template;
	}
	
	@Override
	public boolean matches(InventoryCrafting crafting, World world)
	{
		int empty = 0;
		
		for(int i = 0; i < crafting.getSizeInventory(); i++)
		{
			ItemStack slot = crafting.getStackInSlot(i);
			
			if(slot.isEmpty())
			{
				empty++;
				continue;
			}
			
			if(slot.getItem() != output.getItem())
				return false;
			
			if(output.getMetadata() != 32767 && slot.getMetadata() != output.getMetadata())
				return false;
		}
		
		return empty < crafting.getSizeInventory() - 1;
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
		
		compound.setInteger("limit", template.getLimit());
		compound.setInteger("capacity", template.getCapacity());
		
		int limit = 0;
		int capacity = 0;
		
		for(ItemStack ingredient : ingredients)
		{
			if(!ingredient.hasTagCompound())
				continue;
			
			limit += ingredient.getTagCompound().getInteger("limit");
			capacity += ingredient.getTagCompound().getInteger("capacity");
		}
		
		compound.setInteger("limit", limit);
		compound.setInteger("capacity", capacity);
		
		output.setTagCompound(compound);
		return output.copy();
	}
}