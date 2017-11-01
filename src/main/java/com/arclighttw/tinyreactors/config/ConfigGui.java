package com.arclighttw.tinyreactors.config;

import java.util.List;

import com.arclighttw.tinyreactors.main.Reference;
import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGui extends GuiConfig
{
	public ConfigGui(GuiScreen parent)
	{
		super(parent, getConfigElements(), Reference.ID, false, false, GuiConfig.getAbridgedConfigPath(ModConfig.config.toString()));
	}
	
	static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = Lists.newArrayList();
		
		for(String category : ModConfig.config.getCategoryNames())
			list.add(new ConfigElement(ModConfig.config.getCategory(category)));
		
		return list;
	}
}
