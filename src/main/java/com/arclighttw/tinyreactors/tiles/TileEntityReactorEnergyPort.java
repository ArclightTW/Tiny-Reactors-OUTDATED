package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.blocks.BlockReactorComponentDirectional;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityReactorEnergyPort extends TileEntityEnergy
{
	private EnumFacing facing;
	
	@Override
	public void onInitialLoad()
	{
		facing = world.getBlockState(pos).getValue(BlockReactorComponentDirectional.FACING);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(world == null || world.isRemote || getEnergyStored() <= 0)
			return;
		
		if(facing == null)
			onInitialLoad();
		
		TileEntity tile = world.getTileEntity(pos.offset(facing));
		
		if(tile == null)
			return;
		
		IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
		
		if(storage == null)
			return;
		
		int extracted = extractEnergy(energy.getCurrentExtract(), true);
		
		if(extracted <= 0)
			return;
		
		int received = storage.receiveEnergy(extracted, false);
		extractEnergy(received, false);
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
}
