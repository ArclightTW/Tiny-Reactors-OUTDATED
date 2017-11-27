package com.arclighttw.tinyreactors.main;

import java.io.File;

import com.arclighttw.tinyreactors.config.ModConfig;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.inits.TRItems;
import com.arclighttw.tinyreactors.inits.TRRecipes;
import com.arclighttw.tinyreactors.inits.TRSounds;
import com.arclighttw.tinyreactors.lib.registry.ModRegistry;
import com.arclighttw.tinyreactors.managers.GuiHandler;
import com.arclighttw.tinyreactors.network.SMessageReactorController;
import com.arclighttw.tinyreactors.network.SMessageReactorEnergyPort;
import com.arclighttw.tinyreactors.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MINECRAFT, dependencies = Reference.DEPENDENCIES, guiFactory = Reference.GUI_FACTORY)
public class TinyReactors extends ModRegistry
{
	@Mod.Instance(value = Reference.ID)
	public static TinyReactors instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
		ModConfig.onPreInitialization(new File(event.getModConfigurationDirectory(), "tinyreactors.cfg"));
		
		register(TRBlocks.class);
		register(TRItems.class);
		register(TRRecipes.class);
		register(TRSounds.class);
		
		proxy.onPreInitialization(event);
	}
	
	@Mod.EventHandler
	public void onInitialization(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		
		network.registerMessage(SMessageReactorController.Handler.class, SMessageReactorController.class, 0, Side.SERVER);
		network.registerMessage(SMessageReactorEnergyPort.Handler.class, SMessageReactorEnergyPort.class, 1, Side.SERVER);
		
		proxy.onInitialization(event);
	}
	
	@Mod.EventHandler
	public void onPostInitialization(FMLPostInitializationEvent event)
	{
		proxy.onPostInitialization(event);
	}
}
