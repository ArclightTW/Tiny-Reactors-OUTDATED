package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.storage.EnergyStorageRF;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergy extends TileEntitySync implements IEnergyStorage
{	
	protected EnergyStorageRF energy;
	
	public TileEntityEnergy()
	{
		this(0);
	}
	
	public TileEntityEnergy(int size)
	{
		this(size, new EnergyStorageRF(0));
	}
	
	public TileEntityEnergy(EnergyStorageRF energy)
	{
		this(0, energy);
	}
	
	public TileEntityEnergy(int size, EnergyStorageRF energy)
	{
		super(size);
		
		energy.setValidityListener(() -> {
			sync();
		});
		
		this.energy = energy;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		int receive = energy.receiveEnergy(maxReceive, simulate);
		sync();
		return receive;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		int extract = energy.extractEnergy(maxExtract, simulate);
		sync();
		return extract;
	}
	
	@Override
	public int getEnergyStored()
	{
		return energy.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return energy.getMaxEnergyStored();
	}
	
	@Override
	public boolean canReceive()
	{
		return energy.getMaxReceive() > 0;
	}
	
	@Override
	public boolean canExtract()
	{
		return energy.getMaxExtract() > 0;
	}
	
	@Override
	public final void writeToNBTInternal(NBTTagCompound compound)
	{
		energy.writeToNBT(compound);
		writeAdditionalNBT(compound);
	}
	
	@Override
	public final void readFromNBTInternal(NBTTagCompound compound)
	{
		energy.readFromNBT(compound);
		readAdditionalNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(!hasCapability(capability, facing))
			return super.getCapability(capability, facing);
		
		if(capability == CapabilityEnergy.ENERGY)
			return (T)energy;
		
		return super.getCapability(capability, facing);
	}
	
	public EnergyStorageRF getEnergyStorage()
	{
		return energy;
	}
	
	public boolean isPowered()
	{
		int maxPower = 0;
		
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			int power = world.getRedstonePower(pos.offset(facing), facing);
			
			if(power > maxPower)
				maxPower = power;
		}
		
		return maxPower > 0;
	}
	
	public void writeAdditionalNBT(NBTTagCompound compound)
	{
	}
	
	public void readAdditionalNBT(NBTTagCompound compound)
	{
	}
}
