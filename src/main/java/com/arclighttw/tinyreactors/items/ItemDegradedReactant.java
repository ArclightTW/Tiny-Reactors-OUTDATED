package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.blocks.BlockDegradedReactant;
import com.arclighttw.tinyreactors.client.model.ModelItemDegradedReactant;
import com.arclighttw.tinyreactors.inits.Registry.IModelProvider;
import com.arclighttw.tinyreactors.inits.Registry.IRuntimeModel;

import net.minecraft.item.ItemBlock;

public class ItemDegradedReactant extends ItemBlock implements IModelProvider
{
	public ItemDegradedReactant(BlockDegradedReactant block)
	{
		super(block);
	}
	
	@Override
	public IRuntimeModel createModel()
	{
		return new ModelItemDegradedReactant();
	}
}
