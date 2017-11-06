package com.arclighttw.tinyreactors.storage;

import java.util.List;

import com.arclighttw.tinyreactors.blocks.BlockReactorController;
import com.arclighttw.tinyreactors.config.TRConfig;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.tiles.TileEntityDegradedReactant;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorVent;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorWastePort;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReactorMultiBlockStorage extends MultiBlockStorage
{
	final TileEntityReactorController controller;
	
	int availableYield, maximumYield;
	int heatSinkCount, reactorSize, prevReactorSize;
	int degradedItem;
	
	List<TileEntityReactorEnergyPort> energyPorts;
	List<TileEntityReactorWastePort> wastePorts;
	List<TileEntityReactorVent> reactorVents;
	
	List<BlockPos> validReactants;
	
	boolean isReactor, hasController;
	
	public ReactorMultiBlockStorage(TileEntityReactorController controller)
	{
		this.controller = controller;
		onPreCalculation(null);
	}
	
	@Override
	public boolean isValidRoof(IBlockState state)
	{
		return ReactorManager.isValidRoof(state.getBlock());
	}
	
	@Override
	public boolean isValidCasing(IBlockState state)
	{
		return ReactorManager.isValidCasing(state.getBlock());
	}
	
	@Override
	public boolean isValidStructure(IBlockState state)
	{
		return ReactorManager.isValidStructure(state.getBlock());
	}
	
	@Override
	public boolean isValidInternalBlock(World world, BlockPos pos, IBlockState state)
	{
		return ReactorManager.isReactant(world, pos, state);
	}
	
	@Override
	public void onBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		if(state.getBlock() instanceof BlockReactorController)
			hasController = true;
		
		if(state.getBlock() == TRBlocks.REACTOR_HEAT_SINK)
			heatSinkCount++;
		
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile != null)
		{
			if(tile instanceof TileEntityReactorEnergyPort)
				energyPorts.add((TileEntityReactorEnergyPort)tile);
			
			if(tile instanceof TileEntityReactorWastePort)
				wastePorts.add((TileEntityReactorWastePort)tile);
			
			if(tile instanceof TileEntityReactorVent)
				reactorVents.add((TileEntityReactorVent)tile);
		}
	}
	
	@Override
	public void onInternalBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		reactorSize++;
		maximumYield += ReactorManager.getMaximumReactantRate();
		
		if(state.getBlock() != Blocks.AIR)
		{
			availableYield += ReactorManager.getReactantRate(world, pos, state);
			validReactants.add(pos);
		}
	}
	
	@Override
	public void onPreCalculation(World world)
	{
		isReactor = true;
		hasController = false;
		
		availableYield = 0;
		maximumYield = 0;
		
		heatSinkCount = 0;
		reactorSize = 0;
	
		energyPorts = Lists.newArrayList();
		wastePorts = Lists.newArrayList();
		
		reactorVents = Lists.newArrayList();
		validReactants = Lists.newArrayList();
	}
	
	@Override
	public void onPostCalculation(World world)
	{
		prevReactorSize = reactorSize;
		
		if(!hasController || energyPorts.size() == 0)
		{
			isReactor = false;
			availableYield = 0;
			maximumYield = 0;
		}
		
		setValid(isReactor);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setInteger("availableYield", availableYield);
		compound.setInteger("maximumYield", maximumYield);
		
		compound.setInteger("heatSinkCount", heatSinkCount);
		compound.setInteger("reactorSize", reactorSize);
		
		compound.setInteger("degradedItem", degradedItem);
		
		// TODO: SAVE ALL POSITIONS
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		availableYield = compound.getInteger("availableYield");
		maximumYield = compound.getInteger("maximumYield");
		
		heatSinkCount = compound.getInteger("heatSinkCount");
		reactorSize = compound.getInteger("reactorSize");
		
		degradedItem = compound.getInteger("degradedItem");
		
		// TODO: LOAD ALL POSITIONS
	}
	
	public void onActivate(World world)
	{
		if(!TRConfig.REACTANT_DEGRADATION)
			return;
		
		for(TileEntityReactorVent vent : reactorVents)
		{
			vent.setController(controller);
			vent.sync();
		}
	}
	
	public void degradeReactant(World world, float qualityDegration)
	{
		if(validReactants.size() == 0)
			return;
		
		BlockPos pos = null;
		
		try
		{
			pos = validReactants.get(degradedItem);
		}
		catch(IndexOutOfBoundsException e)
		{
			degradedItem = 0;
			pos = validReactants.get(degradedItem);
		}
		
		if(pos == null)
			return;
		
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null)
		{
			Block reactant = world.getBlockState(pos).getBlock();
			world.setBlockState(pos, TRBlocks.DEGRADED_REACTANT.getDefaultState());
			
			tile = world.getTileEntity(pos);
			
			if(tile == null || !(tile instanceof TileEntityDegradedReactant))
				return;
			
			TileEntityDegradedReactant degradedReactant = (TileEntityDegradedReactant)tile;
			degradedReactant.setRepresentedBlock(reactant);
		}
		
		if(tile != null && tile instanceof TileEntityDegradedReactant)
			((TileEntityDegradedReactant)tile).degrade(qualityDegration);
		
		degradedItem++;
	}
	
	public List<TileEntityReactorEnergyPort> getEnergyPorts()
	{
		return energyPorts;
	}
	
	public List<TileEntityReactorWastePort> getWastePorts()
	{
		return wastePorts;
	}
	
	public int getHeatSinkCount()
	{
		return heatSinkCount;
	}
	
	public int getAvailableYield()
	{
		return availableYield;
	}
	
	public int getReactorSize()
	{
		return prevReactorSize;
	}
}
