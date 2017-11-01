package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.blocks.BlockReactorComponentDirectional;
import com.arclighttw.tinyreactors.properties.EnumEnergyPortTier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityReactorEnergyPort extends TileEntityEnergy
{
	private EnumEnergyPortTier tier;
	private EnumFacing facing;
	
	public TileEntityReactorEnergyPort()
	{
		this(EnumEnergyPortTier.I);
	}
	
	public TileEntityReactorEnergyPort(EnumEnergyPortTier tier)
	{
		this.tier = tier;
		energy = tier.getEnergyStorage();
	}
	
	@Override
	public void onInitialLoad()
	{
		facing = world.getBlockState(pos).getValue(BlockReactorComponentDirectional.FACING);
	}
	
	@Override
	public void update()
	{
		if(getEnergyStored() <= 0 || world == null || world.isRemote)
			return;
		
		if(facing == null)
			onInitialLoad();
		
		((WorldServer)world).addScheduledTask(() -> {
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			
			if(tile != null)
			{
				IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				
				if(storage != null)
				{
					int extracted = extractEnergy( energy.getCurrentExtract(), true);
					
					if(extracted > 0)
					{
						int received = storage.receiveEnergy(extracted, false);
						extractEnergy(received, false);
					}
				}
			}
		});
		
		sync();
	}
	
	@Override
	public boolean canExtract()
	{
		return false;
	}
	
	@Override
	public boolean canReceive()
	{
		return true;
	}
	
	@Override
	public void writeToNBTInternal(NBTTagCompound compound)
	{
		compound.setInteger("tier", tier.ordinal());
	}
	
	@Override
	public void readFromNBTInternal(NBTTagCompound compound)
	{
		tier = EnumEnergyPortTier.values()[compound.getInteger("tier")];
	}
}
