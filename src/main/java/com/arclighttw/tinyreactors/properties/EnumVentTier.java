package com.arclighttw.tinyreactors.properties;

import java.util.Locale;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum EnumVentTier implements IStringSerializable
{
	IRON(0.05F),
	GOLD(0.10F),
	DIAMOND(0.20F);
	
	private float heatOffset;
	
	EnumVentTier(float heatOffset)
	{
		this.heatOffset = heatOffset;
	}
	
	@Override
	public String getName()
	{
		return toString().toLowerCase(Locale.UK);
	}
	
	public float getHeatOffset()
	{
		return heatOffset;
	}
	
	public static final PropertyEnum<EnumVentTier> PROPERTY = PropertyEnum.create("venttier", EnumVentTier.class);
}
