package com.arclighttw.tinyreactors.client.gui.manual;

import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.TextEntry;
import com.arclighttw.tinyreactors.proxy.ClientProxy;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;

public class GuiManualTextPage extends GuiManualPage
{
	protected List<TextEntry> entries;
	
	public GuiManualTextPage(String id, JsonObject json)
	{
		super(id);
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
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
		boolean clicked = mouseX - gui.getGuiLeft() >= 154 && mouseX - gui.getGuiLeft() <= 154 + 16 && mouseY - gui.getGuiTop() >= 12 && mouseY - gui.getGuiTop() <= 12 + 15;
		
		if(clicked)
			gui.attemptNavigation("contents");
		
		return clicked;
	}
	
	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTinyManual.WIDGETS);
		gui.drawTexturedModalRect(guiLeft + 154, guiTop + 12, 20, 0, 16, 15);
	}
	
	@Override
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		for(TextEntry entry : entries)
			entry.draw(gui, ClientProxy.fontRenderer);
	}
}
