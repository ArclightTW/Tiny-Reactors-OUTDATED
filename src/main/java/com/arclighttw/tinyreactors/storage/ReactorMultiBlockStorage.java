package com.arclighttw.tinyreactors.storage;

import java.util.List;

import com.arclighttw.tinyreactors.blocks.BlockReactorController;
import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;
import com.google.common.collect.Lists;

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
	
	List<TileEntityReactorEnergyPort> energyPorts;
	
	boolean isReactor, hasController;
	
	public ReactorMultiBlockStorage(TileEntityReactorController controller)
	{
		this.controller = controller;
		
		energyPorts = Lists.newArrayList();
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
	public boolean isValidInternalBlock(IBlockState state)
	{
		return ReactorManager.isReactant(state);
	}
	
	@Override
	public void onBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		if(state.getBlock() instanceof BlockReactorController)
			hasController = true;
		
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile != null && tile instanceof TileEntityReactorEnergyPort)
			energyPorts.add((TileEntityReactorEnergyPort)tile);
	}
	
	@Override
	public void onInternalBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		maximumYield += ReactorManager.getMaximumReactantRate();
		
		if(state.getBlock() != Blocks.AIR)
			availableYield += ReactorManager.getReactantRate(state);
	}
	
	@Override
	public void onPreCalculation()
	{
		isReactor = true;
		hasController = false;
		
		availableYield = 0;
		maximumYield = 0;
	
		energyPorts = Lists.newArrayList();
	}
	
	@Override
	public void onPostCalculation()
	{
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
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		availableYield = compound.getInteger("availableYield");
		maximumYield = compound.getInteger("maximumYield");
	}
	
	public List<TileEntityReactorEnergyPort> getEnergyPorts()
	{
		return energyPorts;
	}
	
	public int getAvailableYield()
	{
		return availableYield;
	}
}
