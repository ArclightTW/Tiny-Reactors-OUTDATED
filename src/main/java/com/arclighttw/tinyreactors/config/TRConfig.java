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
	public static String REACTANT_REGISTRY_LABEL = "Entries should be input in the format (rate is specified per tick):\nmod_id:block_name:rate";
}
