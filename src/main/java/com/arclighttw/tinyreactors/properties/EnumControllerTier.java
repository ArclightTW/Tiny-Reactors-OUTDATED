package com.arclighttw.tinyreactors.properties;

import com.arclighttw.tinyreactors.storage.EnergyStorageRF;

import net.minecraft.util.IStringSerializable;

public enum EnumControllerTier implements IStringSerializable
{	
	I("1", 5000000),
	II("2", 5000000);

	private String name;
	private int capacity;
	
	private EnumControllerTier(String name, int capacity)
	{
		this.name = name;
		this.capacity = capacity;
	}
	
	public EnergyStorageRF getEnergyStorage()
	{
		return new EnergyStorageRF(capacity);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
}