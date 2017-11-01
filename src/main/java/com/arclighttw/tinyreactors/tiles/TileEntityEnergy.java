package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.storage.EnergyStorageRF;

import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxProvider;
import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
	@Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyProvider", modid = "redstoneflux"),
	@Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyReceiver", modid = "redstoneflux"),
	@Optional.Interface(iface = "blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxProvider", modid = "immersiveengineering"),
	@Optional.Interface(iface = "blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver", modid = "immersiveengineering")
})
public class TileEntityEnergy extends TileEntitySync implements IEnergyStorage, IEnergyReceiver, IEnergyProvider, IFluxReceiver, IFluxProvider
{	
	protected EnergyStorageRF energy;
	
	public TileEntityEnergy()
	{
		this(new EnergyStorageRF(0));
	}
	
	public TileEntityEnergy(EnergyStorageRF energy)
	{
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
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
	{
		return receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		int extract = energy.extractEnergy(maxExtract, simulate);
		sync();
		return extract;
	}
	
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
	{
		return extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int getEnergyStored()
	{
		return energy.getEnergyStored();
	}
	
	@Override
	public int getEnergyStored(EnumFacing facing)
	{
		return getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return energy.getMaxEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored(EnumFacing facing)
	{
		return getMaxEnergyStored();
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
	public boolean canConnectEnergy(EnumFacing from)
	{
		return true;
	}
	
	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		energy.writeToNBT(compound);
		writeToNBTInternal(compound);
		
		return compound;
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		energy.readFromNBT(compound);
		readFromNBTInternal(compound);
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
	
	public void writeToNBTInternal(NBTTagCompound compound)
	{
	}
	
	public void readFromNBTInternal(NBTTagCompound compound)
	{
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
}
