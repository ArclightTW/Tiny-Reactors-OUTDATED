package com.arclighttw.tinyreactors.config;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class ModConfig
{
	public static Configuration config;
	
	public static void onPreInitialization(File file)
	{
		config = new Configuration(file);
		sync();
		
		MinecraftForge.EVENT_BUS.register(new ModConfig());
	}
	
	public static void sync()
	{
		if(config.hasChanged())
			config.save();
	}
}
