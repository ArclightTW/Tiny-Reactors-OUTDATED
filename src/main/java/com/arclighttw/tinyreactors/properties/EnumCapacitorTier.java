package com.arclighttw.tinyreactors.properties;

import com.arclighttw.tinyreactors.storage.EnergyStorageRF;

import net.minecraft.util.IStringSerializable;

public enum EnumCapacitorTier implements IStringSerializable
{	
	I("1", 5000000, 512),
	II("2", 10000000, 1024),
	III("3", 50000000, 2048);

	private String name;
	
	private int capacity;
	private int maxTransfer;
	
	private EnumCapacitorTier(String name, int capacity, int maxTransfer)
	{
		this.name = name;
		
		this.capacity = capacity;
		this.maxTransfer = maxTransfer;
	}
	
	public EnergyStorageRF getEnergyStorage()
	{
		return new EnergyStorageRF(capacity, maxTransfer);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
}