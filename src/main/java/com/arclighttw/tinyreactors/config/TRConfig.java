package com.arclighttw.tinyreactors.config;

public class TRConfig
{
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
	
	public static boolean REACTANT_DEGRADATION = true;
	public static String REACTANT_DEGRADATION_LABEL = "Whether Reactants in an active Reactor should degrade over time.";
	
	public static int REACTANT_DEGRADATION_TICK = 20;
	public static String REACTANT_DEGRADATION_TICK_LABEL = "The number of ticks between each quality degradation cycle.";
}
