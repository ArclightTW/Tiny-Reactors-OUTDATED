package com.arclighttw.tinyreactors.util;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.arclighttw.tinyreactors.storage.EnergyStorageRF;
import com.arclighttw.tinyreactors.storage.TemperatureStorage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.InterfaceList({
	@Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyHandler", modid = "redstoneflux")
})
public class Util
{
	public static int getEnergyFilledScaled(EnergyStorageRF energy, int scale)
	{
		if(energy.getMaxEnergyStored() == 0)
			return 0;
		
		return (int)(scale * (float)energy.getEnergyStored() / (float)energy.getMaxEnergyStored());
	}
	
	public static int getEnergyFilledScaled(IEnergyStorage energy, int scale)
	{
		if(energy.getMaxEnergyStored() == 0)
			return 0;
		
		return (int)(scale * (float)energy.getEnergyStored() / (float)energy.getMaxEnergyStored());
	}
	
	public static int getTemperatureScaled(TemperatureStorage temperature, int scale)
	{
		if(temperature.getMaximumTemperature() == 0)
			return 0;
		
		return (int)(scale * temperature.getCurrentTemperature() / temperature.getMaximumTemperature());
	}
	
	public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dz = z1 - z2;
		return Math.sqrt((dx * dx + dy * dy + dz * dz));
	}
	
	public static double getDistanceAtoB(double x1, double z1, double x2, double z2)
	{
		double dx = x1 - x2;
		double dz = z1 - z2;
		return Math.sqrt((dx * dx + dz * dz));
	}

	public static double getDistanceAtoB(Vec3d pos1, Vec3d pos2)
	{
		return getDistanceAtoB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
	}
	
	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite getTexture(IBlockState state)
	{
		if(state == null)
			return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
	}
	
	@SideOnly(Side.CLIENT)
	public static void drawTextInWorld(String text, float scale, int x, int y, EnumTextAlignment alignment)
	{
		drawTextInWorld(Arrays.asList(text), scale, x, y, alignment);
	}
	
	@SideOnly(Side.CLIENT)
	public static void drawTextInWorld(List<String> lines, float scale, int x, int y, EnumTextAlignment alignment)
	{
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		float f1 = 0.016666668F * scale;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(1F, 1F, 0F);
		GL11.glTranslatef(-x / 256F, -y / 256F, 0F);
		
		GL11.glScalef(-f1, -f1, f1);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		
		int maxWidth = -1;
		
		for(String line : lines)
		{
			int width = font.getStringWidth(line);
			
			if(maxWidth < width)
				maxWidth = width;
		}
		
		int xPos = alignment == EnumTextAlignment.LEFT ? 0 : alignment == EnumTextAlignment.CENTER ? -1 : -maxWidth;
		int yPos = y;
		
		for(String line : lines)
		{
			font.drawString(line, xPos == -1 ? -font.getStringWidth(line) / 2 : xPos, yPos, 553648127);
			yPos += 10;
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		
		yPos = y;
		
		for(String line : lines)
		{
			font.drawString(line, xPos == -1 ? -font.getStringWidth(line) / 2 : xPos, yPos, -1);
			yPos += 10;
		}
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glPopMatrix();
	}
	
	@SideOnly(Side.CLIENT)
	public enum EnumTextAlignment
	{
		LEFT,
		CENTER,
		RIGHT
	}
}
