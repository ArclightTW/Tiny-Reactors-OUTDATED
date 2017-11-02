package com.arclighttw.tinyreactors.proxy;

import com.arclighttw.tinyreactors.client.render.RenderTileEntityCapacitor;
import com.arclighttw.tinyreactors.client.render.RenderTileEntityReactorController;
import com.arclighttw.tinyreactors.client.render.RenderTileEntityReactorEnergyPort;
import com.arclighttw.tinyreactors.client.render.RenderTileEntityReactorVent;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.managers.ModelBakeEventManager;
import com.arclighttw.tinyreactors.tiles.TileEntityCapacitor;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorVent;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	private static final StateMapperBase getIgnoreState(Block block)
	{
		return new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return new ModelResourceLocation(block.getRegistryName(), "normal");
			}
		};
	}
	
	@Override
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
		ModelLoader.setCustomStateMapper(TRBlocks.DEGRADED_REACTANT, getIgnoreState(TRBlocks.DEGRADED_REACTANT));
		
		MinecraftForge.EVENT_BUS.register(new ModelBakeEventManager());
	}
	
	@Override
	public void onInitialization(FMLInitializationEvent event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorController.class, new RenderTileEntityReactorController());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorEnergyPort.class, new RenderTileEntityReactorEnergyPort());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorVent.class, new RenderTileEntityReactorVent());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCapacitor.class, new RenderTileEntityCapacitor());
	}
	
	@Override
	public void onPostInitialization(FMLPostInitializationEvent event)
	{
	}
}
