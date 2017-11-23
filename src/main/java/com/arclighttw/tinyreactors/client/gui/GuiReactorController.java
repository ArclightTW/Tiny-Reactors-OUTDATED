package com.arclighttw.tinyreactors.client.gui;

import java.io.IOException;
import java.util.Arrays;

import com.arclighttw.tinyreactors.client.gui.components.GuiButtonToggleable;
import com.arclighttw.tinyreactors.client.gui.components.TextureMapping;
import com.arclighttw.tinyreactors.container.ContainerReactorController;
import com.arclighttw.tinyreactors.helpers.TranslationHelper;
import com.arclighttw.tinyreactors.helpers.UIHelper;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.network.SMessageReactorController;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiReactorController extends GuiContainerBase
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactor_controller.png");
	
	private final TileEntityReactorController controller;
	
	private GuiButtonToggleable buttonActivate, buttonWarming;
	
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
		
		addButton(buttonActivate = new GuiButtonToggleable(this, 0, guiLeft + 8, guiTop + 60, 12, 12, new TextureMapping[] {
				new TextureMapping(TEXTURE, 189, 56, 177, 78, 189, 67).setEnabledTooltip("button.tinyreactors.reactor.activate").setActionListener((message) -> {
					((SMessageReactorController)message).setActive(true);
				}),
				new TextureMapping(TEXTURE, 177, 56, 177, 78, 177, 67).setEnabledTooltip("button.tinyreactors.reactor.deactivate").setActionListener((message) -> {
					((SMessageReactorController)message).setActive(false);
				})
		}).setDisabledTooltip("button.tinyreactors.reactor.cannotActivate"));
		
		addButton(buttonWarming = new GuiButtonToggleable(this, 1, guiLeft + 20, guiTop + 60, 12, 12, new TextureMapping[] {
				// Enabled (On) , Disabled (Off), Hovered (On)
				new TextureMapping(TEXTURE, 212, 56, 200, 78, 212, 67).setEnabledTooltip("button.tinyreactors.reactor.warm").setActionListener((message) -> {
					((SMessageReactorController)message).setWarming(true);
				}),
				new TextureMapping(TEXTURE, 200, 56, 200, 78, 200, 67).setEnabledTooltip("button.tinyreactors.reactor.dewarm").setActionListener((message) -> {
					((SMessageReactorController)message).setWarming(false);
				})
		}).setDisabledTooltip("button.tinyreactors.reactor.cannotWarm"));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException
	{
		SMessageReactorController message = new SMessageReactorController(controller);
		((GuiButtonToggleable)button).actionPerformed(message);
		TinyReactors.instance.network.sendToServer(message);
	}
	
	@Override
	public void updateScreen()
	{
		buttonActivate.setButton(controller.getStructure().isValid() && !controller.isWarming(), controller.isActive() ? 1 : 0);
		buttonWarming.setButton(controller.getStructure().isValid() && !controller.isActive(), controller.isWarming() ? 1 : 0);
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
		UIHelper.smallFontRenderer.drawString(TranslationHelper.translate(
				String.format("gui.tinyreactors.controller.%s", controller.getStructure().isValid() ? (controller.isActive() ? "active" : controller.isWarming() ? "warming" : "inactive") : "invalid")
			), 8, 8, 0xFFFFFF);
		
		if(controller.isActive())
		{
			UIHelper.smallFontRenderer.drawString(String.format(
					"%s: %,d RF/t", TranslationHelper.translate("gui.tinyreactors.controller.producing"), controller.getStructure().getEnergyProduced()
				), 8, 16, 0xFFFFFF);
			
			UIHelper.smallFontRenderer.drawString(String.format(
					"%s: %,.2f x", TranslationHelper.translate("gui.tinyreactors.controller.multiplier"), controller.getStructure().getTemperature().getMultiplier()
				), 8, 24, 0xFFFFFF);
		}
		
		for(GuiButton button : buttonList)
		{
			if(button.isMouseOver())
				button.drawButtonForegroundLayer(mouseX - guiLeft, mouseY - guiTop);
		}
		
		if(mouseX >= guiLeft + 133 && mouseX <= guiLeft + 133 + 16 && mouseY >= guiTop + 8 && mouseY <= guiTop + 8 + 64)
			drawHoveringText(Arrays.asList(
					String.format("%s: %,.1f°C", TranslationHelper.translate("gui.tinyreactors.temperature.current"), controller.getStructure().getTemperature().getCurrentTemperature()),
					String.format("%s: %,.1f°C", TranslationHelper.translate("gui.tinyreactors.temperature.limit"), controller.getStructure().getTemperature().getMaxTemperature())
				), mouseX - guiLeft, mouseY - guiTop);
		
		if(mouseX >= guiLeft + 153 && mouseX <= guiLeft + 153 + 16 && mouseY >= guiTop + 8 && mouseY <= guiTop + 12 + 60)
			drawHoveringText(Arrays.asList(
					String.format("%s: %,d RF", TranslationHelper.translate("gui.tinyreactors.energy.current"), controller.getStructure().getEnergy().getEnergyStored()),
					String.format("%s: %,d RF", TranslationHelper.translate("gui.tinyreactors.energy.capacity"), controller.getStructure().getEnergy().getMaxEnergyStored())
				), mouseX - guiLeft, mouseY - guiTop);
	}
}
