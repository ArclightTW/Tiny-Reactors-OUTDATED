package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.config.TRConfig;
import com.arclighttw.tinyreactors.inits.TRSounds;
import com.arclighttw.tinyreactors.properties.EnumControllerTier;
import com.arclighttw.tinyreactors.properties.EnumRedstoneMode;
import com.arclighttw.tinyreactors.storage.ReactorMultiBlockStorage;
import com.arclighttw.tinyreactors.storage.TemperatureStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

public class TileEntityReactorController extends TileEntityEnergy
{
	private EnumControllerTier tier;
	private EnumRedstoneMode redstoneMode;
	private int previousRedstone;
	
	private ReactorMultiBlockStorage multiblock;
	private TemperatureStorage temperature;
	
	private boolean active;
	private boolean warming;
	private boolean meltdownInitiated;

	private int meltdownTimer;
	private int soundTimer;
	private int degradationTimer;
	
	private static final float TEMP_GAIN = 0.05F;
	
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
				return;
			
			temperature.setHeatSinkCount(multiblock.getHeatSinkCount());
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
		
		if(world == null)
			return;
		
		if(multiblock.shouldRefresh())
		{
			multiblock.checkValidity(world, pos);
			multiblock.markRefreshed();
		}
		
		if(temperature.hasOverheated())
		{
			temperature.applyCooldownTick();
			return;
		}
		
		if(getEnergyStored() > 0)
		{
			int average = (int)(getEnergyStored() / (float)multiblock.getEnergyPorts().size());
			
			for(TileEntityReactorEnergyPort energyPort : multiblock.getEnergyPorts())
			{
				int extracted = energyPort.receiveEnergy(average, false);
				extractEnergy(extracted, false);
			}			
		}
		
		if(isWarming() || isActive())
		{
			boolean disabled = false;
			
			if(redstoneMode != EnumRedstoneMode.IGNORE)
			{
				if(isPowered() && redstoneMode == EnumRedstoneMode.DISABLE_ON_REDSTONE)
					disabled = true;
				
				if(!isPowered() && redstoneMode == EnumRedstoneMode.ENABLE_ON_REDSTONE)
					disabled = true;
			}
			
			soundTimer++;
			if(soundTimer >= 36)
			{
				world.playSound(null, pos, isWarming() ? TRSounds.REACTOR_WARMING : TRSounds.REACTOR_ACTIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				soundTimer = 0;
			}
			
			if(isWarming())
			{
				temperature.modifyHeat(disabled ? 0 : TEMP_GAIN * multiblock.getReactorSize() * 4);
				sync();
			}
			else
			{
				energy.receiveEnergy(multiblock.getAvailableYield(), disabled);
				temperature.modifyHeat(disabled ? 0 : TEMP_GAIN * multiblock.getReactorSize());
				sync();
				
				if(TRConfig.REACTANT_DEGRADATION)
				{
					degradationTimer++;
					
					if(degradationTimer >= TRConfig.REACTANT_DEGRADATION_TICK)
					{
						multiblock.degradeReactant(world, temperature.getCurrentTemperature() / (TemperatureStorage.BASE_TEMPERATURE_CAP * 5));
						degradationTimer = 0;
					}
				}
			}
		}
		else
		{
			temperature.modifyHeat(-TEMP_GAIN * multiblock.getReactorSize());
			soundTimer = 0;
		}
		
		if(meltdownInitiated)
		{
			if(meltdownTimer % 40 == 0)
				world.playSound(null, pos, TRSounds.REACTOR_KLAXON, SoundCategory.BLOCKS, 2.0F, 1.0F);
			
			meltdownTimer--;
			
			if(meltdownTimer <= 0)
			{
				world.playSound(null, pos, TRSounds.REACTOR_OVERHEAT, SoundCategory.BLOCKS, 1.0F, 1.0F);
				temperature.overheat();
				meltdownInitiated = false;
			}
		}

		int redstone = world.getBlockState(pos).getWeakPower(world, pos, EnumFacing.NORTH);
		
		if(redstone != previousRedstone)
		{
			previousRedstone = redstone;
			sync();
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
		compound.setBoolean("warming", warming);
	}
	
	@Override
	public void readFromNBTInternal(NBTTagCompound compound)
	{
		multiblock.readFromNBT(compound);
		temperature.readFromNBT(compound);
		
		tier = EnumControllerTier.values()[compound.getInteger("tier")];
		redstoneMode = EnumRedstoneMode.values()[compound.getInteger("redstoneMode")];
		
		setActive(compound.getBoolean("active"));
		setWarming(compound.getBoolean("warming"));
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
		if(warming)
			return;
		
		this.active = active;
		soundTimer = 0;
		sync();
		
		if(active)
		{
			multiblock.onActivate(world);
			
			if(world != null)
				world.playSound(null, pos, TRSounds.REACTOR_ACTIVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	public boolean isActive()
	{
		return multiblock.isValid() && active;
	}
	
	public void setWarming(boolean warming)
	{
		this.warming = warming;
		soundTimer = 0;
		sync();
		
		if(warming && world != null)
			world.playSound(null, pos, TRSounds.REACTOR_WARMING, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
	
	public boolean isWarming()
	{
		return multiblock.isValid() && warming;
	}
	
	public void toggleRedstone(EnumRedstoneMode redstoneMode)
	{
		if(tier != EnumControllerTier.II)
			redstoneMode = EnumRedstoneMode.IGNORE;
		
		this.redstoneMode = redstoneMode;
		sync();
	}
}
