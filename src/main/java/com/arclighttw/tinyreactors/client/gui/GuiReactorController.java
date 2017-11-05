package com.arclighttw.tinyreactors.client.gui;

import java.io.IOException;
import java.util.Arrays;

import com.arclighttw.tinyreactors.client.gui.components.GuiButtonDrawable;
import com.arclighttw.tinyreactors.client.gui.components.GuiButtonItemStack;
import com.arclighttw.tinyreactors.client.gui.components.GuiButtonMulti;
import com.arclighttw.tinyreactors.container.ContainerReactorController;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.network.SMessageReactorControllerActive;
import com.arclighttw.tinyreactors.network.SMessageReactorControllerPrewarm;
import com.arclighttw.tinyreactors.network.SMessageReactorControllerRedstone;
import com.arclighttw.tinyreactors.properties.EnumControllerTier;
import com.arclighttw.tinyreactors.properties.EnumRedstoneMode;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.util.Util;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiReactorController extends GuiContainerMulti
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactor_controller.png");
	
	private final TileEntityReactorController controller;
	
	private GuiButtonMulti buttonOff, buttonOn, buttonPrewarmingOff, buttonPrewarmingOn;
	private GuiButtonMulti buttonRedstoneIgnore, buttonRedstoneDisable, buttonRedstoneEnable;
	
	public GuiReactorController(TileEntityReactorController controller, InventoryPlayer player)
	{
		super(new ContainerReactorController(controller, player));
		this.controller = controller;
		
		xSize = 177;
		ySize = 163;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		addButton(buttonOn = new GuiButtonDrawable(0, guiLeft + 118, guiTop + 60, TEXTURE, 189, 56, 12, 12).setHoverTextureLocation(189, 67).setDisabledTextureLocation(177, 78).setLabel("Enable"));
		addButton(buttonOff = new GuiButtonDrawable(1, guiLeft + 118, guiTop + 60, TEXTURE, 177, 56, 12, 12).setHoverTextureLocation(177, 67).setDisabledTextureLocation(177, 78).setLabel("Disable"));
		
		addButton(buttonPrewarmingOn = new GuiButtonDrawable(6, guiLeft + 118, guiTop + 46, TEXTURE, 189, 56, 12, 12).setHoverTextureLocation(189, 67).setDisabledTextureLocation(177, 78).setLabel("Start Pre-Warming"));
		addButton(buttonPrewarmingOff = new GuiButtonDrawable(5, guiLeft + 118, guiTop + 46, TEXTURE, 177, 56, 12, 12).setHoverTextureLocation(177, 67).setDisabledTextureLocation(177, 78).setLabel("Stop Pre-Warming"));
		
		addButton(buttonRedstoneIgnore = new GuiButtonItemStack(2, guiLeft - 20, guiTop, TEXTURE, 177, 89, 20, 20, this).setIcon(new ItemStack(Blocks.BARRIER)).setLabel(Arrays.asList("Mode: Ignore", "Click to Cycle")));
		addButton(buttonRedstoneDisable = new GuiButtonItemStack(3, guiLeft - 20, guiTop, TEXTURE, 177, 89, 20, 20, this).setIcon(new ItemStack(Items.REDSTONE)).setLabel(Arrays.asList("Mode: Disable on Redstone", "Click to Cycle")));
		addButton(buttonRedstoneEnable = new GuiButtonItemStack(4, guiLeft - 20, guiTop, TEXTURE, 177, 89, 20, 20, this).setIcon(new ItemStack(Blocks.REDSTONE_TORCH)).setLabel(Arrays.asList("Mode: Enable on Redstone", "Click to Cycle")));
		
		updateScreen();
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException
	{
		switch(button.id)
		{
		case 0:
			TinyReactors.network.sendToServer(new SMessageReactorControllerActive(true, controller.getPos()));
			break;
		case 1:
			TinyReactors.network.sendToServer(new SMessageReactorControllerActive(false, controller.getPos()));
			break;
		case 2:
			TinyReactors.network.sendToServer(new SMessageReactorControllerRedstone(EnumRedstoneMode.DISABLE_ON_REDSTONE, controller.getPos()));
			break;
		case 3:
			TinyReactors.network.sendToServer(new SMessageReactorControllerRedstone(EnumRedstoneMode.ENABLE_ON_REDSTONE, controller.getPos()));
			break;
		case 4:
			TinyReactors.network.sendToServer(new SMessageReactorControllerRedstone(EnumRedstoneMode.IGNORE, controller.getPos()));
			break;
		case 5:
			TinyReactors.network.sendToServer(new SMessageReactorControllerPrewarm(false, controller.getPos()));
			break;
		case 6:
			TinyReactors.network.sendToServer(new SMessageReactorControllerPrewarm(true, controller.getPos()));
			break;
		}
		
		updateScreen();
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if(controller.isWarming())
		{
			buttonPrewarmingOff.visible = true;
			buttonPrewarmingOff.enabled = true;
			
			buttonPrewarmingOn.visible = false;
			buttonPrewarmingOn.enabled = false;
			
			buttonOff.visible = true;
			buttonOff.enabled = false;
			
			buttonOn.visible = false;
			buttonOn.enabled = false;
		}
		else
		{
			buttonPrewarmingOff.visible = false;
			buttonPrewarmingOff.enabled = false;
			
			buttonPrewarmingOn.visible = true;
			buttonPrewarmingOn.enabled = true;
			
			buttonOff.visible = true;
			buttonOff.enabled = false;
			
			buttonOn.visible = false;
			buttonOn.enabled = false;
			
			if(controller.getTemperature().hasOverheated())
			{
				buttonOff.visible = true;
				buttonOff.enabled = false;
				
				buttonOff.visible = false;
				buttonOff.enabled = false;
				
				buttonOn.visible = false;
				buttonOn.enabled = false;
				
				buttonPrewarmingOff.visible = true;
				buttonPrewarmingOff.enabled = false;
				
				buttonPrewarmingOn.visible = false;
				buttonPrewarmingOn.enabled = false;
			}
			else if(controller.getMultiblock().isValid())
			{
				buttonOff.visible = controller.isActive();
				buttonOff.enabled = buttonOff.visible;
				
				buttonOn.visible = !buttonOff.visible;
				buttonOn.enabled = buttonOn.visible;
				
				buttonPrewarmingOff.visible = controller.isActive();
				buttonPrewarmingOff.enabled = !controller.isActive();
				
				buttonPrewarmingOn.visible = !controller.isActive();
				buttonPrewarmingOn.enabled = true;
			}
			else
			{
				buttonOn.visible = true;
				buttonOn.enabled = false;
				
				buttonOff.visible = false;
				buttonOff.enabled = false;
				
				buttonPrewarmingOn.visible = true;
				buttonPrewarmingOn.enabled = false;
				
				buttonPrewarmingOff.visible = false;
				buttonPrewarmingOff.enabled = false;
			}
		}
		
		if(controller.getTier() == EnumControllerTier.I)
		{
			buttonRedstoneIgnore.visible = false;
			buttonRedstoneDisable.visible = false;
			buttonRedstoneEnable.visible = false;
		}
		else
		{
			switch(controller.getRedstoneMode())
			{
			case IGNORE:
				buttonRedstoneIgnore.visible = true;
				buttonRedstoneDisable.visible = false;
				buttonRedstoneEnable.visible = false;
				break;
			case DISABLE_ON_REDSTONE:
				buttonRedstoneIgnore.visible = false;
				buttonRedstoneDisable.visible = true;
				buttonRedstoneEnable.visible = false;
				break;
			case ENABLE_ON_REDSTONE:
				buttonRedstoneIgnore.visible = false;
				buttonRedstoneDisable.visible = false;
				buttonRedstoneEnable.visible = true;
				break;
			}
		}
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int progress = Util.getEnergyFilledScaled(controller, 56);
		drawTexturedModalRect(guiLeft + 155, guiTop + 68 - progress, 177, 56 - progress, 8, progress);
		
		int temperature = Util.getTemperatureScaled(controller.getTemperature(), 56);
		drawTexturedModalRect(guiLeft + 134, guiTop + 68 - temperature - 1, 185, 0, 14, 3);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if(controller.getTemperature().hasOverheated())
		{
			drawString(fontRenderer, "CRITICAL FAILURE", 5, 7, 0xFFFFFF);
			drawString(fontRenderer, "Reactor Cooling", 5, 17, 0xFFFFFF);
		}
		else if(controller.isActive())
		{
			if(controller.isPowered() && controller.getRedstoneMode() == EnumRedstoneMode.DISABLE_ON_REDSTONE)
			{
				drawString(fontRenderer, "Disabled", 5, 7, 0xFFFFFF);
				drawString(fontRenderer, "Reactor is Powered", 5, 17, 0xFFFFFF);
			}
			else if(!controller.isPowered() && controller.getRedstoneMode() == EnumRedstoneMode.ENABLE_ON_REDSTONE)
			{
				drawString(fontRenderer, "Disabled", 5, 7, 0xFFFFFF);
				drawString(fontRenderer, "Reactor is Unpowered", 5, 17, 0xFFFFFF);
			}
			else
			{
				drawString(fontRenderer, "Base Output:", 5, 7, 0xFFFFFF);
				drawString(fontRenderer, String.format("%,d RF/t", controller.getMultiblock().getAvailableYield()), 5, 17, 0xFFFFFF);
				
				drawString(fontRenderer, "Efficiency:", 5, 32, 0xFFFFFF);
				drawString(fontRenderer, String.format("%.0f", controller.getTemperature().getEfficiency() * 100) + " %", 5, 42, 0xFFFFFF);
				
				drawString(fontRenderer, "Producing:", 5, 57, 0xFFFFFF);
				drawString(fontRenderer, String.format("%,d RF/t", (int)(controller.getMultiblock().getAvailableYield() * controller.getTemperature().getEfficiency())), 5, 67, 0xFFFFFF);
			}
		}
		else if(controller.isWarming())
			drawString(fontRenderer, "Pre-Warming", 5, 7, 0xFFFFFF);
		else if(controller.getMultiblock().isValid())
			drawString(fontRenderer, "Inactive", 5, 7, 0xFFFFFF);
		else
			drawString(fontRenderer, "Invalid Structure", 5, 7, 0xFFFFFF);
		
		if(mouseX >= guiLeft + 153 && mouseX <= guiLeft + 153 + 16 && mouseY >= guiTop + 8 && mouseY <= guiTop + 12 + 60)
			drawHoveringText(Arrays.asList(
					String.format("Energy: %,d RF", controller.getEnergyStored()),
					String.format("Capacity: %,d RF", controller.getMaxEnergyStored())
				), mouseX - guiLeft, mouseY - guiTop);
		
		if(mouseX >= guiLeft + 133 && mouseX <= guiLeft + 133 + 16 && mouseY >= guiTop + 8 && mouseY <= guiTop + 8 + 64)
			drawHoveringText(Arrays.asList(
					String.format("Temperature: %,.1f C", (controller.getTemperature().getCurrentTemperature() * 10)),
					String.format("Limit: %,.1f C", (controller.getTemperature().getMaximumTemperature() * 10))
				), mouseX - guiLeft, mouseY - guiTop);
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}
