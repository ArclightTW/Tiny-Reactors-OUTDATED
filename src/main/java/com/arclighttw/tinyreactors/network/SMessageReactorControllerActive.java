package com.arclighttw.tinyreactors.network;

import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SMessageReactorControllerActive implements IMessage
{
	private boolean enabled;
	private BlockPos pos;
	
	public SMessageReactorControllerActive()
	{
	}
	
	public SMessageReactorControllerActive(boolean enabled, BlockPos pos)
	{
		this.enabled = enabled;
		this.pos = pos;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(enabled);
		
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		enabled = buf.readBoolean();
		
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		pos = new BlockPos(x, y, z);
	}
	
	public static class Handler implements IMessageHandler<SMessageReactorControllerActive, IMessage>
	{
		@Override
		public IMessage onMessage(SMessageReactorControllerActive message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			TileEntity tile = world.getTileEntity(message.pos);
			
			if(tile != null && tile instanceof TileEntityReactorController)
			{
				TileEntityReactorController controller = (TileEntityReactorController)tile;
				controller.setActive(message.enabled);
			}
			
			return null;
		}
	}
}
