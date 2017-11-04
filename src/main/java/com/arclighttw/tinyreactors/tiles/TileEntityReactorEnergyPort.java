package com.arclighttw.tinyreactors.tiles;

public class TileEntityReactorEnergyPort extends TileEntityEnergy
{
	public void setLimit(int limit)
	{
		energy.setMaxTransfer(limit);
	}
	
	public int getLimit()
	{
		return energy.getMaxExtract();
	}
	
	public void setCapacity(int capacity)
	{
		energy.setCapacity(capacity);
	}
}
