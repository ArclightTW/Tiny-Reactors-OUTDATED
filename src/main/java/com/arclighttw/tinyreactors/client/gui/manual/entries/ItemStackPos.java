package com.arclighttw.tinyreactors.client.gui.manual.entries;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

public class ItemStackPos
{
	private final ItemStack itemstack;
	
	private final int x;
	private final int y;
	
	public ItemStackPos(ItemStack itemstack, int x, int y)
	{
		this.itemstack = itemstack;
		
		this.x = x;
		this.y = y;
	}
	
	public void draw(GuiTinyManual gui)
	{
		gui.drawItemStack(itemstack, gui.getGuiLeft() + x, gui.getGuiTop() + y, "");
	}
	
	public void drawTooltip(GuiTinyManual gui, int mouseX, int mouseY)
	{
		if(mouseX - gui.getGuiLeft() < x || mouseX - gui.getGuiLeft() > x + 16 || mouseY - gui.getGuiTop() < y || mouseY - gui.getGuiTop() > y + 16)
			return;
		
		gui.drawHoveringText(itemstack.getTooltip(null, new ITooltipFlag() {
			@Override
			public boolean isAdvanced()
			{
				return false;
			}
		}), mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
	}
}
