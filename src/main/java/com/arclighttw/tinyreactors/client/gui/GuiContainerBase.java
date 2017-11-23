package com.arclighttw.tinyreactors.client.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GuiContainerBase extends GuiContainer
{
	public GuiContainerBase(Container container)
	{
		super(container);
	}
	
	public float getZLevel()
	{
		return zLevel;
	}
	
	public void setZLevel(float zLevel)
	{
		this.zLevel = zLevel;
	}
	
	public float getItemZLevel()
	{
		return itemRender.zLevel;
	}
	
	public void setItemZLevel(float zLevel)
	{
		itemRender.zLevel = zLevel;
	}
	
	@Override
	public void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font)
	{
		super.drawHoveringText(textLines, x, y, font);
	}
}
