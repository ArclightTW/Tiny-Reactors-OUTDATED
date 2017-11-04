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
	@SuppressWarnings("deprecation")
	public void register(IModRegistry registry)
	{
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(TRBlocks.DEGRADED_REACTANT));
		
		registry.addDescription(new ItemStack(TRBlocks.REACTOR_ENERGY_PORT), "Combine Energy Ports to accumulate production.", "e.g. 2x Energy Ports with Limit of 256 RF/t will craft to 1x Energy Port with Limit of 512 RF/t.");
	}
}
