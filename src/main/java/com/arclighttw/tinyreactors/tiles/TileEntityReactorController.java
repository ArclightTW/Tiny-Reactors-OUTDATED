package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.helpers.NBTHelper;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.storage.MultiblockStructureReactor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorController extends TileEntity implements ITickable
{
	private final MultiblockStructureReactor structure;
	
	private boolean active;
	
	public TileEntityReactorController()
	{
		structure = new MultiblockStructureReactor(1000000, 2500);
		structure.setEnergyListener((energy) -> {
			// TODO: Do something when energy value changes.
		});
		structure.setValidityListener((valid) -> {
			// TODO: Valid/Invalid Reactor should perform some form of action.
		});
		
		structure.addAccessibleStructureBlock(TRBlocks.REACTOR_CASING);
		structure.addAccessibleStructureBlock(TRBlocks.REACTOR_CONTROLLER);
		structure.addAccessibleStructureBlock(TRBlocks.REACTOR_ENERGY_PORT);
		
		structure.addWallBlock(TRBlocks.REACTOR_GLASS);
		structure.addWallBlock(TRBlocks.REACTOR_HEAT_SINK);
		structure.addWallBlock(TRBlocks.REACTOR_INPUT_PORT);
		structure.addWallBlock(TRBlocks.REACTOR_WASTE_PORT);
		
		structure.addRoofBlock(TRBlocks.REACTOR_CASING);
		structure.addRoofInteriorBlock(TRBlocks.REACTOR_VENT);
		
		structure.addBaseBlock(TRBlocks.REACTOR_CASING);
		
		structure.addInternalBlock(Blocks.COAL_ORE);
	}
	
	@Override
	public void update()
	{
		if(!active)
			return;
		
		structure.getEnergy().receiveEnergy(structure.getAvailableYield(), false);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTHelper.writeToNBT(compound, "structure", structure);
		
		compound.setBoolean("active", active);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		NBTHelper.readFromNBT(compound, "structure", structure);
		
		setActive(compound.getBoolean("active"));

		ReactorManager.addReactor(pos, this);
	}
	
	public void setActive(boolean active)
	{
		if(!structure.isValid())
			active = false;
		
		this.active = active;
	}
	
	public boolean validateController(BlockPos added, IBlockState addedBlock, BlockPos removed)
	{
		return structure.validate(getWorld(), getPos(), added, addedBlock, removed);
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public MultiblockStructureReactor getStructure()
	{
		return structure;
	}
}
