package com.arclighttw.tinyreactors.client.gui.manual.entries;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;

import net.minecraft.client.gui.FontRenderer;

public class TextEntry
{
	private final String text;
	
	private final String x_alignment;
	private final String y_alignment;
	
	private final int border;
	
	private final int x;
	private final int y;
	
	private final int color;
	
	private final boolean wrapped;
	
	public TextEntry(String text, String x_alignment, String y_alignment, int border, int x, int y, int color, boolean wrapped)
	{
		this.text = text;
		
		this.x_alignment = x_alignment;
		this.y_alignment = y_alignment;
		
		this.border = border;
		
		this.x = x;
		this.y = y;
		
		this.color = color;
		
		this.wrapped = wrapped;
	}
	
	public void draw(GuiTinyManual gui, FontRenderer fontRenderer)
	{
		int posX = gui.getXSize() / 2;
		posX += x;
		
		switch(x_alignment)
		{
		case "left":
			posX = border + x;
			break;
		case "center":
			posX -= fontRenderer.getStringWidth(text) / 2;
			break;
		case "right":
			posX = gui.getXSize() - x - border - fontRenderer.getStringWidth(text);
			break;
		}
		
		int posY = gui.getYSize() / 2;
		posY += y;
		
		switch(y_alignment)
		{
		case "top":
			posY = border + y;
			break;
		case "middle":
			posY -= fontRenderer.FONT_HEIGHT / 2;
			break;
		case "bottom":
			posY = gui.getYSize() - y - border - fontRenderer.FONT_HEIGHT;
			break;
		}
		
		if(wrapped)
			fontRenderer.drawSplitString(text, posX, posY, gui.getXSize() - (border * 2), color);
		else
			fontRenderer.drawString(text, posX, posY, color);
	}
}
