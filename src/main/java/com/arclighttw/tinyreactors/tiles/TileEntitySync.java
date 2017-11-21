package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.inits.TRItems;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TileEntitySync extends TileEntity implements ITickable, IInventory
{
	private boolean syncScheduled;
	private boolean firstLoad;
	
	private ItemStack[] inventory;
	
	public TileEntitySync()
	{
		this(0);
	}
	
	public TileEntitySync(int size)
	{
		inventory = new ItemStack[size];
		clear();
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 3, getUpdateTag());
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public final void update()
	{
		if(world == null || world.isRemote)
			return;
		
		if(syncScheduled)
			actualSync();
		
		if(!firstLoad)
		{
			onInitialLoad();
			firstLoad = true;
			sync();
		}
		
		updateInternal();
	}
	
	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
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
		
		writeToNBTInternal(compound);
		return compound;
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		NBTTagList list = compound.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
		
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound item = list.getCompoundTagAt(i);
			int slot = item.getInteger("slot");
			
			if(slot >= 0 && slot < getSizeInventory())
				setInventorySlotContents(slot, new ItemStack(item));
		}
		
		readFromNBTInternal(compound);
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
	}
	
	@Override
	public void clear()
	{
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
		return "missing-inventory-name";
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
	
	public void sync()
	{
		syncScheduled = true;
	}
	
	public void onInitialLoad()
	{
	}
	
	public void writeToNBTInternal(NBTTagCompound compound)
	{
	}
	
	public void readFromNBTInternal(NBTTagCompound compound)
	{
	}
	
	public void updateInternal()
	{
	}
	
	private void actualSync()
	{
		syncScheduled = false;
//		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
//		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
//		world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
		markDirty();
	}
}
