package com.arclighttw.tinyreactors.client.gui.components;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonMulti extends GuiButton
{
	public int mouseButton;
	public List<String> label, labelShift;
	
	public GuiButtonMulti(int buttonId, int x, int y, String buttonText)
	{
		this(buttonId, x, y, 200, 20, buttonText);
	}
	
	public GuiButtonMulti(int buttonId, int x, int y, int width, int height, String buttonText)
	{
		super(buttonId, x, y, width, height, buttonText);
	}
	
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY, int mouseButton)
	{
		boolean pressed = enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		this.mouseButton = pressed ? mouseButton : -1;
		return pressed;
	}
	
	public GuiButtonMulti setLabel(String label)
	{
		return setLabel(Arrays.asList(label));
	}
	
	public GuiButtonMulti setLabel(List<String> label)
	{
		this.label = label;
		return this;
	}
	
	public GuiButtonMulti setShiftLabel(String label)
	{
		return setShiftLabel(Arrays.asList(label));
	}
	
	public GuiButtonMulti setShiftLabel(List<String> label)
	{
		this.labelShift = label;
		return this;
	}
	
	public List<String> getLabel(boolean shift)
	{
		return shift ? labelShift : label;
	}
}
