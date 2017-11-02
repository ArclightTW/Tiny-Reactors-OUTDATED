package com.arclighttw.tinyreactors.tiles;

import java.util.Map;

import com.arclighttw.tinyreactors.properties.EnumCapacitorTier;
import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityCapacitor extends TileEntityEnergy
{
	private EnumCapacitorTier tier;
	private Map<EnumFacing, IEnergyStorage> receivers;
	
	public TileEntityCapacitor()
	{
		this(EnumCapacitorTier.I);
	}
	
	public TileEntityCapacitor(EnumCapacitorTier tier)
	{
		this.tier = tier;
		energy = tier.getEnergyStorage();
	}
	
	@Override
	public void onInitialLoad()
	{
		calculateNeighbors();
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(getEnergyStored() <= 0 || world == null || world.isRemote)
			return;
		
		onInitialLoad();
		
		for(Map.Entry<EnumFacing, IEnergyStorage> receiver : receivers.entrySet())
		{
			int extracted = extractEnergy(energy.getCurrentExtract() / receivers.size(), true);
			
			if(extracted > 0)
			{
				int received = receiver.getValue().receiveEnergy(extracted, false);
				extractEnergy(received, false);
			}
		}
	}
	
	@Override
	public void writeToNBTInternal(NBTTagCompound compound)
	{
		compound.setInteger("tier", tier.ordinal());
	}
	
	@Override
	public void readFromNBTInternal(NBTTagCompound compound)
	{
		tier = EnumCapacitorTier.values()[compound.getInteger("tier")];
	}
	
	public void calculateNeighbors()
	{
		receivers = Maps.newHashMap();
		
		for(EnumFacing direction : EnumFacing.VALUES)
		{
			TileEntity tile = world.getTileEntity(pos.offset(direction));
			
			if(tile == null || tile instanceof TileEntityReactorEnergyPort)
				continue;
			
			IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite());
			
			if(storage != null)
				receivers.put(direction.getOpposite(), storage);
		}
	}
}
