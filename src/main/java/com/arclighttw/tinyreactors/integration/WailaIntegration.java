package com.arclighttw.tinyreactors.integration;

import java.util.List;

import com.arclighttw.tinyreactors.blocks.BlockCapacitor;
import com.arclighttw.tinyreactors.blocks.BlockReactorController;
import com.arclighttw.tinyreactors.blocks.BlockReactorEnergyPort;
import com.arclighttw.tinyreactors.tiles.TileEntityEnergy;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorVent;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;

@Optional.InterfaceList({
	@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "waila")
})
public class WailaIntegration implements IWailaDataProvider
{
	public WailaIntegration()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private List<String> getWailaInfo(ItemStack itemstack, List<String> text, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		if(itemstack == ItemStack.EMPTY)
			return text;
		
		Item item = itemstack.getItem();
		Block block = Block.getBlockFromItem(item);
		
		if(block == null)
			return text;
		
		if(block instanceof BlockReactorEnergyPort)
			text.add("Tier: " + ((BlockReactorEnergyPort)block).getTier().getName());
		
		if(block instanceof BlockReactorController)
			text.add("Tier: " + ((BlockReactorController)block).getTier().getName());
		
		if(block instanceof BlockCapacitor)
			text.add("Tier: " + ((BlockCapacitor)block).getTier().getName());
		
		TileEntity tile = accessor.getTileEntity();
		
		if(tile == null)
			return text;
		
		if(tile instanceof TileEntityReactorVent)
		{
			TileEntityReactorVent vent = (TileEntityReactorVent)tile;
			text.add(String.format("State: %s", vent.isOperational() ? "Operational" : (vent.isObstructed() ? "Obstructed" : "Non-operational")));
		}
		
		if(accessor.getPlayer().isSneaking())
		{
			if(tile instanceof TileEntityEnergy)
			{
				TileEntityEnergy energy = (TileEntityEnergy)tile;
				text.add(String.format("Energy: %,d RF", energy.getEnergyStored()));
				text.add(String.format("Capacity: %,d RF", energy.getMaxEnergyStored()));
			}
		}
		
		return text;
	}
	
	@Override
	@Optional.Method(modid = "waila")
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return getWailaInfo(itemStack, currenttip, accessor, config);
	}
	
	@Override
	@Optional.Method(modid = "waila")
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return getWailaInfo(itemStack, currenttip, accessor, config);
	}
	
	@Override
	@Optional.Method(modid = "waila")
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return getWailaInfo(itemStack, currenttip, accessor, config);
	}
	
	@Optional.Method(modid = "waila")
	public static void callbackRegister(final IWailaRegistrar registrar)
	{
		WailaIntegration instance = new WailaIntegration();
		registrar.registerHeadProvider(instance, Block.class);
	}
	
	public static void register()
	{
		FMLInterModComms.sendMessage("waila", "register", WailaIntegration.class.getName() + ".callbackRegister");
	}
}
