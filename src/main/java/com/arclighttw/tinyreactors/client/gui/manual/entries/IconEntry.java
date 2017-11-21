package com.arclighttw.tinyreactors.client.gui.manual.entries;

import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class IconEntry
{
	private final List<ItemStack> icons;
	private final List<String> label;
	private final List<String> tooltip;
	
	private final String link;
	
	private final int x;
	private final int y;
	
	private int index;
	private int counter;
	
	public IconEntry(List<ItemStack> icons, List<String> label, List<String> tooltip, String link, int x, int y)
	{
		this.icons = icons;
		this.label = label;
		this.tooltip = tooltip;
		this.link = link;
		
		this.x = x;
		this.y = y;
	}
	
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
		if(link == null)
			return false;
		
		boolean clicked = mouseX - gui.getGuiLeft() >= x && mouseX - gui.getGuiLeft() <= x + 16 && mouseY - gui.getGuiTop() >= y && mouseY - gui.getGuiTop() <= y + 16;
		
		if(clicked)
			gui.attemptNavigation(link);
		
		return clicked;
	}
	
	public void drawStack(GuiTinyManual gui)
	{
		if(icons.size() == 0)
			return;
		
		counter++;
		
		if(counter >= 40)
		{
			counter = 0;
			index++;
			
			if(index >= icons.size())
				index = 0;
		}
		
		gui.drawItemStack(icons.get(index), gui.getGuiLeft() + x, gui.getGuiTop() + y, icons.get(index).getCount() > 1 ? "x" + icons.get(index).getCount() : "");
	}
	
	public void drawTooltip(GuiTinyManual gui, FontRenderer fontRenderer, int mouseX, int mouseY)
	{
		boolean hovered = mouseX - gui.getGuiLeft() >= x && mouseX - gui.getGuiLeft() <= x + 16 && mouseY - gui.getGuiTop() >= y && mouseY - gui.getGuiTop() <= y + 16;
		
		if(hovered)
			gui.drawHoveringText(tooltip, mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
	}
	
	public void drawLabel(GuiTinyManual gui, FontRenderer fontRenderer, int mouseX, int mouseY)
	{
		boolean hovered = mouseX - gui.getGuiLeft() >= x && mouseX - gui.getGuiLeft() <= x + 16 && mouseY - gui.getGuiTop() >= y && mouseY - gui.getGuiTop() <= y + 16;
		
		for(int i = 0; i < label.size(); i++)
			fontRenderer.drawString(String.format("%s%s", hovered ? "§o" : "", label.get(i)), x + 8 - fontRenderer.getStringWidth(label.get(i)) / 2, y + 18 + ((fontRenderer.FONT_HEIGHT + 1) * i), 0xFFFFFF);
	}
}
