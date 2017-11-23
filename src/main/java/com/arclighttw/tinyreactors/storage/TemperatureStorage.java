package com.arclighttw.tinyreactors.storage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

import com.arclighttw.tinyreactors.lib.nbt.INBTSerializable;

import net.minecraft.nbt.NBTTagCompound;

public class TemperatureStorage implements INBTSerializable
{
	protected BigDecimal temperature;
	protected BigDecimal limit;
	
	private Consumer<Double> valueListener;
	
	public TemperatureStorage()
	{
		this(0);
	}
	
	public TemperatureStorage(double limit)
	{
		this.temperature = createBigDecimal(0.0);
		this.limit = createBigDecimal(limit);
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
		compound.setDouble("temperature", temperature.doubleValue());
		compound.setDouble("limit", limit.doubleValue());
	}
	
	@Override
	public void deserialize(NBTTagCompound compound)
	{
		temperature = createBigDecimal(compound.getDouble("temperature"));
		limit = createBigDecimal(compound.getDouble("limit"));
		
		onValueChanged();
	}
	
	public double receiveHeat(double maxReceive, boolean simulate)
	{
		double received = Math.min(limit.doubleValue() - temperature.doubleValue(), maxReceive);
		
		if(!simulate)
		{
			temperature.add(new BigDecimal(received));
			onValueChanged();
		}
		
		return received;
	}

	public double extractHeat(double maxExtract, boolean simulate)
	{
		double extracted = Math.min(temperature.doubleValue(), maxExtract);
		
		if(!simulate)
		{
			temperature.subtract(new BigDecimal(extracted));
			onValueChanged();
		}
		
		return extracted;
	}

	public double getCurrentTemperature()
	{
		return temperature.doubleValue();
	}

	public double getMaxTemperature()
	{
		return limit.doubleValue();
	}
	
	public void setCurrentTemperature(double temperature)
	{
		this.temperature = createBigDecimal(temperature);
	}
	
	public void setMaxTemperature(double limit)
	{
		this.limit = createBigDecimal(limit);
	}
	
	private void onValueChanged()
	{
		if(valueListener != null)
			valueListener.accept(getCurrentTemperature());
	}
	
	private BigDecimal createBigDecimal(double value)
	{
		return new BigDecimal(value).setScale(2, RoundingMode.CEILING);
	}
}
