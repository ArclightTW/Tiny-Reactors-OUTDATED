package com.arclighttw.tinyreactors.client.gui.components;

import com.arclighttw.tinyreactors.client.gui.GuiContainerMulti;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiButtonItemStack extends GuiButtonDrawable
{
	private GuiContainerMulti gui;
	private ItemStack icon;
	
	public GuiButtonItemStack(int buttonId, int x, int y, ResourceLocation texture, int u, int v, int width, int height, GuiContainerMulti gui)
	{
		super(buttonId, x, y, texture, u, v, width, height);
		this.gui = gui;
		
		setIcon(ItemStack.EMPTY);
	}
	
	public GuiButtonItemStack setIcon(ItemStack icon)
	{
		this.icon = icon;
		return this;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		super.drawButton(mc, mouseX, mouseY, partialTicks);
		
		if(visible)
			gui.drawItemStack(icon, x + (width / 2) - 8, y + (height / 2) - 8, "");
    }
}
