package com.arclighttw.tinyreactors.properties;

import java.util.Locale;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum EnumVentTier implements IStringSerializable
{
	IRON(0.5F, 8),
	GOLD(1F, 16),
	DIAMOND(2F, 32);
	
	private float heatOffset;
	private int particleCount;
	
	EnumVentTier(float heatOffset, int particleCount)
	{
		this.heatOffset = heatOffset;
		this.particleCount = particleCount;
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
	
	public int getParticleCount()
	{
		return particleCount;
	}
	
	public static final PropertyEnum<EnumVentTier> PROPERTY = PropertyEnum.create("venttier", EnumVentTier.class);
}
