package com.arclighttw.tinyreactors.storage;

import java.util.function.Consumer;

import com.arclighttw.tinyreactors.lib.nbt.INBTSerializable;

import net.minecraft.nbt.NBTTagCompound;

public class TemperatureStorage implements INBTSerializable
{
	protected double temperature;
	protected double limit;
	
	private Consumer<Double> valueListener;
	
	public TemperatureStorage()
	{
		this(0);
	}
	
	public TemperatureStorage(double limit)
	{
		this.temperature = 0.0;
		this.limit = limit;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends TemperatureStorage> T setValueListener(Consumer<Double> listener)
	{
		valueListener = listener;
		return (T)this;
	}

	@Override
	public void serialize(NBTTagCompound compound)
	{
		compound.setDouble("temperature", temperature);
		compound.setDouble("limit", limit);
	}
	
	@Override
	public void deserialize(NBTTagCompound compound)
	{
		temperature = compound.getDouble("temperature");
		limit = compound.getDouble("limit");
		
		onValueChanged();
	}
	
	public double getMultiplier()
	{
		double midTemp = limit / 2;
		
		if(temperature < midTemp)
		{
			double rate = temperature / midTemp;
			return 0.5 + (rate / 2.0);
		}
		else
		{
			double rate = temperature / limit;
			return 0.5 + rate;
		}
	}
	
	public double receiveHeat(double maxReceive, boolean simulate)
	{
		double received = Math.min(limit - temperature, maxReceive);

		if(!simulate)
		{
			temperature += received;
			onValueChanged();
		}
		
		return received;
	}

	public double extractHeat(double maxExtract, boolean simulate)
	{
		double extracted = Math.min(temperature, maxExtract);
		
		if(!simulate)
		{
			temperature -= extracted;
			onValueChanged();
		}
		
		return extracted;
	}

	public double getCurrentTemperature()
	{
		return temperature;
	}

	public double getMaxTemperature()
	{
		return limit;
	}
	
	public void setCurrentTemperature(double temperature)
	{
		this.temperature = temperature;
	}
	
	public void setMaxTemperature(double limit)
	{
		this.limit = limit;
	}
	
	private void onValueChanged()
	{
		if(valueListener != null)
			valueListener.accept(getCurrentTemperature());
	}
}
