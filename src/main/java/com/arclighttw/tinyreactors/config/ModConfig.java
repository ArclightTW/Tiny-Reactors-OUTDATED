package com.arclighttw.tinyreactors.config;

import java.io.File;

import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.managers.ReactorManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModConfig
{
	public static Configuration config;
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(!event.getModID().equals(Reference.ID))
			return;
		
		syncConfig();
	}
	
	public static void initialize(File file)
	{
		config = new Configuration(file);
		syncConfig();
		
		MinecraftForge.EVENT_BUS.register(new ModConfig());
	}
	
	public static void syncConfig()
	{
		String category;
		
		category = "General";
		config.addCustomCategoryComment(category, "General settings regarding Tiny Reactors");
		
		TRConfig.REACTOR_MELTDOWN = config.getBoolean("Reactor Meltdown", category, TRConfig.REACTOR_MELTDOWN, TRConfig.REACTOR_MELTDOWN_LABEL);
		TRConfig.REACTOR_MELTDOWN_DELAY = config.getInt("Reactor Meltdown Delay", category, TRConfig.REACTOR_MELTDOWN_DELAY, 1, Integer.MAX_VALUE, TRConfig.REACTOR_MELTDOWN_DELAY_LABEL);
		TRConfig.REACTOR_MELTDOWN_COOLDOWN_TIMER = config.getInt("Reactor Meltdown Cooldown Timer", category, TRConfig.REACTOR_MELTDOWN_COOLDOWN_TIMER, 1, Integer.MAX_VALUE, TRConfig.REACTOR_MELTDOWN_COOLDOWN_TIMER_LABEL);
		
		TRConfig.REACTANT_DEGRADATION = config.getBoolean("Reactant Degradation", category, TRConfig.REACTANT_DEGRADATION, TRConfig.REACTANT_DEGRADATION_LABEL);
		TRConfig.REACTANT_DEGRADATION_TICK = config.getInt("Reactant Degradation Tick", category, TRConfig.REACTANT_DEGRADATION_TICK, 1, Integer.MAX_VALUE, TRConfig.REACTANT_DEGRADATION_TICK_LABEL);
		TRConfig.REACTANT_REMOVAL_ON_FULL_DEGRADATION = config.getBoolean("Remove Fully Degraded Reactants", category, TRConfig.REACTANT_REMOVAL_ON_FULL_DEGRADATION, TRConfig.REACTANT_REMOVAL_ON_FULL_DEGRADATION_LABEL);		

		TRConfig.REACTOR_HEAT_SCALE = config.getFloat("Reactor Heat Scale", category, TRConfig.REACTOR_HEAT_SCALE, 1, Float.MAX_VALUE, TRConfig.REACTOR_HEAT_SCALE_LABEL);
		
		category = "Manual";
		
		TRConfig.MANUAL_ON_SPAWN = config.getBoolean("Manual on Spawn", category, TRConfig.MANUAL_ON_SPAWN, TRConfig.MANUAL_ON_SPAWN_LABEL);
		TRConfig.MANUAL_ON_CRAFT = config.getBoolean("Manual on Craft", category, TRConfig.MANUAL_ON_CRAFT, TRConfig.MANUAL_ON_CRAFT_LABEL);
		
		category = "Reactants";
		config.addCustomCategoryComment(category, "Specify the rate per tick of each Block when placed inside an active reactor.");
		
		TRConfig.REACTANT_REGISTRY = config.getStringList("Entries", category, TRConfig.REACTANT_REGISTRY, TRConfig.REACTANT_REGISTRY_LABEL);
		
		category = "Audio";
		config.addCustomCategoryComment(category, "Increase or decrease the audio levels.");
		
		
		TRConfig.AUDIO_REACTOR_WARMING = config.getFloat("Reactor Pre-Warming", category, TRConfig.AUDIO_REACTOR_WARMING, 0F, 5F, TRConfig.AUDIO_REACTOR_WARMING_LABEL);
		TRConfig.AUDIO_REACTOR_ACTIVE = config.getFloat("Reactor Active", category, TRConfig.AUDIO_REACTOR_ACTIVE, 0F, 5F, TRConfig.AUDIO_REACTOR_ACTIVE_LABEL);
		
		TRConfig.AUDIO_REACTOR_KLAXON = config.getFloat("Reactor Overheat Warning", category, TRConfig.AUDIO_REACTOR_KLAXON, 0F, 5F, TRConfig.AUDIO_REACTOR_KLAXON_LABEL);
		TRConfig.AUDIO_REACTOR_OVERHEAT = config.getFloat("Reactor Overheat Shutdown", category, TRConfig.AUDIO_REACTOR_OVERHEAT, 0F, 5F, TRConfig.AUDIO_REACTOR_OVERHEAT_LABEL);
		
		TRConfig.AUDIO_TINY_WRENCH = config.getFloat("Tiny Wrench", category, TRConfig.AUDIO_TINY_WRENCH, 0F, 5F, TRConfig.AUDIO_TINY_WRENCH_LABEL);
		
		ReactorManager.populate();
		
		if(config.hasChanged())
			config.save();
	}
}
