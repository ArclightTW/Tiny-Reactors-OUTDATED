package com.arclighttw.tinyreactors.client.gui;

import java.io.IOException;
import java.util.List;

import com.arclighttw.tinyreactors.client.gui.components.GuiButtonMulti;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class GuiContainerMulti extends GuiContainer
{
	public GuiContainerMulti(Container container)
	{
		super(container);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		for(GuiButton button : buttonList)
		{
			if(!(button instanceof GuiButtonMulti))
				continue;
				
			GuiButtonMulti guiButton = (GuiButtonMulti)button;
			
			if(guiButton.mousePressed(mc, mouseX, mouseY, mouseButton))
			{
				selectedButton = guiButton;
				guiButton.playPressSound(mc.getSoundHandler());
				actionPerformed(guiButton);
				return;
			}
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		for(GuiButton button : buttonList)
		{
			if(!(button instanceof GuiButtonMulti))
				continue;
				
			GuiButtonMulti guiButton = (GuiButtonMulti)button;
			
			if(!guiButton.visible || !guiButton.isMouseOver())
				continue;
			
			List<String> label = guiButton.getLabel(isShiftKeyDown());
			
			if(label == null)
				return;
			
			drawHoveringText(label, mouseX - guiLeft, mouseY - guiTop);
		}
	}
	
	public void drawItemStack(ItemStack stack, int x, int y, String altText)
	{
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		
		FontRenderer font = stack.getItem().getFontRenderer(stack);

		if(font == null)
			font = fontRenderer;
		
		itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (stack.isEmpty() ? 0 : 8), altText);
		zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
    }
}
