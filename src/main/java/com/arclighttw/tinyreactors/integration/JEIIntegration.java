package com.arclighttw.tinyreactors.integration;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.inits.TRBlocks;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.Minecraft;
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
		registry.addDescription(new ItemStack(TRBlocks.CAPACITOR), "Combine Capacitors to accumulate production.", "e.g. 2x Capacitors with Limit of 256 RF/t will craft to 1x Capacitor with Limit of 512 RF/t.");
		
		registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<GuiTinyManual>() {
			@Override
			public Class<GuiTinyManual> getGuiContainerClass() {
				return GuiTinyManual.class;
			}
			
			@Override
			public List<Rectangle> getGuiExtraAreas(GuiTinyManual guiContainer)
			{
				return Arrays.asList(new Rectangle(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight));
			}
		});
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
	}
}
