package com.arclighttw.tinyreactors.client.gui.components;

import java.util.List;

import com.arclighttw.tinyreactors.helpers.TranslationHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonToggleable extends GuiButtonImage
{
	private final TextureMapping[] states;
	private int state;
	
	public GuiButtonToggleable(int id, int x, int y, int width, int height, TextureMapping... states)
	{
		super(id, x, y, null, -1, -1, width, height);
		this.states = states;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(!visible)
			return;
		
		if(states.length == 0)
			return;
		
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		
		if(state >= states.length)
			state = 0;
		
		TextureMapping mapping = states[state];
		
		mc.getTextureManager().bindTexture(enabled ? (hovered ? mapping.getHoveredTexture() : mapping.getEnabledTexture()) : mapping.getDisabledTexture());
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		drawTexturedModalRect(x, y, enabled ? (hovered ? mapping.getHoveredU() : mapping.getEnabledU()) : mapping.getDisabledU(), enabled ? (hovered ? mapping.getHoveredV() : mapping.getEnabledV()) : mapping.getDisabledV(), width, height);
		mouseDragged(mc, mouseX, mouseY);
	}
	
	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{
		if(states.length == 0)
		{
			super.drawButtonForegroundLayer(mouseX, mouseY);
			return;
		}
		
		if(state >= states.length)
			state = 0;
		
		TextureMapping mapping = states[state];
		List<String> tooltip = enabled ? mapping.getEnabledTooltip() : mapping.getDisabledTooltip();
		
		if(tooltip == null)
		{
			super.drawButtonForegroundLayer(mouseX, mouseY);
			return;
		}
		
		for(String line : tooltip)
			Minecraft.getMinecraft().currentScreen.drawHoveringText(TranslationHelper.translate(line), mouseX, mouseY);
	}
	
	public void setButton(boolean enabled, int state)
	{
		setState(state);
		this.enabled = enabled;
	}
	
	public void setState(int state)
	{
		if(state >= states.length)
			state = 0;
		
		this.state = state;
	}
}
