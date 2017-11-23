package com.arclighttw.tinyreactors.storage;

import java.util.function.Consumer;

import com.arclighttw.tinyreactors.lib.nbt.INBTSerializable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorage implements IEnergyStorage, INBTSerializable
{
	protected long energy;
	protected long capacity;
	
	protected long maxExtract;
	protected long maxReceive;
	
	protected long currentExtract;
	protected long currentReceive;
	
	private Consumer<Integer> valueListener;
	
	public EnergyStorage()
	{
		this(0);
	}
	
	public EnergyStorage(int capacity)
	{
		this(Long.valueOf((long)capacity));
	}
	
	public EnergyStorage(long capacity)
	{
		this(capacity, capacity);
	}
	
	public EnergyStorage(int capacity, int maxTransfer)
	{
		this(Long.valueOf((long)capacity), Long.valueOf((long)maxTransfer));
	}
	
	public EnergyStorage(long capacity, long maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer);
	}

	public EnergyStorage(int capacity, int maxExtract, int maxReceive)
	{
		this(Long.valueOf((long)capacity), Long.valueOf((long)maxExtract), Long.valueOf((long)maxReceive));
	}
	
	public EnergyStorage(long capacity, long maxExtract, long maxReceive)
	{
		this.capacity = capacity;
		this.maxExtract = currentExtract = maxExtract;
		this.maxReceive = currentReceive = maxReceive;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EnergyStorage> T setValueListener(Consumer<Integer> listener)
	{
		valueListener = listener;
		return (T)this;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		long received = Math.min(capacity - energy, Math.min(currentReceive, maxReceive));
		
		if(!simulate)
		{
			energy += received;
			onValueChanged();
		}
		
		return Integer.valueOf((int)received);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		long extracted = Math.min(energy, Math.min(currentExtract, maxExtract));
		
		if(!simulate)
		{
			energy -= extracted;
			onValueChanged();
		}
		
		return Integer.valueOf((int)extracted);
	}

	@Override
	public int getEnergyStored()
	{
		return Integer.valueOf((int)energy);
	}

	@Override
	public int getMaxEnergyStored()
	{
		return Integer.valueOf((int)capacity);
	}

	@Override
	public boolean canExtract()
	{
		return maxExtract > 0;
	}

	@Override
	public boolean canReceive()
	{
		return maxReceive > 0;
	}
	
	@Override
	public void serialize(NBTTagCompound compound)
	{
		compound.setLong("energy", energy);
		compound.setLong("capacity", capacity);
		
		compound.setLong("maxExtract", maxExtract);
		compound.setLong("maxReceive", maxReceive);
		
		compound.setLong("currentExtract", currentExtract);
		compound.setLong("currentReceive", currentReceive);
	}
	
	@Override
	public void deserialize(NBTTagCompound compound)
	{
		energy = compound.getLong("energy");
		capacity = compound.getLong("capacity");
		
		maxExtract = compound.getLong("maxExtract");
		maxReceive = compound.getLong("maxReceive");
		
		currentExtract = compound.getLong("currentExtract");
		currentReceive = compound.getLong("currentReceive");
		
		onValueChanged();
	}
	
	public int extractEnergy(boolean simulate)
	{
		return extractEnergy(Integer.valueOf((int)capacity), simulate);
	}
	
	public void setEnergyStored(int energy)
	{
		this.energy = Long.valueOf((long)energy);
	}
	
	public void setMaxEnergyStored(int capacity)
	{
		this.capacity = Long.valueOf((long)capacity);
	}
	
	private void onValueChanged()
	{
		if(valueListener != null)
			valueListener.accept(getEnergyStored());
	}
}
