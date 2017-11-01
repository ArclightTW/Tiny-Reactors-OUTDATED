package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.properties.EnumVentTier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorVent extends TileEntitySync
{
	private EnumVentTier tier;
	
	private BlockPos controllerBlockPos;
	private TileEntityReactorController controller;
	
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
		
		if(controller != null)
			controller.getTemperature().modifyHeat(-tier.getHeatOffset());
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setInteger("tier", tier != null ? tier.ordinal() : 0);

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
		
		if(!compound.hasKey("Empty"))
		{
			int x = compound.getInteger("controllerX");
			int y = compound.getInteger("controllerX");
			int z = compound.getInteger("controllerX");
			
			controllerBlockPos = new BlockPos(x, y, z);
		}
	}
	
	public void setController(TileEntityReactorController controller)
	{
		this.controller = controller;
		controllerBlockPos = controller != null ? controller.getPos() : null;
	}
}
