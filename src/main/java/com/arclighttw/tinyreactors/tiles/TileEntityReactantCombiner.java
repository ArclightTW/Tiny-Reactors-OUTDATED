package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.inits.TRItems;
import com.arclighttw.tinyreactors.storage.EnergyStorageRF;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntityReactantCombiner extends TileEntityEnergy implements IInventory
{
	private ItemStack[] inventory;
	
	private boolean processing;
	private boolean processed;
	private boolean calculated;
	
	private int timer;
	
	public TileEntityReactantCombiner()
	{
		super(new EnergyStorageRF(10000));
		clear();
	}
	
	public void resetProcess()
	{
		processing = false;
		processed = false;
		calculated = false;
		timer = 0;
		sync();
	}
	
	public void startProcessing()
	{
		processing = true;
	}
	
	public boolean isProcessing()
	{
		return processing;
	}
	
	public boolean hasProcessed()
	{
		return processed;
	}
	
	public int getProcessTick()
	{
		return timer;
	}
	
	public int getProcessLength()
	{
		return 100;
	}
	
	@Override
	public void updateInternal()
	{
		if(!processing)
		{
			if(!calculated && !processed)
			{
				ItemStack reactant = getStackInSlot(0);
				ItemStack ingot = getStackInSlot(1);
				
				if(reactant.isEmpty() || ingot.isEmpty() || !reactant.hasTagCompound())
				{
					setInventorySlotContents(2, ItemStack.EMPTY);
					calculated = true;
					return;
				}
				
				float quality = reactant.getTagCompound().getFloat("quality");
				float newQuality = quality + 20F;
				
				ItemStack result = reactant.copy();
				result.setCount(1);
				
				if(newQuality < 100)
					result.getTagCompound().setFloat("quality", newQuality);
				else
				{
					String registryName = reactant.getTagCompound().getString("registryName");
					Block block = Block.REGISTRY.getObject(new ResourceLocation(registryName));
					result = new ItemStack(block);
				}
				
				setInventorySlotContents(2, result);
				calculated = true;
			}
			
			return;
		}
		
		if(!world.isRemote)
		{
			if(getEnergyStored() <= 10)
			{
				resetProcess();
				return;
			}
			
			extractEnergy(10, false);
			timer++;
			sync();
			
			if(timer >= getProcessLength() && processing)
			{
				processing = false;
				processed = true;
				
				getStackInSlot(0).setCount(getStackInSlot(0).getCount() - 1);
				getStackInSlot(1).setCount(getStackInSlot(1).getCount() - 1);
				
				calculated = true;
				sync();
			}
		}
	}
	
	@Override
	public void writeAdditionalNBT(NBTTagCompound compound)
	{
		compound.setInteger("timer", timer);
		compound.setBoolean("processing", processing);
		compound.setBoolean("processed", processed);
		compound.setBoolean("calculated", calculated);
		
		NBTTagList list = new NBTTagList();
		
		for(int i = 0; i < inventory.length; i++)
		{
			ItemStack itemstack = getStackInSlot(i);
			
			if(itemstack == ItemStack.EMPTY)
				continue;
			
			NBTTagCompound item = new NBTTagCompound();
			item.setInteger("slot", i);
			itemstack.writeToNBT(item);
			
			list.appendTag(item);
		}
		
		compound.setTag("inventory", list);
	}
	
	@Override
	public void readAdditionalNBT(NBTTagCompound compound)
	{
		timer = compound.getInteger("timer");
		processing = compound.getBoolean("processing");
		processed = compound.getBoolean("processed");
		calculated = compound.getBoolean("calculated");
		
		NBTTagList list = compound.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
		
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound item = list.getCompoundTagAt(i);
			int slot = item.getInteger("slot");
			
			if(slot >= 0 && slot < getSizeInventory())
				setInventorySlotContents(slot, new ItemStack(item));
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= getSizeInventory() ? ItemStack.EMPTY : inventory[index];
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack stack = getStackInSlot(index);
		
		if(stack != ItemStack.EMPTY && stack.getCount() > count)
			return stack.splitStack(count);

		setInventorySlotContents(index, ItemStack.EMPTY);
		return stack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if(index >= getSizeInventory())
			return ItemStack.EMPTY;
		
		ItemStack stack = getStackInSlot(index);
		setInventorySlotContents(index, ItemStack.EMPTY);
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if(index >= getSizeInventory())
			return;
		
		inventory[index] = stack;
		markDirty();
		
		if(index < 2)
			calculated = false;
	}
	
	@Override
	public void clear()
	{
		inventory = new ItemStack[3];
		
		for(int i = 0; i < getSizeInventory(); i++)
			inventory[i] = ItemStack.EMPTY;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if(index == 0)
			return stack != null && Block.getBlockFromItem(stack.getItem()) == TRBlocks.DEGRADED_REACTANT;
		
		if(index == 1)
			return stack != null && stack.getItem() == TRItems.RADIOACTIVE_INGOT;
		
		return false;
	}
	
	@Override
	public boolean isEmpty()
	{
		for(int i = 0; i < getSizeInventory(); i++)
			if(!getStackInSlot(i).isEmpty())
				return false;
		
		return true;
	}
		
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName());
	}
	
	@Override
	public String getName()
	{
		return TRBlocks.REACTANT_COMBINER.getLocalizedName();
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)new InvWrapper(this);
		
		return super.getCapability(capability, facing);
	}
}
