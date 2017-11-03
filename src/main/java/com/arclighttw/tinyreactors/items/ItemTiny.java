package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.main.TinyReactors;

import net.minecraft.item.Item;

public class ItemTiny extends Item
{
	public ItemTiny(String name)
	{
		setUnlocalizedName(name);
		setCreativeTab(TinyReactors.tab);
	}
}
