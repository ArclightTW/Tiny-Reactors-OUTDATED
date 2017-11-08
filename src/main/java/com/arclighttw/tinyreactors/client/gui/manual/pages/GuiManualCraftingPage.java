package com.arclighttw.tinyreactors.client.gui.manual.pages;

import java.util.List;

import com.arclighttw.tinyreactors.client.ParseHelper;
import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.RecipeEntry;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;

public class GuiManualCraftingPage extends GuiManualTextPage
{
	protected List<RecipeEntry> entries;
	
	public GuiManualCraftingPage(String id, JsonObject json)
	{
		super(id, json, true);
		
		entries = Lists.newArrayList();
		
		JsonArray array = json.has("recipes") ? json.get("recipes").getAsJsonArray() : new JsonArray();
		
		for(int i = 0; i < array.size(); i++)
		{
			JsonObject element = array.get(i).getAsJsonObject();

			int size = element.has("size") ? element.get("size").getAsInt() : 2;
			String type = element.has("type") ? element.get("type").getAsString() : "shaped";
			boolean shaped = type == "shaped";
			
			int x = element.has("x") ? element.get("x").getAsInt() : 0;
			int y = element.has("y") ? element.get("y").getAsInt() : 0;
			
			ItemStack output = ParseHelper.parseItemStack(element.has("output") ? element.get("output").getAsJsonObject() : null);
			
			List<ItemStack> inputStacks = Lists.newArrayList();
			JsonArray inputs = element.has("inputs") ? element.get("inputs").getAsJsonArray() : new JsonArray();
			
			for(int j = 0; j < inputs.size(); j++)
			{
				ItemStack input = ParseHelper.parseItemStack(inputs.get(j).getAsJsonObject());
				
				if(input == ItemStack.EMPTY)
				{
					if(shaped)
						inputStacks.add(input);
					
					continue;
				}
				
				inputStacks.add(input);
			}
			
			entries.add(new RecipeEntry(x, y, size, shaped, output, inputStacks));
		}
	}

	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		super.drawBackground(gui, guiLeft, guiTop, mouseX, mouseY);
		
		for(RecipeEntry entry : entries)
			entry.drawBackground(gui, guiLeft, guiTop);
	}
	
	@Override
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		super.drawForeground(gui, mouseX, mouseY);
		
		for(RecipeEntry entry : entries)
			entry.drawForeground(gui, mouseX, mouseY);
	}
}
