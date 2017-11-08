package com.arclighttw.tinyreactors.client.gui.manual.pages;

import java.util.Arrays;
import java.util.List;

import com.arclighttw.tinyreactors.client.ParseHelper;
import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.IconEntry;
import com.arclighttw.tinyreactors.client.gui.manual.entries.TextEntry;
import com.arclighttw.tinyreactors.client.gui.manual.widgets.WidgetButton;
import com.arclighttw.tinyreactors.proxy.ClientProxy;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GuiManualTextPage extends GuiManualPage
{
	private boolean drawIcons;
	
	protected List<TextEntry> textEntries;
	protected List<IconEntry> iconEntries;
	
	public GuiManualTextPage(String id, JsonObject json)
	{
		this(id, json, true);
	}
	
	public GuiManualTextPage(String id, JsonObject json, boolean drawIcons)
	{
		super(id);
		this.drawIcons = drawIcons;
		initGui();
		
		textEntries = Lists.newArrayList();
		iconEntries = Lists.newArrayList();
		
		JsonArray array = json.has("lines") ? json.get("lines").getAsJsonArray() : new JsonArray();
		
		for(int i = 0; i < array.size(); i++)
		{
			JsonObject element = array.get(i).getAsJsonObject();

			String text = element.has("text") ? element.get("text").getAsString() : "< Missing >";
			
			String x_alignment = element.has("x-alignment") ? element.get("x-alignment").getAsString() : "left";
			String y_alignment = element.has("y-alignment") ? element.get("y-alignment").getAsString() : "top";
			
			int border = element.has("border") ? element.get("border").getAsInt() : 0;
			
			int x = element.has("x") ? element.get("x").getAsInt() : 0;
			int y = element.has("y") ? element.get("y").getAsInt() : 0;
			
			String color = element.has("color") ? element.get("color").getAsString() : "FFFFFF";
			
			boolean wrapped = element.has("wrapped") ? element.get("wrapped").getAsBoolean() : false;
			
			textEntries.add(new TextEntry(text, x_alignment, y_alignment, border, x, y, Integer.parseInt(color, 16), wrapped));
		}
		
		array = json.has("stacks") ? json.get("stacks").getAsJsonArray() : new JsonArray();
		
		for(int i = 0; i < array.size(); i++)
		{
			JsonObject element = array.get(i).getAsJsonObject();

			String icon = element.has("icon") ? (element.get("icon").isJsonPrimitive() ? element.get("icon").getAsString() : null) : null;
			String label = element.has("label") ? (element.get("label").isJsonPrimitive() ? element.get("label").getAsString() : null) : null;
			String tooltip = element.has("tooltip") ? (element.get("tooltip").isJsonPrimitive() ? element.get("tooltip").getAsString() : null) : null;
			
			List<ItemStack> itemstacks = Lists.newArrayList();
			
			if(icon != null)
				itemstacks = Arrays.asList(ParseHelper.parseItemStack(icon));
			else
			{
				if(element.has("icon") && element.get("icon").isJsonObject())
					itemstacks.add(ParseHelper.parseItemStack(element.has("output") ? element.get("output").getAsJsonObject() : null));
				else
				{						
					JsonArray icons = element.has("icon") ? element.get("icon").getAsJsonArray() : new JsonArray();
					
					for(int j = 0; j < icons.size(); j++)
						itemstacks.add(ParseHelper.parseItemStack(icons.get(j).getAsString()));
				}
			}
			
			List<String> lbls = Lists.newArrayList();
			
			if(label != null)
				lbls = Arrays.asList(label);
			else
			{
				JsonArray labels = element.has("label") ? element.get("label").getAsJsonArray() : new JsonArray();
				
				for(int j = 0; j < labels.size(); j++)
					lbls.add(labels.get(j).getAsString());
			}
			
			List<String> tips = Lists.newArrayList();
			
			if(tooltip != null)
				tips = Arrays.asList(tooltip);
			else
			{
				JsonArray tooltips = element.has("tooltip") ? element.get("tooltip").getAsJsonArray() : new JsonArray();
				
				for(int j = 0; j < tooltips.size(); j++)
					tips.add(tooltips.get(j).getAsString());
			}
			
			String link = element.has("link") ? element.get("link").getAsString() : "";
			
			int x = element.has("x") ? element.get("x").getAsInt() : 0;
			int y = element.has("y") ? element.get("y").getAsInt() : 0;
			
			iconEntries.add(new IconEntry(itemstacks, lbls, tips, link, x, y));
		}
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		if(!drawIcons)
			return;
		
		widgets.add(new WidgetButton(GuiTinyManual.WIDGETS, 156, 12, 20, 0, 11, 11, (gui) -> {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}));
		
		widgets.add(new WidgetButton(GuiTinyManual.WIDGETS, 21, 12, 0, 8, 10, 8, (gui) -> {
			gui.attemptNavigation("contents");
		}));
		
		widgets.add(new WidgetButton(GuiTinyManual.WIDGETS, 15, 12, 0, 8, 10, 8, (gui) -> {
			gui.attemptNavigation("contents");
		}));
	}
	
	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		super.drawBackground(gui, guiLeft, guiTop, mouseX, mouseY);
		
		for(IconEntry entry : iconEntries)
			entry.drawStack(gui);
	}
	
	@Override
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		super.drawForeground(gui, mouseX, mouseY);
		
		if(!drawIcons)
			return;
		
		for(TextEntry entry : textEntries)
			entry.draw(gui, ClientProxy.fontRenderer);
		
		for(IconEntry entry : iconEntries)
		{
			entry.drawLabel(gui, gui.getFontRenderer(), mouseX, mouseY);
			entry.drawTooltip(gui, gui.getFontRenderer(), mouseX, mouseY);
		}
	}
	
	@Override
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
		for(IconEntry entry : iconEntries)
		{
			if(entry.mouseClicked(gui, mouseX, mouseY, mouseButton))
				return true;
		}
		
		return super.mouseClicked(gui, mouseX, mouseY, mouseButton);
	}
}
