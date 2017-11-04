package com.arclighttw.tinyreactors.integration;

import com.arclighttw.tinyreactors.inits.TRBlocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIIntegration implements IModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(TRBlocks.DEGRADED_REACTANT));
	}
}
