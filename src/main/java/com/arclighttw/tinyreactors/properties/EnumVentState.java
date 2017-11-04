package com.arclighttw.tinyreactors.properties;

import java.util.Locale;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum EnumVentState implements IStringSerializable
{
	CLOSED,
	OPEN;
	
	EnumVentState()
	{
	}
	
	@Override
	public String getName()
	{
		return toString().toLowerCase(Locale.UK);
	}
	
	public static final PropertyEnum<EnumVentState> PROPERTY = PropertyEnum.create("ventstate", EnumVentState.class);
}
