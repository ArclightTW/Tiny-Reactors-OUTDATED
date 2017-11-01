package com.arclighttw.tinyreactors.storage;

import com.arclighttw.tinyreactors.network.SMessageReactorEnergyPort.Mode;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageRF implements IEnergyStorage
{
	protected int energy;
	protected int capacity;
	
	protected int maxReceive;
	protected int maxExtract;
	
	protected int currentReceive;
	protected int currentExtract;
	
	private Runnable listener;
	
	public EnergyStorageRF(int capacity)
	{
		this(capacity, capacity, capacity);
	}
	
	public EnergyStorageRF(int capacity, int maxTransfer)
	{
		this(capacity, maxTransfer, maxTransfer);
	}
	
	public EnergyStorageRF(int capacity, int maxReceive, int maxExtract)
	{
		this.capacity = capacity;
		this.maxReceive = currentReceive = maxReceive;
		this.maxExtract = currentExtract = maxExtract;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EnergyStorageRF> T setValidityListener(Runnable listener)
	{
		this.listener = listener;
		return (T)this;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		int received = Math.min(capacity - energy, Math.min(currentReceive, maxReceive));
		
		if(!simulate)
		{
			energy += received;
			
			if(listener != null)
				listener.run();
		}
		
		return received;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		int extracted = Math.min(energy, Math.min(currentExtract, maxExtract));
		
		if(!simulate)
		{
			energy -= extracted;
			
			if(listener != null)
				listener.run();	
		}
		
		return extracted;
	}
	
	@Override
	public int getEnergyStored()
	{
		return energy;
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return capacity;
	}
	
	@Override
	public boolean canExtract()
	{
		return true;
	}
	 
	@Override
	public boolean canReceive()
	{
		return true;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Energy", energy);
		nbt.setInteger("Capacity", capacity);
		nbt.setInteger("MaxReceive", maxReceive);
		nbt.setInteger("MaxExtract", maxExtract);
		nbt.setInteger("CurrentReceive", currentReceive);
		nbt.setInteger("CurrentExtract", currentExtract);
		
		return nbt;
	}
	
	public EnergyStorageRF readFromNBT(NBTTagCompound nbt)
	{
		energy = nbt.getInteger("Energy");
		
		setCapacity(nbt.getInteger("Capacity"));
		setMaxReceive(nbt.getInteger("MaxReceive"));
		setMaxExtract(nbt.getInteger("MaxExtract"));
		
		currentReceive = nbt.getInteger("CurrentReceive");
		currentExtract = nbt.getInteger("CurrentExtract");
		
		return this;
	}
	
	public EnergyStorageRF setCapacity(int capacity)
	{
		this.capacity = capacity;
		
		if(energy > capacity)
			energy = capacity;
		
		return this;
	}
	
	public EnergyStorageRF setMaxTransfer(int maxTransfer)
	{
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}
	
	public EnergyStorageRF setMaxReceive(int maxReceive)
	{
		this.maxReceive = maxReceive;
		return this;
	}
	
	public EnergyStorageRF setMaxExtract(int maxExtract)
	{
		this.maxExtract = maxExtract;
		return this;
	}
	
	public int getMaxReceive()
	{
		return maxReceive;
	}
	
	public int getMaxExtract()
	{
		return maxExtract;
	}
	
	public int getCurrentReceive()
	{
		return currentReceive;
	}
	
	public int getCurrentExtract()
	{
		return currentExtract;
	}
	
	public void setEnergyStored(int energy)
	{
		if(energy > capacity)
			energy = capacity;
		else if(energy < 0)
			energy = 0;
		
		this.energy = energy;
		
		if(listener != null)
			listener.run();
	}
	
	public boolean modifyEnergyStored(int energy)
	{
		int toadd = Math.min(energy, capacity - this.energy);
		setEnergyStored(this.energy + toadd);
		
		return toadd == energy;
	}
	
	public void modify(Mode mode, int amount)
	{
		switch(mode)
		{
		case DecreaseInput:
			currentReceive -= amount;
			break;
		case IncreaseInput:
			currentReceive += amount;
			break;
		case DecreaseOutput:
			currentExtract -= amount;
			break;
		case IncreaseOutput:
			currentExtract += amount;
			break;
		}

		if(currentReceive < 0)
			currentReceive = 0;
		
		if(currentReceive > maxReceive)
			currentReceive = maxReceive;
		
		if(currentExtract < 0)
			currentExtract = 0;
		
		if(currentExtract > maxExtract)
			currentExtract = maxExtract;
	}
}
