package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.properties.EnumVentState;
import com.arclighttw.tinyreactors.properties.EnumVentTier;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

public class TileEntityReactorVent extends TileEntitySync
{
	private EnumVentTier tier;
	
	private TileEntityReactorController controller;
	private BlockPos controllerPos;
	
	private boolean operational;
	
	public TileEntityReactorVent()
	{
	}
	
	@Override
	public void onInitialLoad()
	{
		tier = world.getBlockState(pos).getValue(EnumVentTier.PROPERTY);
		
		if(controller == null && controllerPos != null)
		{
			TileEntity tile = world.getTileEntity(controllerPos);
			
			if(tile != null && tile instanceof TileEntityReactorController)
				controller = (TileEntityReactorController)tile;
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(controller == null)
		{
			onInitialLoad();
			return;
		}
		
		if(!world.isRemote)
		{
			operational = world.getBlockState(pos).getValue(EnumVentState.PROPERTY) == EnumVentState.OPEN;
			
			if(!isOperational())
				return;
			
			if(controller.getMultiblock().isValid())
				controller.getTemperature().modifyHeat(-tier.getHeatOffset());
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setInteger("tier", tier != null ? tier.ordinal() : 0);
		compound.setBoolean("operational", operational);
		
		if(controller != null)
		{
			compound.setInteger("controllerX", controller.getPos().getX());
			compound.setInteger("controllerY", controller.getPos().getY());
			compound.setInteger("controllerZ", controller.getPos().getZ());
		}
		else
			compound.setString("Empty", "");

		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		tier = EnumVentTier.values()[compound.getInteger("tier")];
		operational = compound.getBoolean("operational");
		
		if(!compound.hasKey("Empty"))
		{
			int x = compound.getInteger("controllerX");
			int y = compound.getInteger("controllerY");
			int z = compound.getInteger("controllerZ");
			controllerPos = new BlockPos(x, y, z);
		}
	}
	
	public void setController(TileEntityReactorController controller)
	{
		this.controller = controller;
	}
	
	public boolean isObstructed()
	{
		Block above = world.getBlockState(pos.offset(EnumFacing.UP)).getBlock();
		
		if(above == Blocks.AIR || above == Blocks.WATER || above == Blocks.FLOWING_WATER)
			return false;
		
		if(!(above instanceof IFluidBlock))
			return true;
		
		Fluid fluid = ((IFluidBlock)above).getFluid();
		return fluid.getTemperature() > 300;
	}
	
	public boolean isOperational()
	{
		if(!operational)
			return false;
		
		return !isObstructed();		
	}
	
	public boolean isRunning()
	{
		return isOperational() && controller != null && controller.isActive();
	}
	
	public TileEntityReactorController getController()
	{
		return controller;
	}
	
	public EnumVentTier getTier()
	{
		return tier;
	}
}
