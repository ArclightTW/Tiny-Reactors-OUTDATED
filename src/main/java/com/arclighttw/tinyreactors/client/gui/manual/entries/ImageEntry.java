package com.arclighttw.tinyreactors.client.gui.manual.entries;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.main.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ImageEntry
{
	private final String sprite;
	
	private final int x;
	private final int y;
	
	private final int u;
	private final int v;
	
	private final int width;
	private final int height;
	
	public ImageEntry(String sprite, int x, int y, int u, int v, int width, int height)
	{
		this.sprite = sprite;
		
		this.x = x;
		this.y = y;
		
		this.u = u;
		this.v = v;
		
		this.width = width;
		this.height = height;
	}
	
	public void draw(GuiTinyManual gui, int guiLeft, int guiTop)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.ID, sprite));
		gui.drawTexturedModalRect(guiLeft + x, guiTop + y, u, v, width, height);
	}
}
