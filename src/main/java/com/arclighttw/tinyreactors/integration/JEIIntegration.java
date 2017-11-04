package com.arclighttw.tinyreactors.integration;

import com.arclighttw.tinyreactors.inits.TRBlocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.Item;

@JEIPlugin
public class JEIIntegration implements IModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(TRBlocks.DEGRADED_REACTANT);
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(Item.getItemFromBlock(TRBlocks.DEGRADED_REACTANT));
	}
}
