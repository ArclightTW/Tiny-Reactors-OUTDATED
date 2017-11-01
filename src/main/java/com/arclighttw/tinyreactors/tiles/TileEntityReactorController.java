package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.config.TRConfig;
import com.arclighttw.tinyreactors.inits.TRSounds;
import com.arclighttw.tinyreactors.properties.EnumControllerTier;
import com.arclighttw.tinyreactors.properties.EnumRedstoneMode;
import com.arclighttw.tinyreactors.storage.ReactorMultiBlockStorage;
import com.arclighttw.tinyreactors.storage.TemperatureStorage;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;

public class TileEntityReactorController extends TileEntityEnergy
{
	private EnumControllerTier tier;
	private EnumRedstoneMode redstoneMode;
	private int previousRedstone;
	
	private ReactorMultiBlockStorage multiblock;
	private TemperatureStorage temperature;
	
	private boolean active;
	
	private boolean meltdownInitiated;
	private int meltdownTimer;
	
	private int timer;
	
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
			temperature.setHeatSinkCount(multiblock.getHeatSinkCount());
			
			if(!multiblock.isValid())
			{
				setActive(false);
				return;
			}
			
			sync();
		});
		
		temperature = new TemperatureStorage(0);
		temperature.setChangeListener(() -> {
			sync();
		});
		temperature.setPeakListener(
			() -> {
				if(!TRConfig.REACTOR_MELTDOWN)
					return;
				
				meltdownInitiated = true;
				
				if(meltdownTimer == -1)
					meltdownTimer = 20 * TRConfig.REACTOR_MELTDOWN_DELAY;
		},
			() -> {
				meltdownInitiated = false;
				meltdownTimer = -1;
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
				if(meltdownInitiated)
				{
					if(meltdownTimer % 40 == 0)
						world.playSound(null, pos, TRSounds.REACTOR_KLAXON, SoundCategory.BLOCKS, 1.0F, 1.0F);
					
					meltdownTimer--;
					
					if(meltdownTimer <= 0)
					{
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						return;
					}
				}
				
				if(!multiblock.isValid())
				{
					temperature.modifyHeat(multiblock.getReactorSize() * -0.25F);
					return;
				}
				
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
				{
					energy.receiveEnergy((int)(multiblock.getAvailableYield() * temperature.getEfficiency()), false);
					temperature.modifyHeat(multiblock.getReactorSize() * 0.25F);
					
					timer++;
					
					if(timer <= 36)
					{
						world.playSound(null, pos, TRSounds.REACTOR_ACTIVE, SoundCategory.BLOCKS, 0.05F, 1.0F);
						timer = 0;
					}
				}
				else
				{
					temperature.modifyHeat(multiblock.getReactorSize() * -0.25F);
					timer = 0;
				}
				
				int average = (int)(getEnergyStored() / (float)multiblock.getEnergyPorts().size());
				
				for(TileEntityReactorEnergyPort energyPort : multiblock.getEnergyPorts())
				{
					int extracted = energyPort.receiveEnergy(average, false);
					extractEnergy(extracted, false);
				}
			});
		}
	}
	
	@Override
	public boolean canReceive()
	{
		return false;
	}
	
	@Override
	public boolean canExtract()
	{
		return false;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public void writeToNBTInternal(NBTTagCompound compound)
	{
		multiblock.writeToNBT(compound);
		temperature.writeToNBT(compound);
		
		compound.setInteger("tier", tier.ordinal());
		compound.setInteger("redstoneMode", redstoneMode.ordinal());
		
		compound.setBoolean("active", active);
	}
	
	@Override
	public void readFromNBTInternal(NBTTagCompound compound)
	{
		multiblock.readFromNBT(compound);
		temperature.readFromNBT(compound);
		
		tier = EnumControllerTier.values()[compound.getInteger("tier")];
		redstoneMode = EnumRedstoneMode.values()[compound.getInteger("redstoneMode")];
		
		setActive(compound.getBoolean("active"));
	}
	
	public ReactorMultiBlockStorage getMultiblock()
	{
		return multiblock;
	}
	
	public TemperatureStorage getTemperature()
	{
		return temperature;
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
		
		if(active)
			world.playSound(null, pos, TRSounds.REACTOR_ACTIVE, SoundCategory.BLOCKS, 0.05F, 1.0F);
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
