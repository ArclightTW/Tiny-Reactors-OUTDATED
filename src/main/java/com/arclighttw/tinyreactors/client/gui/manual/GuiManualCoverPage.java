package com.arclighttw.tinyreactors.client.gui.manual;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.TextEntry;
import com.arclighttw.tinyreactors.main.Reference;
import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public class GuiManualCoverPage extends GuiManualTextPage
{
	protected String cover;
	
	public GuiManualCoverPage(String id, JsonObject json)
	{
		super(id, json);
		this.cover = json.get("cover").getAsString();
	}
	
	@Override
	public ResourceLocation getTexture() 
	{
		return new ResourceLocation(Reference.ID, "textures/gui/manual/cover_" + cover + ".png");
	}
	
	@Override
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
		return false;
	}
	
	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
	}
	
	@Override
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		for(TextEntry entry : entries)
			entry.draw(gui, gui.getFontRenderer());
	}
}
