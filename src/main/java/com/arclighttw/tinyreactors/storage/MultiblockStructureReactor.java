package com.arclighttw.tinyreactors.storage;

import java.util.List;
import java.util.Map;

import com.arclighttw.tinyreactors.helpers.NBTHelper;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.lib.reactor.IReactorComponent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockStructureReactor extends MultiblockStructure
{
	private List<BlockPos> controllers;
	
	private Map<BlockPos, IReactorComponent> energyPorts;
	private Map<BlockPos, IReactorComponent> inputPorts;
	private Map<BlockPos, IReactorComponent> wastePorts;
	
	private Map<BlockPos, IReactorComponent> vents;
	
	private EnergyStorage energy;
	private TemperatureStorage temperature;
	
	private int availableYield;
	private double heatProduced;
	
	public MultiblockStructureReactor(int energyCapacity, int temperatureLimit)
	{
		energy = new EnergyStorage(energyCapacity);
		temperature = new TemperatureStorage(temperatureLimit);
	}
	
	@Override
	public void onPreCalculation(World world)
	{
		controllers = Lists.newArrayList();
		
		energyPorts = Maps.newConcurrentMap();
		inputPorts = Maps.newConcurrentMap();
		wastePorts = Maps.newConcurrentMap();
		
		vents = Maps.newConcurrentMap();
		
		availableYield = 0;
		heatProduced = 0;
	}
	
	@Override
	public boolean onValidityChanged(World world, BlockPos origin, boolean valid)
	{
		if(valid)
		{
			if(controllers.size() > 0)
				valid = false;
			
			if(energyPorts.size() == 0)
				valid = false;
		}
		
		if(energyPorts != null)
			for(Map.Entry<BlockPos, IReactorComponent> energyPort : energyPorts.entrySet())
				energyPort.getValue().onStructureValidityChanged(world, energyPort.getKey(), origin, valid);
		
		if(inputPorts != null)
			for(Map.Entry<BlockPos, IReactorComponent> inputPort : inputPorts.entrySet())
				inputPort.getValue().onStructureValidityChanged(world, inputPort.getKey(), origin, valid);
		
		if(wastePorts != null)
			for(Map.Entry<BlockPos, IReactorComponent> wastePort : wastePorts.entrySet())
				wastePort.getValue().onStructureValidityChanged(world, wastePort.getKey(), origin, valid);
		
		if(vents != null)
			for(Map.Entry<BlockPos, IReactorComponent> vent : vents.entrySet())
				vent.getValue().onStructureValidityChanged(world, vent.getKey(), origin, valid);
		
		return valid;
	}
	
	@Override
	public void onBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		Block block = state.getBlock();
		
		if(block == TRBlocks.REACTOR_ENERGY_PORT && block instanceof IReactorComponent)
			energyPorts.put(pos, (IReactorComponent)block);
		
		if(block == TRBlocks.REACTOR_INPUT_PORT && block instanceof IReactorComponent)
			inputPorts.put(pos, (IReactorComponent)block);
		
		if(block == TRBlocks.REACTOR_WASTE_PORT && block instanceof IReactorComponent)
			wastePorts.put(pos, (IReactorComponent)block);
		
		if(block == TRBlocks.REACTOR_VENT && block instanceof IReactorComponent)
			vents.put(pos, (IReactorComponent)block);
	}
	
	@Override
	public void onInternalBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		// TODO: 1x Vent offsets 1x Internal Block
		heatProduced += 0.01;
		
		if(state.getBlock() == Blocks.AIR)
			return;
		
		// TODO: Set proper yield values (this is so wrong).
		availableYield += 64;
	}
	
	@Override
	public void serialize(NBTTagCompound compound)
	{
		super.serialize(compound);
		NBTHelper.writeToNBT(compound, "energy", energy);
		NBTHelper.writeToNBT(compound, "temperature", temperature);
		
		compound.setInteger("availableYield", availableYield);
		compound.setDouble("heatProduced", heatProduced);
	}
	
	@Override
	public void deserialize(NBTTagCompound compound)
	{
		super.deserialize(compound);
		NBTHelper.readFromNBT(compound, "energy", energy);
		NBTHelper.readFromNBT(compound, "temperature", temperature);
		
		availableYield = compound.getInteger("availableYield");
		heatProduced = compound.getDouble("heatProduced");
	}
	
	public EnergyStorage getEnergy()
	{
		return energy;
	}
	
	public TemperatureStorage getTemperature()
	{
		return temperature;
	}
	
	public void setAvailableYield(int availableYield)
	{
		this.availableYield = availableYield;
	}
	
	public int getAvailableYield()
	{
		return availableYield;
	}
	
	public int getEnergyProduced()
	{
		return (int)(availableYield * temperature.getMultiplier());
	}
	
	public void setHeatProduced(double temperatureYield)
	{
		this.heatProduced = temperatureYield;
	}
	
	public double getHeatProduced()
	{
		return heatProduced;
	}
}
