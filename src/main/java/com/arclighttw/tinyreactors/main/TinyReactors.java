package com.arclighttw.tinyreactors.main;

import java.io.File;

import com.arclighttw.tinyreactors.config.ModConfig;
import com.arclighttw.tinyreactors.inits.Registry;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.integration.WailaIntegration;
import com.arclighttw.tinyreactors.managers.GuiManager;
import com.arclighttw.tinyreactors.managers.ProcessManager;
import com.arclighttw.tinyreactors.network.SMessageReactorControllerActive;
import com.arclighttw.tinyreactors.network.SMessageReactorControllerRedstone;
import com.arclighttw.tinyreactors.network.SMessageReactorEnergyPort;
import com.arclighttw.tinyreactors.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, acceptedMinecraftVersions = Reference.MINECRAFT, guiFactory = Reference.GUI_FACTORY)
public class TinyReactors
{
	@Mod.Instance(value = Reference.ID)
	public static TinyReactors instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
	public static CommonProxy proxy;
	
	public static CreativeTabs tab = new CreativeTabs(Reference.ID) {
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(TRBlocks.REACTOR_GLASS);
		}
	};
	
	public static SimpleNetworkWrapper network;
	
	@Mod.EventHandler
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
		File configDir = new File(event.getModConfigurationDirectory(), Reference.ID);
		configDir.mkdir();
		
		ModConfig.initialize(new File(configDir, "config.cfg"));
		
		MinecraftForge.EVENT_BUS.register(new Registry());
		proxy.onPreInitialization(event);
	}

	@Mod.EventHandler
	public void onInitialization(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiManager());
		proxy.onInitialization(event);
		
		WailaIntegration.register();
		ProcessManager.register();
		
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);
		network.registerMessage(SMessageReactorEnergyPort.Handler.class, SMessageReactorEnergyPort.class, 0, Side.SERVER);
		network.registerMessage(SMessageReactorControllerActive.Handler.class, SMessageReactorControllerActive.class, 1, Side.SERVER);
		network.registerMessage(SMessageReactorControllerRedstone.Handler.class, SMessageReactorControllerRedstone.class, 2, Side.SERVER);
	}
	
	@Mod.EventHandler
	public void onPostInitialization(FMLPostInitializationEvent event)
	{
		proxy.onPostInitialization(event);
	}
}
