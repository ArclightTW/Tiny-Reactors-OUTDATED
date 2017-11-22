package com.arclighttw.tinyreactors.proxy;

import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.inits.TRItems;
import com.arclighttw.tinyreactors.inits.TRRecipes;
import com.arclighttw.tinyreactors.inits.TRSounds;
import com.arclighttw.tinyreactors.main.TinyReactors;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
		TinyReactors.instance.register(TRBlocks.class);
		TinyReactors.instance.register(TRItems.class);
		TinyReactors.instance.register(TRRecipes.class);
		TinyReactors.instance.register(TRSounds.class);
	}
	
	public void onInitialization(FMLInitializationEvent event)
	{
	}
	
	public void onPostInitialization(FMLPostInitializationEvent event)
	{
	}	
}
