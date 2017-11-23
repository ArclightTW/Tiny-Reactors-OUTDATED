package com.arclighttw.tinyreactors.lib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class NetworkMessage implements IMessage
{
	@Override
	public final void toBytes(ByteBuf buf)
	{
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		
		ByteBufUtils.writeTag(buf, compound);
	}
	
	@Override
	public final void fromBytes(ByteBuf buf)
	{
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		readFromNBT(compound);
	}
	
	public abstract void writeToNBT(NBTTagCompound compound);
	public abstract void readFromNBT(NBTTagCompound compound);
}
