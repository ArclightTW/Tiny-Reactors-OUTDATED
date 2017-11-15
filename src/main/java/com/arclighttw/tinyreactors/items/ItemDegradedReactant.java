package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.blocks.BlockDegradedReactant;
import com.arclighttw.tinyreactors.client.model.ModelItemDegradedReactant;
import com.arclighttw.tinyreactors.inits.Registry.IModelProvider;
import com.arclighttw.tinyreactors.inits.Registry.IRuntimeModel;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDegradedReactant extends ItemTinyBlock implements IModelProvider
{
	public ItemDegradedReactant(BlockDegradedReactant block)
	{
		super(block);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRuntimeModel createModel()
	{
		return new ModelItemDegradedReactant();
	}
}
