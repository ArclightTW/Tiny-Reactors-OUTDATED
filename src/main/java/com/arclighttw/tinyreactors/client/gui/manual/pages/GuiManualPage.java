package com.arclighttw.tinyreactors.client.gui.manual.pages;

import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.widgets.Widget;
import com.arclighttw.tinyreactors.main.Reference;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public class GuiManualPage
{
	private final String identifier;
	private int pageIndex;
	
	protected List<Widget> widgets;
	
	public GuiManualPage(String identifier)
	{
		this.identifier = identifier;
		
		initGui();
	}
	
	public void initGui()
	{
		widgets = Lists.newArrayList();
	}
	
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		for(Widget widget : widgets)
			widget.drawBackground(gui, guiLeft, guiTop, mouseX, mouseY);
	}
	
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		for(Widget widget : widgets)
			widget.drawForeground(gui, mouseX, mouseY);
	}
	
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
		for(Widget widget : widgets)
		{
			if(widget.mouseClicked(gui, mouseX, mouseY, mouseButton))
				return true;
		}
		
		return false;
	}
	
	public ResourceLocation getTexture()
	{
		return new ResourceLocation(Reference.ID, "textures/gui/manual/page.png");
	}
	
	public final String getIdentifier()
	{
		return identifier;
	}
	
	public final void setPageIndex(int pageIndex)
	{
		this.pageIndex = pageIndex;
	}
	
	public final int getPageIndex()
	{
		return pageIndex;
	}
	
	public static GuiManualPage parse(JsonObject json)
	{
		String type = json.get("type").getAsString();
		String id = json.has("id") ? json.get("id").getAsString() : null;
		
		switch(type)
		{
		case "cover":
			return new GuiManualCoverPage(id, json);
		case "contents":
		case "text":
			return new GuiManualTextPage(id, json);
		case "image":
			return new GuiManualImagePage(id, json);
		case "crafting":
			return new GuiManualCraftingPage(id, json);
		default:
			return new GuiManualPage(id);
		}
	}
}
