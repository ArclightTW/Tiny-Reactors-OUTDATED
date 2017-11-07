package com.arclighttw.tinyreactors.client.gui.manual.entries;

import java.util.Arrays;
import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class IconEntry
{
	private final List<ItemStack> icon;
	private final List<String> tooltip;
	
	private final String link;
	
	private final int x;
	private final int y;
	
	private int index;
	private int counter;
	
	public IconEntry(String icon, int metadata, String tooltip, String link, int x, int y)
	{
		this(icon, Arrays.asList(metadata), Arrays.asList(tooltip), link, x, y);
	}
	
	public IconEntry(String icon, List<Integer> metadata, String tooltip, String link, int x, int y)
	{
		this(icon, metadata, Arrays.asList(tooltip), link, x, y);
	}
	
	public IconEntry(String icon, int metadata, List<String> tooltip, String link, int x, int y)
	{
		this(icon, Arrays.asList(metadata), tooltip, link, x, y);
	}
	
	public IconEntry(String icon, List<Integer> metadata, List<String> tooltip, String link, int x, int y)
	{
		this.icon = Lists.newArrayList();
		this.tooltip = tooltip;
		this.link = link;
		
		this.x = x;
		this.y = y;
		
		ResourceLocation registry = new ResourceLocation(icon);
		Block block = Block.REGISTRY.getObject(registry);
		
		if(block != null && block != Blocks.AIR)
		{
			for(Integer meta : metadata)
				this.icon.add(new ItemStack(block, 1, meta));
				
			return;
		}
		
		Item item = Item.REGISTRY.getObject(registry);
		
		if(item != null)
		{
			for(Integer meta : metadata)
				this.icon.add(new ItemStack(block, 1, meta));
			
			return;
		}
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
		if(icon.size() == 0)
			return;
		
		counter++;
		
		if(counter >= 40)
		{
			counter = 0;
			index++;
			
			if(index >= icon.size())
				index = 0;
		}
		
		gui.drawItemStack(icon.get(index), gui.getGuiLeft() + x, gui.getGuiTop() + y, "");
	}
	
	public void drawLabel(GuiTinyManual gui, FontRenderer fontRenderer, int mouseX, int mouseY)
	{
		boolean hovered = mouseX - gui.getGuiLeft() >= x && mouseX - gui.getGuiLeft() <= x + 16 && mouseY - gui.getGuiTop() >= y && mouseY - gui.getGuiTop() <= y + 16;
		
		for(int i = 0; i < tooltip.size(); i++)
			fontRenderer.drawString(String.format("%s%s", hovered ? "§o" : "", tooltip.get(i)), x + 8 - fontRenderer.getStringWidth(tooltip.get(i)) / 2, y + 18 + ((fontRenderer.FONT_HEIGHT + 1) * i), 0xFFFFFF);
	}
}
