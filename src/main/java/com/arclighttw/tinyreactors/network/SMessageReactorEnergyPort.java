package com.arclighttw.tinyreactors.network;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SMessageReactorEnergyPort implements IMessage
{
	public enum Mode
	{
		DecreaseInput,
		IncreaseInput,
		DecreaseOutput,
		IncreaseOutput
	}
	
	private Mode mode;
	private int amount;
	
	private BlockPos pos;
	
	public SMessageReactorEnergyPort()
	{
	}
	
	public SMessageReactorEnergyPort(Mode mode, int amount, BlockPos pos)
	{
		this.mode = mode;
		this.amount = amount;
		
		this.pos = pos;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(mode.ordinal());
		buf.writeInt(amount);
		
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		mode = Mode.values()[buf.readInt()];
		amount = buf.readInt();
		
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		pos = new BlockPos(x, y, z);
	}
	
	public static class Handler implements IMessageHandler<SMessageReactorEnergyPort, IMessage>
	{
		@Override
		public IMessage onMessage(SMessageReactorEnergyPort message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			TileEntity tile = world.getTileEntity(message.pos);
			
			if(tile != null && tile instanceof TileEntityReactorEnergyPort)
			{
				TileEntityReactorEnergyPort energyPort = (TileEntityReactorEnergyPort)tile;
				energyPort.getEnergyStorage().modify(message.mode, message.amount);
				energyPort.sync();
			}
			
			return null;
		}
	}
}
