package com.arclighttw.tinyreactors.network;

import com.arclighttw.tinyreactors.properties.EnumRedstoneMode;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SMessageReactorControllerRedstone implements IMessage
{
	private EnumRedstoneMode mode;
	private BlockPos pos;
	
	public SMessageReactorControllerRedstone()
	{
	}
	
	public SMessageReactorControllerRedstone(EnumRedstoneMode mode, BlockPos pos)
	{
		this.mode = mode;
		this.pos = pos;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(mode.ordinal());
		
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		mode = EnumRedstoneMode.values()[buf.readInt()];
		
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		pos = new BlockPos(x, y, z);
	}
	
	public static class Handler implements IMessageHandler<SMessageReactorControllerRedstone, IMessage>
	{
		@Override
		public IMessage onMessage(SMessageReactorControllerRedstone message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			TileEntity tile = world.getTileEntity(message.pos);
			
			if(tile != null && tile instanceof TileEntityReactorController)
			{
				TileEntityReactorController controller = (TileEntityReactorController)tile;
				controller.toggleRedstone(message.mode);
				controller.sync();
			}
			
			return null;
		}
	}
}
