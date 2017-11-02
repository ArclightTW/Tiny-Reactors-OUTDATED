package com.arclighttw.tinyreactors.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntitySync extends TileEntity implements ITickable
{
	private boolean syncScheduled;
	private boolean firstLoad;
	private boolean syncUpcoming;
	
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
	public void update()
	{
		if(world == null)
			return;
		
		if(syncScheduled)
			sync();
		
		if(!firstLoad)
		{
			onInitialLoad();
			firstLoad = true;
			sync();
		}
		
		syncUpcoming = false;
	}
	
	public void sync()
	{
		if(world == null)
		{
			syncScheduled = true;
			return;
		}
		
		if(syncUpcoming)
			return;
		
		if(!world.isRemote)
		{
			syncScheduled = false;
			syncUpcoming = true;
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
			world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
			markDirty();
		}
	}
	
	public void onInitialLoad()
	{
	}
}
