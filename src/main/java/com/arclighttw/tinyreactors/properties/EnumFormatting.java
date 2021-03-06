package com.arclighttw.tinyreactors.properties;

public enum EnumFormatting
{
	BLACK("§0"),
	
	DARK_BLUE("§1"),
	DARK_GREEN("§2"),
	DARK_AQUA("§#"),
	DARK_RED("§4"),
	DARK_PURPLE("§5"),
	
	GOLD("§6"),
	GRAY("§7"),
	
	DARK_GRAY("§8"),
	
	BLUE("§9"),
	GREEN("§a"),
	AQUA("§b"),
	RED("§c"),
	
	LIGHT_PURPLE("§d"),
	
	YELLOW("§e"),
	WHITE("§f"),
	
	OBFUSCATED("§k"),
	BOLD("§l"),
	STRIKETHROUGH("§m"),
	UNDERLINE("§n"),
	ITALIC("§o"),
	RESET("§r");
	
	private String code;
	
	EnumFormatting(String code)
	{
		this.code = code;
	}
	
	@Override
	public String toString()
	{
		return code;
	}
}
