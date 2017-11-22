package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.storage.InventoryStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntitySync extends TileEntity implements ITickable
{
	private boolean syncScheduled;
	private boolean firstLoad;
	
	protected InventoryStorage inventory;
	
	public TileEntitySync()
	{
		this(0);
	}
	
	public TileEntitySync(int size)
	{
		inventory = new InventoryStorage(size);
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
		compound = inventory.serializeNBT();
		writeToNBTInternal(compound);
		return compound;
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound);
		readFromNBTInternal(compound);
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
			return (T)inventory;
		
		return super.getCapability(capability, facing);
	}
	
	public InventoryStorage getInventory()
	{
		return inventory;
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
