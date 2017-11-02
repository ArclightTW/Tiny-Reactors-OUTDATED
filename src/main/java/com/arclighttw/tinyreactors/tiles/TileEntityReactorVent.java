package com.arclighttw.tinyreactors.tiles;

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
	
	private BlockPos controllerBlockPos;
	private TileEntityReactorController controller;
	
	private boolean operational;
	private int cooldown;
	
	public TileEntityReactorVent()
	{
	}
	
	@Override
	public void onInitialLoad()
	{
		tier = world.getBlockState(pos).getValue(EnumVentTier.PROPERTY);
		
		if(controllerBlockPos != null && controller == null)
		{
			TileEntity tile = world.getTileEntity(controllerBlockPos);
			
			if(tile != null && tile instanceof TileEntityReactorController)
				controller = (TileEntityReactorController)tile;
			else
				controllerBlockPos = null;
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(cooldown > 0)
			cooldown--;
		
		if(controller == null || !operational)
			return;
		
		if(controller.getMultiblock().isValid())
			controller.getTemperature().modifyHeat(-tier.getHeatOffset());
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setInteger("tier", tier != null ? tier.ordinal() : 0);
		compound.setBoolean("operational", operational);

		if(controllerBlockPos != null)
		{
			compound.setInteger("controllerX", controllerBlockPos.getX());
			compound.setInteger("controllerY", controllerBlockPos.getY());
			compound.setInteger("controllerZ", controllerBlockPos.getZ());
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
			int y = compound.getInteger("controllerX");
			int z = compound.getInteger("controllerX");
			
			controllerBlockPos = new BlockPos(x, y, z);
		}
	}
	
	public void toggleState()
	{
		if(cooldown > 0)
			return;
		
		cooldown = 5;
		operational = !operational;
		sync();
	}
	
	public void setController(TileEntityReactorController controller)
	{
		this.controller = controller;
		controllerBlockPos = controller != null ? controller.getPos() : null;
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
		boolean canRun = operational && (controller != null && controller.isActive());
		if(!canRun) return false;
		return !isObstructed();		
	}
	
	public EnumVentTier getTier()
	{
		return tier;
	}
}
