package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.blocks.BlockReactorVent;

import net.minecraft.item.ItemStack;

public class ItemReactorVent extends ItemTinyBlock
{
	public ItemReactorVent(BlockReactorVent block)
	{
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return block.getUnlocalizedName() + "." + stack.getItemDamage();
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
}
