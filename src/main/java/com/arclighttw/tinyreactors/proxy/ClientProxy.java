package com.arclighttw.tinyreactors.proxy;

import com.arclighttw.tinyreactors.client.render.RenderTileEntityCapacitor;
import com.arclighttw.tinyreactors.client.render.RenderTileEntityReactorController;
import com.arclighttw.tinyreactors.client.render.RenderTileEntityReactorEnergyPort;
import com.arclighttw.tinyreactors.tiles.TileEntityCapacitor;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
	}
	
	@Override
	public void onInitialization(FMLInitializationEvent event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorController.class, new RenderTileEntityReactorController());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorEnergyPort.class, new RenderTileEntityReactorEnergyPort());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCapacitor.class, new RenderTileEntityCapacitor());
	}
	
	@Override
	public void onPostInitialization(FMLPostInitializationEvent event)
	{
	}
}
