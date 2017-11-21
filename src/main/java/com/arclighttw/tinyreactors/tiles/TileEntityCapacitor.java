package com.arclighttw.tinyreactors.tiles;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityCapacitor extends TileEntityEnergy
{
	private List<IEnergyStorage> receivers;
	
	@Override
	public void onInitialLoad()
	{
		calculateNeighbors();
	}
	
	@Override
	public void updateInternal()
	{
		if(world == null || world.isRemote || getEnergyStored() <= 0)
			return;
		
		if(receivers == null || receivers.size() == 0)
		{
			onInitialLoad();
			return;
		}
		
		for(IEnergyStorage receiver : receivers)
		{
			int extracted = extractEnergy(energy.getCurrentExtract() / receivers.size(), true);
			int received = receiver.receiveEnergy(extracted, false);
			extractEnergy(received, false);
		}
	}
	
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
	
	public void calculateNeighbors()
	{
		receivers = Lists.newArrayList();
		
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			
			if(tile == null || tile instanceof TileEntityReactorEnergyPort || tile instanceof TileEntityCapacitor)
				continue;
			
			IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
			
			if(storage == null)
				continue;
			
			receivers.add(storage);
		}
	}
}
