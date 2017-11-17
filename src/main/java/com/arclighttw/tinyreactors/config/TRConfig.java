package com.arclighttw.tinyreactors.config;

public class TRConfig
{
	public static boolean REACTANT_REMOVAL_ON_FULL_DEGRADATION = true;
	public static String REACTANT_REMOVAL_ON_FULL_DEGRADATION_LABEL = "Should fully degraded reactants (i.e. 0% quality) be destroyed?";
	
	public static String[] REACTANT_REGISTRY = new String[] {
			"minecraft:coal_ore:1",
			"minecraft:iron_ore:2",
			"minecraft:lapis_ore:4",
			"minecraft:redstone_ore:8",
			"minecraft:gold_ore:8",
			"minecraft:diamond_ore:16",
			"minecraft:emerald_ore:32",
			"minecraft:quartz_ore:8",
			
			"minecraft:coal_block:8",
			"minecraft:iron_block:16",
			"minecraft:lapis_block:32",
			"minecraft:redstone_block:64",
			"minecraft:gold_block:64",
			"minecraft:diamond_block:128",
			"minecraft:emerald_block:256",
			"minecraft:quartz_block:32"		
	};
	public static String REACTANT_REGISTRY_LABEL = "Entries should be input in the format (rate is specified per tick):\nmod_id:block_name:rate or\nmod_id:block_name:metadata:rate";
	
	public static boolean REACTOR_MELTDOWN = true;
	public static String REACTOR_MELTDOWN_LABEL = "Whether a Reactor should go into meltdown if it peaks in temperature.";
	
	public static int REACTOR_MELTDOWN_DELAY = 30;
	public static String REACTOR_MELTDOWN_DELAY_LABEL = "The amount of time (in seconds) a Reactor can be at maximum heat before a meltdown occurs.";
	
	public static int REACTOR_MELTDOWN_COOLDOWN_TIMER = 30;
	public static String REACTOR_MELTDOWN_COOLDOWN_TIMER_LABEL = "The amount of time (in seconds) an overheated Reactor takes to fully cool and become operational again.";
	
	public static float REACTOR_HEAT_SCALE = 1;
	public static String REACTOR_HEAT_SCALE_LABEL = "The scale factor to multiply the reactor heat gain/loss by.";
	
	public static boolean REACTANT_DEGRADATION = true;
	public static String REACTANT_DEGRADATION_LABEL = "Whether Reactants in an active Reactor should degrade over time.";
	
	public static int REACTANT_DEGRADATION_TICK = 20;
	public static String REACTANT_DEGRADATION_TICK_LABEL = "The number of ticks between each quality degradation cycle.";
	
	public static boolean MANUAL_ON_SPAWN = true;
	public static String MANUAL_ON_SPAWN_LABEL = "Should players be given a copy of the Tiny Manual when joining the world for the first time?";
	
	public static boolean MANUAL_ON_CRAFT = true;
	public static String MANUAL_ON_CRAFT_LABEL = "Should players be given a copy of the Tiny Manual when crafting a Tiny Reactors based block?  This is exclusive to 'Manual on Spawn'.";
	
	public static float AUDIO_REACTOR_KLAXON = 1.0F;
	public static String AUDIO_REACTOR_KLAXON_LABEL = "The volume level of the warning sound when a reactor is about to overheat.";
	
	public static float AUDIO_REACTOR_OVERHEAT = 1.0F;
	public static String AUDIO_REACTOR_OVERHEAT_LABEL = "The volume level of the shutdown sound when a reactor overheats.";
	
	public static float AUDIO_REACTOR_ACTIVE = 1.0F;
	public static String AUDIO_REACTOR_ACTIVE_LABEL = "The volume level of reactor when it is running normally.";
	
	public static float AUDIO_REACTOR_WARMING = 1.0F;
	public static String AUDIO_REACTOR_WARMING_LABEL = "The volume level of reactor when it is pre-warming.";
}
