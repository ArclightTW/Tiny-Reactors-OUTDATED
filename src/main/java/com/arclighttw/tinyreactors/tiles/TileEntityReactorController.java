package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.properties.EnumControllerTier;
import com.arclighttw.tinyreactors.properties.EnumRedstoneMode;
import com.arclighttw.tinyreactors.storage.ReactorMultiBlockStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;

public class TileEntityReactorController extends TileEntityEnergy
{
	private EnumControllerTier tier;
	private EnumRedstoneMode redstoneMode;
	private int previousRedstone;
	
	private ReactorMultiBlockStorage multiblock;
	private boolean active;
	
	public TileEntityReactorController()
	{
		this(EnumControllerTier.I);
	}
	
	public TileEntityReactorController(EnumControllerTier tier)
	{
		this.tier = tier;
		redstoneMode = EnumRedstoneMode.IGNORE;
		energy = tier.getEnergyStorage();
		
		multiblock = new ReactorMultiBlockStorage(this);
		multiblock.setValidityListener(() -> {
			if(!multiblock.isValid())
			{
				setActive(false);
				return;
			}
			
			sync();
		});
	}
	
	@Override
	public void onInitialLoad()
	{
		multiblock.checkValidity(world, pos);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(!world.isRemote)
		{
			((WorldServer)world).addScheduledTask(() -> {
				if(!multiblock.isValid())
					return;
				
				int redstone = world.getBlockState(pos).getWeakPower(world, pos, EnumFacing.NORTH);
				
				if(redstone != previousRedstone)
				{
					previousRedstone = redstone;
					sync();
				}
				
				if(tier == EnumControllerTier.I)
					redstoneMode = EnumRedstoneMode.IGNORE;
				
				if(redstoneMode != EnumRedstoneMode.IGNORE)
				{
					boolean powered = isPowered();
					
					if(powered && redstoneMode == EnumRedstoneMode.DISABLE_ON_REDSTONE)
						return;
					
					if(!powered && redstoneMode == EnumRedstoneMode.ENABLE_ON_REDSTONE)
						return;
				}
				
				if(isActive())
					energy.receiveEnergy(multiblock.getAvailableYield(), false);
				
				int average = (int)(getEnergyStored(EnumFacing.NORTH) / (float)multiblock.getEnergyPorts().size());
				
				for(TileEntityReactorEnergyPort energyPort : multiblock.getEnergyPorts())
				{
					int extracted = energyPort.receiveEnergy(EnumFacing.NORTH, average, false);
					extractEnergy(EnumFacing.NORTH, extracted, false);
				}
			});
		}
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from)
	{
		return false;
	}
	
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public void writeToNBTInternal(NBTTagCompound compound)
	{
		multiblock.writeToNBT(compound);
		compound.setInteger("tier", tier.ordinal());
		compound.setInteger("redstoneMode", redstoneMode.ordinal());
		
		compound.setBoolean("active", active);
	}
	
	@Override
	public void readFromNBTInternal(NBTTagCompound compound)
	{
		multiblock.readFromNBT(compound);
		tier = EnumControllerTier.values()[compound.getInteger("tier")];
		redstoneMode = EnumRedstoneMode.values()[compound.getInteger("redstoneMode")];
		
		setActive(compound.getBoolean("active"));
	}
	
	public ReactorMultiBlockStorage getMultiblock()
	{
		return multiblock;
	}
	
	public EnumControllerTier getTier()
	{
		return tier;
	}
	
	public EnumRedstoneMode getRedstoneMode()
	{
		return redstoneMode;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
		sync();
	}
	
	public boolean isActive()
	{
		return multiblock.isValid() && active;
	}
	
	public void toggleRedstone(EnumRedstoneMode redstoneMode)
	{
		if(tier != EnumControllerTier.II)
			redstoneMode = EnumRedstoneMode.IGNORE;
		
		this.redstoneMode = redstoneMode;
	}
}
