package com.arclighttw.tinyreactors.network;

import com.arclighttw.tinyreactors.helpers.NBTHelper;
import com.arclighttw.tinyreactors.lib.network.NetworkMessage;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SMessageReactorEnergyPort extends NetworkMessage
{
	private BlockPos pos;
	private int input;
	private int output;
	
	public SMessageReactorEnergyPort(TileEntityReactorEnergyPort energyPort)
	{
		pos = energyPort.getPos();
	}
	
	public void setInput(int input)
	{
		this.input = input;
	}
	
	public void setOutput(int output)
	{
		this.output = output;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		NBTHelper.Serializer.blockPos(compound, "pos", pos);
		
		compound.setInteger("input", input);
		compound.setInteger("output", output);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		pos = NBTHelper.Deserializer.blockPos(compound, "pos");
		
		input = compound.getInteger("input");
		output = compound.getInteger("output");
	}
	
	public SMessageReactorEnergyPort() {}
	public static class Handler implements IMessageHandler<SMessageReactorEnergyPort, IMessage>
	{
		@Override
		public IMessage onMessage(SMessageReactorEnergyPort message, MessageContext ctx)
		{
			World world = ctx.getServerHandler().player.world;
			TileEntity tile = world.getTileEntity(message.pos);
			
			if(tile != null && tile instanceof TileEntityReactorEnergyPort)
			{
				TileEntityReactorEnergyPort energyPort = (TileEntityReactorEnergyPort)tile;
				energyPort.getEnergy().setCurrentReceive(energyPort.getEnergy().getCurrentReceive() + Long.valueOf((long)message.input));
				energyPort.getEnergy().setCurrentExtract(energyPort.getEnergy().getCurrentExtract() + Long.valueOf((long)message.output));
			}
			
			return null;
		}
	}
}
