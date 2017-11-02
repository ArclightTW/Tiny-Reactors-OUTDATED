package com.arclighttw.tinyreactors.storage;

import net.minecraft.nbt.NBTTagCompound;

public class TemperatureStorage
{
	public static final float BASE_TEMPERATURE_CAP = 250F;
	
	private float currentTemperature;
	private float maximumTemperature;
	
	private boolean peaked;
	
	private Runnable changeListener;
	private Runnable hitPeakListener;
	private Runnable leftPeakListener;
	
	public TemperatureStorage(int heatSinkCount)
	{
		setHeatSinkCount(heatSinkCount);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends TemperatureStorage> T setChangeListener(Runnable listener)
	{
		changeListener = listener;
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends TemperatureStorage> T setPeakListener(Runnable hitListener, Runnable leftListener)
	{
		hitPeakListener = hitListener;
		leftPeakListener = leftListener;
		return (T)this;
	}
	
	public TemperatureStorage setHeatSinkCount(int heatSinkCount)
	{
		maximumTemperature = BASE_TEMPERATURE_CAP + (heatSinkCount * BASE_TEMPERATURE_CAP) / 5;
		return this;
	}
	
	public void modifyHeat(float heat)
	{
		currentTemperature += heat;
		
		if(currentTemperature < 0)
			currentTemperature = 0;
		
		if(currentTemperature >= maximumTemperature)
		{
			currentTemperature = maximumTemperature;
			
			if(!peaked)
			{
				peaked = true;
				
				if(hitPeakListener != null)
					hitPeakListener.run();
			}
			
			return;
		}
		else if(currentTemperature < maximumTemperature - 10)
		{
			peaked = false;
			
			if(leftPeakListener != null)
				leftPeakListener.run();
		}
		
		if(changeListener != null)
			changeListener.run();
	}
	
	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setFloat("currentTemperature", currentTemperature);
		compound.setFloat("maximumTemperature", maximumTemperature);
	}
	
	public TemperatureStorage readFromNBT(NBTTagCompound compound)
	{
		currentTemperature = compound.getFloat("currentTemperature");
		maximumTemperature = compound.getFloat("maximumTemperature");
		
		return this;
	}
	
	public float getEfficiency()
	{
		if(currentTemperature < BASE_TEMPERATURE_CAP / 2)
		{
			float efficiency = currentTemperature / (BASE_TEMPERATURE_CAP / 2);
			return 0.5F + (efficiency / 2);
		}

		return currentTemperature / (BASE_TEMPERATURE_CAP / 2);
	}
	
	public float getCurrentTemperature()
	{
		return currentTemperature;
	}
	
	public float getMaximumTemperature()
	{
		return maximumTemperature;
	}
}
