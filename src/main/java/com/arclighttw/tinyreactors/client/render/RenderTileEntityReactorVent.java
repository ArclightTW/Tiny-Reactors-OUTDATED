package com.arclighttw.tinyreactors.client.render;

import org.lwjgl.opengl.GL11;

import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorVent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityReactorVent extends TileEntitySpecialRenderer<TileEntityReactorVent>
{
	private static final ResourceLocation CLOSED = new ResourceLocation(Reference.ID, "textures/blocks/reactor_vent_closed.png");
	private static final ResourceLocation OPEN = new ResourceLocation(Reference.ID, "textures/blocks/reactor_vent_open.png");
	
	private static final ResourceLocation CASING = new ResourceLocation(Reference.ID, "textures/blocks/reactor_casing.png");
	
	@Override
	public void render(TileEntityReactorVent tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(tile == null || !tile.hasWorld())
			return;
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		GlStateManager.pushMatrix();
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if(tile.isOperational())
		{
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
			Minecraft.getMinecraft().getTextureManager().bindTexture(OPEN);			
			wr.pos(0, 0, 1).tex(0, 0).normal(0, 1, 0).endVertex();
			wr.pos(1, 0, 1).tex(1, 0).normal(0, 1, 0).endVertex();
			wr.pos(1, 0, 0).tex(1, 1).normal(0, 1, 0).endVertex();
			wr.pos(0, 0, 0).tex(0, 1).normal(0, 1, 0).endVertex();
			tess.draw();
		}
		else
		{
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
			Minecraft.getMinecraft().getTextureManager().bindTexture(CLOSED);
			wr.pos(01 / 16F, 0.999F, 15 / 16F).tex(01 / 16F, 15 / 16F).normal(0, 1, 0).endVertex();
			wr.pos(15 / 16F, 0.999F, 15 / 16F).tex(15 / 16F, 15 / 16F).normal(0, 1, 0).endVertex();
			wr.pos(15 / 16F, 0.999F, 01 / 16F).tex(15 / 16F, 01 / 16F).normal(0, 1, 0).endVertex();
			wr.pos(01 / 16F, 0.999F, 01 / 16F).tex(01 / 16F, 01 / 16F).normal(0, 1, 0).endVertex();
			tess.draw();
			
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
			Minecraft.getMinecraft().getTextureManager().bindTexture(CASING);
			wr.pos(0, 1, 0).tex(0, 0).normal(0, 1, 0).endVertex();
			wr.pos(1, 1, 0).tex(1, 0).normal(0, 1, 0).endVertex();
			wr.pos(1, 1, 1).tex(1, 1).normal(0, 1, 0).endVertex();
			wr.pos(0, 1, 1).tex(0, 1).normal(0, 1, 0).endVertex();
			tess.draw();
		}
		
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}
}
