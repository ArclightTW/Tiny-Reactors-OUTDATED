package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.blocks.BlockReactorComponentDirectional;
import com.arclighttw.tinyreactors.inits.TRItems;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityReactorWastePort extends TileEntitySync
{
	public static final float REQUIRED_VOLUME = 200F;
	
	private float volume;
	
	@Override
	public void update()
	{
		super.update();
		
		if(volume < REQUIRED_VOLUME)
			return;
		
		if(!world.isRemote)
		{
			ItemStack itemstack = new ItemStack(TRItems.RADIOACTIVE_INGOT);
			EnumFacing facing = world.getBlockState(pos).getValue(BlockReactorComponentDirectional.FACING);
			
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			
			if(tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
			{
				IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY	, facing.getOpposite());
				ItemStack returned = itemstack;
				
				int currentSlot = 0;
				int maxSlots = inventory.getSlots();
				
				while(!returned.isEmpty() && returned.getCount() > 0 && currentSlot < maxSlots)
					returned = inventory.insertItem(currentSlot++, itemstack, false);
				
				itemstack = returned;
			}
			
			if(!itemstack.isEmpty() && itemstack.getCount() > 0)
			{
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
				item.motionX = facing.getFrontOffsetX();
				item.motionZ = facing.getFrontOffsetZ();
				
				world.spawnEntity(item);
			}
		}
		
		volume = 0;
	}
	
	public void generate(float amount)
	{
		volume += amount;
		sync();
	}
	
	public float getVolume()
	{
		return volume;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setFloat("volume", volume);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		volume = compound.getFloat("volume");
	}
}