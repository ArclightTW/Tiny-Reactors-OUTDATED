package com.arclighttw.tinyreactors.client.gui.components;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ResourceLocation;

public class TextureMapping
{
	private final ResourceLocation enabledTexture;
	private List<String> enabledTooltip;
	private final int enabledU;
	private final int enabledV;
	
	private final ResourceLocation disabledTexture;
	private List<String> disabledTooltip;
	private final int disabledU;
	private final int disabledV;
	
	private final ResourceLocation hoveredTexture;
	private final int hoveredU;
	private final int hoveredV;
	
	public TextureMapping(ResourceLocation texture, int u, int v)
	{
		this(texture, u, v, u, v, u, v);
	}
	
	public TextureMapping(ResourceLocation texture, int enabledU, int enabledV, int disabledU, int disabledV)
	{
		this(texture, enabledU, enabledV, texture, disabledU, disabledV, texture, -1, -1);
	}
	
	public TextureMapping(ResourceLocation texture, int enabledU, int enabledV, int disabledU, int disabledV, int hoveredU, int hoveredV)
	{
		this(texture, enabledU, enabledV, texture, disabledU, disabledV, texture, hoveredU, hoveredV);
	}
	
	public TextureMapping(ResourceLocation enabledTexture, int enabledU, int enabledV, ResourceLocation disabledTexture, int disabledU, int disabledV, ResourceLocation hoveredTexture, int hoveredU, int hoveredV)
	{
		this.enabledTexture = enabledTexture;
		this.enabledU = enabledU;
		this.enabledV = enabledV;
		
		this.disabledTexture = disabledTexture;
		this.disabledU = disabledU;
		this.disabledV = disabledV;
		
		this.hoveredTexture = hoveredTexture;
		this.hoveredU = hoveredU;
		this.hoveredV = hoveredV;
	}
	
	public <T extends TextureMapping> T setEnabledTooltip(String tooltip)
	{
		return setEnabledTooltip(Arrays.asList(tooltip));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends TextureMapping> T setEnabledTooltip(List<String> tooltip)
	{
		enabledTooltip = tooltip;
		return (T)this;
	}
	
	public <T extends TextureMapping> T setDisabledTooltip(String tooltip)
	{
		return setDisabledTooltip(Arrays.asList(tooltip));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends TextureMapping> T setDisabledTooltip(List<String> tooltip)
	{
		disabledTooltip = tooltip;
		return (T)this;
	}
	
	public ResourceLocation getEnabledTexture()
	{
		return enabledTexture;
	}
	
	public int getEnabledU()
	{
		return enabledU;
	}
	
	public int getEnabledV()
	{
		return enabledV;
	}
	
	public List<String> getEnabledTooltip()
	{
		return enabledTooltip;
	}
	
	public ResourceLocation getDisabledTexture()
	{
		return disabledTexture;
	}
	
	public int getDisabledU()
	{
		return disabledU;
	}
	
	public int getDisabledV()
	{
		return disabledV;
	}
	
	public List<String> getDisabledTooltip()
	{
		return disabledTooltip;
	}
	
	public ResourceLocation getHoveredTexture()
	{
		return hoveredTexture;
	}
	
	public int getHoveredU()
	{
		return hoveredU;
	}
	
	public int getHoveredV()
	{
		return hoveredV;
	}
}
