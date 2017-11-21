package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.managers.ReactorManager;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorInputPort extends TileEntitySync
{
	private TileEntityReactorController controller;
	private BlockPos controllerPos;
	
	public TileEntityReactorInputPort()
	{
		super(9);
	}
	
	@Override
	public void onInitialLoad()
	{
		if(controller == null && controllerPos != null)
		{
			TileEntity tile = world.getTileEntity(controllerPos);
			
			if(tile != null && tile instanceof TileEntityReactorController)
				controller = (TileEntityReactorController)tile;
		}
	}
	
	@Override
	public void updateInternal()
	{
		if(controller == null)
		{
			onInitialLoad();
			return;
		}
		
		if(!controller.getMultiblock().hasSpace())
			return;
		
		for(int i = 0; i < getSizeInventory(); i++)
		{
			ItemStack stack = getStackInSlot(i);
			
			if(stack == ItemStack.EMPTY)
				continue;
			
			if(!ReactorManager.isReactant(stack))
				continue;
			
			controller.getMultiblock().insertReactant(world, Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
			
			stack.setCount(stack.getCount() - 1);
			
			if(stack.getCount() <= 0)
				stack = ItemStack.EMPTY;
			
			setInventorySlotContents(i, stack);
			return;
		}
	}
	
	@Override
	public void writeToNBTInternal(NBTTagCompound compound)
	{
		if(controller != null)
		{
			compound.setInteger("controllerX", controller.getPos().getX());
			compound.setInteger("controllerY", controller.getPos().getY());
			compound.setInteger("controllerZ", controller.getPos().getZ());
		}
		else
			compound.setString("Empty", "");
	}
	
	@Override
	public void readFromNBTInternal(NBTTagCompound compound)
	{
		if(!compound.hasKey("Empty"))
		{
			int x = compound.getInteger("controllerX");
			int y = compound.getInteger("controllerY");
			int z = compound.getInteger("controllerZ");
			controllerPos = new BlockPos(x, y, z);
		}
	}
	
	@Override
	public String getName()
	{
		return TRBlocks.REACTOR_INPUT_PORT.getLocalizedName();
	}
	
	public void setController(TileEntityReactorController controller)
	{
		this.controller = controller;
	}
}
