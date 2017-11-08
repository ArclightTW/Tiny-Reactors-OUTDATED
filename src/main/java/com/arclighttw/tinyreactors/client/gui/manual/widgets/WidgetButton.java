package com.arclighttw.tinyreactors.client.gui.manual.widgets;

import java.util.function.Consumer;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class WidgetButton extends Widget
{
	private final ResourceLocation texture;
	
	private final int x;
	private final int y;
	
	private final int u;
	private final int v;
	
	private final int width;
	private final int height;
	
	private final Consumer<GuiTinyManual> clickAction;
	
	public WidgetButton(ResourceLocation texture, int x, int y, int u, int v, int width, int height, Consumer<GuiTinyManual> clickAction)
	{
		this.texture = texture;
		
		this.x = x;
		this.y = y;
		
		this.u = u;
		this.v = v;
		
		this.width = width;
		this.height = height;
		
		this.clickAction = clickAction;
	}
	
	@Override
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		gui.drawTexturedModalRect(guiLeft + x, guiTop + y, u, v, width, height);
	}
	
	@Override
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
	}
	
	@Override
	public boolean mouseClicked(GuiTinyManual gui, int mouseX, int mouseY, int mouseButton)
	{
		boolean clicked = mouseX - gui.getGuiLeft() >= x && mouseX - gui.getGuiLeft() <= x + width && mouseY - gui.getGuiTop() >= y && mouseY - gui.getGuiTop() <= y + height;
		
		if(clicked && clickAction != null)
			clickAction.accept(gui);
		
		return clicked;
	}
}
