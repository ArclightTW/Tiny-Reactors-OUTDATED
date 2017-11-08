package com.arclighttw.tinyreactors.client.gui.manual.entries;

import java.util.List;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class RecipeEntry
{
	private final int x;
	private final int y;
	
	private final int size;
	private final boolean shaped;
	
	private final List<ItemStackPos> stacks;
	
	public RecipeEntry(int x, int y, int size, boolean shaped, ItemStack output, List<ItemStack> inputs)
	{
		this.x = x;
		this.y = y;
		
		this.size = size;
		this.shaped = shaped;
		
		stacks = Lists.newArrayList();
		
		switch(size)
		{
		case 2:
			stacks.add(new ItemStackPos(output, x + 77, y + 10));
			
			for(int i = 0; i < inputs.size(); i++)
			{
				int localX = i % 2;
				int localY = i / 2;
				stacks.add(new ItemStackPos(inputs.get(i), x + 1 + (18 * localX), y + 1 + (18 * localY)));
			}
			
			break;
		case 3:
			stacks.add(new ItemStackPos(output, x + 95, y + 19));
			
			for(int i = 0; i < inputs.size(); i++)
			{
				int localX = i % 3;
				int localY = i / 3;
				stacks.add(new ItemStackPos(inputs.get(i), x + 1 + (18 * localX), y + 1 + (18 * localY)));
			}
			
			break;
		}
	}
	
	public void drawBackground(GuiTinyManual gui, int guiLeft, int guiTop)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTinyManual.WIDGETS);
		
		switch(size)
		{
		case 2:
			gui.drawTexturedModalRect(guiLeft + x, guiTop + y, 35, 0, 36, 36);
			gui.drawTexturedModalRect(guiLeft + x + 36, guiTop + y + 5, 89, 14, 62, 26);
			gui.drawTexturedModalRect(guiLeft + x + (shaped ? 43 : 46), guiTop + y + 10, 0, shaped ? 26 : 41, shaped ? 22 : 16, shaped ? 15 : 14);
			break;
		case 3:
			gui.drawTexturedModalRect(guiLeft + x, guiTop + y, 35, 0, 116, 54);
			gui.drawTexturedModalRect(guiLeft + x + (shaped ? 61 : 64), guiTop + y + 19, 0, shaped ? 26 : 41, shaped ? 22 : 16, shaped ? 15 : 14);
			break;
		}
		
		for(ItemStackPos stack : stacks)
			stack.draw(gui);
	}
	
	public void drawForeground(GuiTinyManual gui, int mouseX, int mouseY)
	{
		for(ItemStackPos stack : stacks)
			stack.drawTooltip(gui, mouseX, mouseY);
	}
}
