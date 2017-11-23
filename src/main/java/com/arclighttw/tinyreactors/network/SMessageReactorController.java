package com.arclighttw.tinyreactors.network;

import com.arclighttw.tinyreactors.helpers.NBTHelper;
import com.arclighttw.tinyreactors.lib.network.NetworkMessage;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SMessageReactorController extends NetworkMessage
{
	private BlockPos pos;
	private boolean active;
	
	public SMessageReactorController(TileEntityReactorController controller)
	{
		pos = controller.getPos();
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		NBTHelper.Serializer.blockPos(compound, "pos", pos);
		compound.setBoolean("active", active);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		pos = NBTHelper.Deserializer.blockPos(compound, "pos");
		active = compound.getBoolean("active");
	}
	
	public SMessageReactorController() {}
	public static class Handler implements IMessageHandler<SMessageReactorController, IMessage>
	{
		@Override
		public IMessage onMessage(SMessageReactorController message, MessageContext ctx)
		{
			World world = ctx.getServerHandler().player.world;
			TileEntity tile = world.getTileEntity(message.pos);
			
			if(tile != null && tile instanceof TileEntityReactorController)
			{
				TileEntityReactorController controller = (TileEntityReactorController)tile;
				controller.setActive(message.active);
			}
			
			return null;
		}
	}
}
