package com.arclighttw.tinyreactors.client.gui;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.arclighttw.tinyreactors.capabilities.IManualCapability;
import com.arclighttw.tinyreactors.capabilities.ManualCapability;
import com.arclighttw.tinyreactors.client.gui.components.GuiButtonDrawable;
import com.arclighttw.tinyreactors.client.gui.manual.pages.GuiManualPage;
import com.arclighttw.tinyreactors.client.gui.manual.pages.GuiManualTextPage;
import com.arclighttw.tinyreactors.container.ContainerTinyManual;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.managers.ResourceManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTinyManual extends GuiContainerMulti
{
	public static final ResourceLocation WIDGETS = new ResourceLocation(Reference.ID, "textures/gui/manual/widgets.png");

	private static Map<String, GuiManualPage> linkedPages;
	private static GuiManualPage[] pages;
	
	private final EntityPlayer player;
	
	private GuiButton pageBackward, pageForward;
	private int currentPage;
	
	public GuiTinyManual(EntityPlayer player)
	{
		super(new ContainerTinyManual(player));
		this.player = player;
		
		xSize = 180;
		ySize = 146;
		
		if(pages == null)
			refreshPageEntries(false);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(pages[currentPage].mouseClicked(this, mouseX, mouseY, mouseButton))
			return;
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void onResize(Minecraft mc, int w, int h)
	{
		saveCapability();
		super.onResize(mc, w, h);
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException
	{
		switch(button.id)
		{
		case 0:
			currentPage++;
			break;
		case 1:
			currentPage--;
			break;
		}
		
		calculateButtons();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		IManualCapability capability = player.getCapability(ManualCapability.CAPABILITY, null);
		currentPage = capability.getPageNumber();
		
		addButton(pageForward = new GuiButtonDrawable(0, guiLeft + 160, guiTop + 120, WIDGETS, 0, 0, 10, 8).setHoverTextureLocation(10, 0).setLabel("Next"));
		addButton(pageBackward = new GuiButtonDrawable(1, guiLeft + 14, guiTop + 120, WIDGETS, 0, 8, 10, 8).setHoverTextureLocation(10, 8).setLabel("Previous"));		
		calculateButtons();
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		saveCapability();
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(pages[currentPage].getTexture());
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		pages[currentPage].drawBackground(this, guiLeft, guiTop, mouseX, mouseY);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if(currentPage != 0 && currentPage != pages.length - 1)
			drawCenteredString(fontRenderer, String.format("Page %d / %d", currentPage, pages.length - 2), xSize / 2, ySize - 25, 0xFFFFFF);
		
		pages[currentPage].drawForeground(this, mouseX, mouseY);
	}
	
	public static void refreshPageEntries(boolean flag)
	{
		if(pages == null && flag)
			return;
		
		List<JsonObject> jsonPages = Lists.newArrayList();
		
		for(int i = 0; i < 100; i++)
		{
			JsonObject json = ResourceManager.loadResource(String.format("/assets/tinyreactors/manual/%s/page_%02d.json",
					Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode(), i
				));
			
			if(json != null)
				jsonPages.add(json);
		}
		
		if(jsonPages.size() == 0)
		{
			for(int i = 0; i < 100; i++)
			{
				JsonObject json = ResourceManager.loadResource(String.format("/assets/tinyreactors/manual/en_us/page_%02d.json", i));
				
				if(json != null)
					jsonPages.add(json);
			}
		}
		
		pages = new GuiManualPage[jsonPages.size()];
		linkedPages = Maps.newHashMap();
		
		for(int i = 0; i < pages.length; i++)
		{
			pages[i] = GuiManualPage.parse(jsonPages.get(i));
			pages[i].setPageIndex(i);
			
			if(pages[i].getIdentifier() != null)
				linkedPages.put(pages[i].getIdentifier(), pages[i]);
		}
		
		if(pages.length == 0)
		{
			JsonObject missing = new JsonObject();
			missing.addProperty("type", "text");
			
			JsonArray array = new JsonArray();
			
			JsonObject text = new JsonObject();
			text.addProperty("text", "Tiny Manual did not successfully load.  Please report this error to the mod author.");
			text.addProperty("border", 20);
			text.addProperty("color", "000000");
			text.addProperty("wrapped", true);
			
			array.add(text);
			missing.add("lines", array);
			
			pages = new GuiManualPage[] { new GuiManualTextPage("", missing) };
		}			
	}
	
	public FontRenderer getFontRenderer()
	{
		return fontRenderer;
	}
	
	public void attemptNavigation(String link)
	{
		if(link == null || !linkedPages.containsKey(link))
			return;
		
		GuiManualPage page = linkedPages.get(link);
		currentPage = page.getPageIndex();
		calculateButtons();
		
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	private void saveCapability()
	{
		IManualCapability capability = player.getCapability(ManualCapability.CAPABILITY, null);
		capability.setPageNumber(currentPage);
	}
	
	private void calculateButtons()
	{
		if(currentPage <= 0)
		{
			currentPage = 0;
			pageBackward.visible = false;
		}
		else if(currentPage >= pages.length - 1)
		{
			currentPage = pages.length - 1;
			pageForward.visible = false;
		}
		else
		{
			pageBackward.visible = true;
			pageForward.visible = true;
		}
	}
}
