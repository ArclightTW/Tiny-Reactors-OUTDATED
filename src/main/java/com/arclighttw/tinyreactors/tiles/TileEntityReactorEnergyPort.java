package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.helpers.NBTHelper;
import com.arclighttw.tinyreactors.storage.EnergyStorage;
import com.arclighttw.tinyreactors.storage.MultiblockStructureReactor;

import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityReactorEnergyPort extends TileEntity implements ITickable
{
	private MultiblockStructureReactor structure;
	private BlockPos controllerPos;
	
	private EnergyStorage energy;
	private EnumFacing facing;
	
	public TileEntityReactorEnergyPort()
	{
		energy = new EnergyStorage(256000, 256);
	}
	
	@Override
	public void update()
	{
		if(world == null)
			return;
		
		if(facing == null)
			facing = world.getBlockState(pos).getValue(BlockDirectional.FACING);
		
		if(facing != null)
		{
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			
			if(tile != null)
			{
				IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				
				if(energy != null)
				{
					int extracted = this.energy.extractEnergy(true);
					int received = energy.receiveEnergy(extracted, false);
					energy.extractEnergy(received, false);
				}
			}
		}
		
		if(structure == null && controllerPos != null)
		{
			TileEntity controller = world.getTileEntity(controllerPos);
			
			if(controller == null || !(controller instanceof TileEntityReactorController))
			{
				controllerPos = null;
				return;
			}
			
			structure = ((TileEntityReactorController)controller).getStructure();
		}
		
		if(structure == null)
			return;
		
		int extracted = structure.getEnergy().extractEnergy(true);
		int received = energy.receiveEnergy(extracted, false);
		structure.getEnergy().extractEnergy(received, false);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTHelper.writeToNBT(compound, "energy", energy);
		
		if(controllerPos != null)
			NBTHelper.Serializer.blockPos(compound, "controller", controllerPos);
		else
			compound.setString("controllerEmpty", "");
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		NBTHelper.readFromNBT(compound, "energy", energy);
		
		if(!compound.hasKey("controllerEmpty"))
			controllerPos = NBTHelper.Deserializer.blockPos(compound, "controller");
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return (T)energy;
			
		return super.getCapability(capability, facing);
	}
	
	public void onStructureValidityChanged(BlockPos origin, boolean valid)
	{
		if(!valid)
		{
			structure = null;
			controllerPos = null;
			return;
		}
		
		TileEntity tile = world.getTileEntity(origin);
		
		if(tile == null || !(tile instanceof TileEntityReactorController))
			return;
		
		TileEntityReactorController cont = (TileEntityReactorController)tile;
		structure = cont.getStructure();
		controllerPos = cont.getPos();
	}
	
	public EnergyStorage getEnergy()
	{
		return energy;
	}
}
