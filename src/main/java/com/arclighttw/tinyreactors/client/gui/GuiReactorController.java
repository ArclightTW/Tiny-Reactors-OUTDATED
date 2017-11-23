package com.arclighttw.tinyreactors.client.gui;

import java.io.IOException;
import java.util.Arrays;

import com.arclighttw.tinyreactors.client.gui.components.GuiButtonToggleable;
import com.arclighttw.tinyreactors.client.gui.components.TextureMapping;
import com.arclighttw.tinyreactors.container.ContainerReactorController;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiReactorController extends GuiContainer
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactor_controller.png");
	
	private final TileEntityReactorController controller;
	
	private GuiButtonToggleable buttonActivate;
	
	public GuiReactorController(EntityPlayer player, TileEntityReactorController controller)
	{
		super(new ContainerReactorController(player, controller));
		this.controller = controller;
		
		xSize = 177;
		ySize = 163;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		addButton(buttonActivate = new GuiButtonToggleable(0, guiLeft + 8, guiTop + 60, 12, 12, new TextureMapping[] {
				new TextureMapping(TEXTURE, 189, 56, 177, 78, 189, 67).setEnabledTooltip("button.tinyreactors.reactor.activate"),
				new TextureMapping(TEXTURE, 177, 56, 177, 78, 177, 67).setEnabledTooltip("button.tinyreactors.reactor.deactivate")
		}).setDisabledTooltip("button.tinyreactors.reactor.cannotActivate"));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException
	{
	}
	
	@Override
	public void updateScreen()
	{
		buttonActivate.setButton(controller.getStructure().isValid(), controller.isActive() ? 1 : 0);
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int energy = (int)((controller.getStructure().getEnergy().getEnergyStored() / (float)controller.getStructure().getEnergy().getMaxEnergyStored()) * 56);
		drawTexturedModalRect(guiLeft + 155, guiTop + 68 - energy, 177, 56 - energy, 8, energy);
		
		int temperature = (int)((controller.getStructure().getTemperature().getCurrentTemperature() / (float)controller.getStructure().getTemperature().getMaxTemperature()) * 56);
		drawTexturedModalRect(guiLeft + 134, guiTop + 68 - temperature - 1, 185, 0, 14, 3);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		for(GuiButton button : buttonList)
		{
			if(button.isMouseOver())
				button.drawButtonForegroundLayer(mouseX - guiLeft, mouseY - guiTop);
		}
		
		if(mouseX >= guiLeft + 133 && mouseX <= guiLeft + 133 + 16 && mouseY >= guiTop + 8 && mouseY <= guiTop + 8 + 64)
			drawHoveringText(Arrays.asList(
					String.format("Temperature: %,.1f°C", controller.getStructure().getTemperature().getCurrentTemperature()),
					String.format("Limit: %,.1f°C", controller.getStructure().getTemperature().getMaxTemperature())
				), mouseX - guiLeft, mouseY - guiTop);
		
		if(mouseX >= guiLeft + 153 && mouseX <= guiLeft + 153 + 16 && mouseY >= guiTop + 8 && mouseY <= guiTop + 12 + 60)
			drawHoveringText(Arrays.asList(
					String.format("Energy: %,d RF", controller.getStructure().getEnergy().getEnergyStored()),
					String.format("Capacity: %,d RF", controller.getStructure().getEnergy().getMaxEnergyStored())
				), mouseX - guiLeft, mouseY - guiTop);
	}
}
