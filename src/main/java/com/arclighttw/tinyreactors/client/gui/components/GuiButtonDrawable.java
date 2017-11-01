package com.arclighttw.tinyreactors.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonDrawable extends GuiButtonMulti
{
	ResourceLocation texture;
	int textureU, textureV;
	int textureHoverU, textureHoverV;
	int disableTextureU, disableTextureV;
	
	public GuiButtonDrawable(int buttonId, int x, int y, ResourceLocation texture, int u, int v, int width, int height)
	{
		super(buttonId, x, y, width, height, "");
		
		this.texture = texture;
		textureU = u;
		textureV = v;
		
		setHoverTextureLocation(u, v);
		setDisabledTextureLocation(u, v);
	}
	
	public GuiButtonDrawable setHoverTextureLocation(int hoverU, int hoverV)
	{
		textureHoverU = hoverU;
		textureHoverV = hoverV;
		return this;
	}

	public GuiButtonDrawable setDisabledTextureLocation(int disabledU, int disabledV)
	{
		disableTextureU = disabledU;
		disableTextureV = disabledV;
		return this;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(!visible)
			return;
			
		mc.getTextureManager().bindTexture(texture);
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		if(enabled)
			drawTexturedModalRect(x, y, hovered ? textureHoverU : textureU, hovered ? textureHoverV : textureV, width, height);
		else
			drawTexturedModalRect(x, y, disableTextureU, disableTextureV, width, height);
		
		mouseDragged(mc, mouseX, mouseY);
    }
}
