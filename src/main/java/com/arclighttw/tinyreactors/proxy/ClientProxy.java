package com.arclighttw.tinyreactors.proxy;

import com.arclighttw.tinyreactors.client.SmallFontRenderer;
import com.arclighttw.tinyreactors.helpers.UIHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void onPreInitialization(FMLPreInitializationEvent event)
	{
	}
	
	@Override
	public void onInitialization(FMLInitializationEvent event)
	{
		UIHelper.smallFontRenderer = new SmallFontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine);
	}
	
	@Override
	public void onPostInitialization(FMLPostInitializationEvent event)
	{
	}
}
