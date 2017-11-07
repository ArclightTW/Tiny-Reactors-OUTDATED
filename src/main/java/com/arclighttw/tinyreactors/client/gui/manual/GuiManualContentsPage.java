package com.arclighttw.tinyreactors.client.gui.manual;

import java.util.Arrays;
import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.IconEntry;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GuiManualContentsPage extends GuiManualPage
{
	protected List<IconEntry> entries;
	
	public GuiManualContentsPage(String id, JsonObject json)
	{
		super(id);
		entries = Lists.newArrayList();
		
		JsonArray array = json.has("stacks") ? json.get("stacks").getAsJsonArray() : new JsonArray();
		
		for(int i = 0; i < array.size(); i++)
		{
			JsonObject element = array.get(i).getAsJsonObject();

			String icon = element.has("icon") ? element.get("icon").getAsString() : null;
			int metadata = element.has("metadata") ? (element.get("metadata").isJsonPrimitive() ? element.get("metadata").getAsInt() : -1) : 0;
			String tooltip = element.has("tooltip") ? (element.get("tooltip").isJsonPrimitive() ? element.get("tooltip").getAsString() : null) : null;
			
			List<Integer> metas = Lists.newArrayList();
			
			if(metadata == 0)
				metas = Arrays.asList(metadata);
			else
			{
				JsonArray metadatas = element.has("metadata") ? element.get("metadata").getAsJsonArray() : new JsonArray();
				
				for(int j = 0; j < metadatas.size(); j++)
					metas.add(metadatas.get(j).getAsInt());
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
			
			entries.add(new IconEntry(icon, metas, tips, link, x, y));
		}
	}
	
	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		for(IconEntry entry : entries)
			entry.drawStack(gui);
	}
	
	@Override
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		for(IconEntry entry : entries)
			entry.drawLabel(gui, gui.getFontRenderer(), mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
		for(IconEntry entry : entries)
		{
			if(entry.mouseClicked(gui, mouseX, mouseY, mouseButton))
				return true;
		}
		
		return false;
	}
}
