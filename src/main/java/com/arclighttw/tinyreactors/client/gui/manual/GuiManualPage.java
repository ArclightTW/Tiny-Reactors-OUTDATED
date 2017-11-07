package com.arclighttw.tinyreactors.client.gui.manual;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.main.Reference;
import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public class GuiManualPage
{
	private final String identifier;
	private int pageIndex;
	
	public GuiManualPage(String identifier)
	{
		this.identifier = identifier;
	}
	
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
	}
	
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
	}
	
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
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
		case "text":
			return new GuiManualTextPage(id, json);
		case "contents":
			return new GuiManualContentsPage(id, json);
		default:
			return new GuiManualPage(id);
		}
	}
}
