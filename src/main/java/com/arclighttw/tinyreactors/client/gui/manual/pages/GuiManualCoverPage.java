package com.arclighttw.tinyreactors.client.gui.manual.pages;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.arclighttw.tinyreactors.client.gui.manual.entries.TextEntry;
import com.arclighttw.tinyreactors.client.gui.manual.widgets.WidgetButton;
import com.arclighttw.tinyreactors.main.Reference;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiManualCoverPage extends GuiManualTextPage
{
	protected String cover;
	
	public GuiManualCoverPage(String id, JsonObject json)
	{
		super(id, json, false);
		this.cover = json.get("cover").getAsString();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		widgets.add(new WidgetButton(GuiTinyManual.WIDGETS, 156, 12, 20, 0, 11, 11, (gui) -> {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}));
	}
	
	@Override
	public ResourceLocation getTexture() 
	{
		return new ResourceLocation(Reference.ID, "textures/gui/manual/cover_" + cover + ".png");
	}
	
	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		super.drawBackground(gui, guiLeft, guiTop, mouseX, mouseY);
	}
	
	@Override
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		super.drawForeground(gui, mouseX, mouseY);
		
		for(TextEntry entry : textEntries)
			entry.draw(gui, gui.getFontRenderer());
	}
}
