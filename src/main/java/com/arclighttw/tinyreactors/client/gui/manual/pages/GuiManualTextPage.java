package com.arclighttw.tinyreactors.client.gui.manual.pages;

import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.TextEntry;
import com.arclighttw.tinyreactors.client.gui.manual.widgets.WidgetButton;
import com.arclighttw.tinyreactors.proxy.ClientProxy;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;

public class GuiManualTextPage extends GuiManualPage
{
	private boolean drawIcons;
	protected List<TextEntry> entries;
	
	public GuiManualTextPage(String id, JsonObject json)
	{
		this(id, json, true);
	}
	
	public GuiManualTextPage(String id, JsonObject json, boolean drawIcons)
	{
		super(id);
		this.drawIcons = drawIcons;
		initGui();
		
		entries = Lists.newArrayList();
		
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
			
			entries.add(new TextEntry(text, x_alignment, y_alignment, border, x, y, Integer.parseInt(color, 16), wrapped));
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
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		super.drawForeground(gui, mouseX, mouseY);
		
		if(!drawIcons)
			return;
		
		for(TextEntry entry : entries)
			entry.draw(gui, ClientProxy.fontRenderer);
	}
}
