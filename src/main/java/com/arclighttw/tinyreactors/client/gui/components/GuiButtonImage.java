package com.arclighttw.tinyreactors.client.gui.components;

import java.util.Arrays;
import java.util.List;

import com.arclighttw.tinyreactors.helpers.TranslationHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImage extends GuiButton
{
	private final ResourceLocation texture;
	private final int textureX;
	private final int textureY;
	
	private List<String> enabledTooltip;
	private List<String> disabledTooltip;
	
	public GuiButtonImage(int id, int x, int y, ResourceLocation texture, int u, int v, int width, int height)
	{
		super(id, x, y, width, height, "");

		this.texture = texture;
		textureX = u;
		textureY = v;
	}
	
	public <T extends GuiButtonImage> T setEnabledTooltip(String tooltip)
	{
		return setEnabledTooltip(Arrays.asList(tooltip));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GuiButtonImage> T setEnabledTooltip(List<String> tooltip)
	{
		enabledTooltip = tooltip;
		return (T)this;
	}
	
	public <T extends GuiButtonImage> T setDisabledTooltip(String tooltip)
	{
		return setDisabledTooltip(Arrays.asList(tooltip));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GuiButtonImage> T setDisabledTooltip(List<String> tooltip)
	{
		disabledTooltip = tooltip;
		return (T)this;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(!visible)
			return;
		
		mc.getTextureManager().bindTexture(texture);
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		drawTexturedModalRect(x, y, textureX, textureY, width, height);
		mouseDragged(mc, mouseX, mouseY);
	}
	
	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{
		List<String> tooltip = enabled ? enabledTooltip : disabledTooltip;
		
		if(tooltip == null)
			return;
		
		for(String line : tooltip)
			Minecraft.getMinecraft().currentScreen.drawHoveringText(TranslationHelper.translate(line), mouseX, mouseY);
	}
}
