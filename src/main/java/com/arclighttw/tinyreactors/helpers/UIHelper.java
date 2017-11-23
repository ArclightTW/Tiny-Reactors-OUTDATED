package com.arclighttw.tinyreactors.helpers;

import java.util.Arrays;
import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiContainerBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class UIHelper
{
	public static FontRenderer smallFontRenderer;
	public static FontRenderer vanillaFontRenderer = Minecraft.getMinecraft().fontRenderer;
	
	public static void drawHoveringText(GuiContainerBase gui, String line, int mouseX, int mouseY)	
	{
		drawHoveringText(gui, Arrays.asList(TranslationHelper.translate(line)), mouseX, mouseY);
	}
	
	public static void drawHoveringText(GuiContainerBase gui, List<String> lines, int mouseX, int mouseY)	
	{
		drawHoveringText(gui, lines, mouseX, mouseY, vanillaFontRenderer);
	}
	
	public static void drawHoveringText(GuiContainerBase gui, List<String> lines, int x, int y, FontRenderer fontRenderer)
    {
		gui.drawHoveringText(lines, x, y, fontRenderer);
    }
}
