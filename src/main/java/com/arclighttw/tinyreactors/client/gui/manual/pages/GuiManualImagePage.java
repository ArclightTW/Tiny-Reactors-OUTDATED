package com.arclighttw.tinyreactors.client.gui.manual.pages;

import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.ImageEntry;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GuiManualImagePage extends GuiManualTextPage
{
	protected List<ImageEntry> entries;
	
	public GuiManualImagePage(String id, JsonObject json)
	{
		super(id, json);
		
		entries = Lists.newArrayList();
		
		JsonArray array = json.has("images") ? json.get("images").getAsJsonArray() : new JsonArray();
		
		for(int i = 0; i < array.size(); i++)
		{
			JsonObject element = array.get(i).getAsJsonObject();

			String sprite = element.has("sprite") ? element.get("sprite").getAsString() : null;
			
			if(sprite == null)
				continue;
			
			int x = element.has("x") ? element.get("x").getAsInt() : 0;
			int y = element.has("y") ? element.get("y").getAsInt() : 0;
			
			int u = element.has("u") ? element.get("u").getAsInt() : 0;
			int v = element.has("v") ? element.get("v").getAsInt() : 0;
			
			int width = element.has("width") ? element.get("width").getAsInt() : 0;
			int height = element.has("height") ? element.get("height").getAsInt() : 0;
			
			entries.add(new ImageEntry(sprite, x, y, u, v, width, height));
		}
	}
	
	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		for(ImageEntry entry : entries)
			entry.draw(gui, guiLeft, guiTop);
		
		super.drawBackground(gui, guiLeft, guiTop, mouseX, mouseY);
	}
}
