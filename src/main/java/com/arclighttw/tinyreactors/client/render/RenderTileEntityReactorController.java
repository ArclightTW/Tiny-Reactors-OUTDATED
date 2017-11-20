package com.arclighttw.tinyreactors.client.render;

import org.lwjgl.opengl.GL11;

import com.arclighttw.tinyreactors.blocks.BlockReactorComponentDirectional;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.arclighttw.tinyreactors.util.Util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityReactorController extends TileEntitySpecialRenderer<TileEntityReactorController>
{
	float ticks;
	
	@Override
	public void render(TileEntityReactorController tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(tile == null || !tile.hasWorld())
			return;
		
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		EnumFacing facing = null;
		
		try
		{
			facing = state.getValue(BlockReactorComponentDirectional.FACING);
		}
		catch(IllegalArgumentException ex)
		{
			return;
		}
		
		if(facing == null)
			return;
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		switch(facing)
		{
		case SOUTH:
			GlStateManager.translate(0, 0, -0.001F);
			break;
		case NORTH:
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.translate(-1, 0, -1.001F);
			break;
		case EAST:
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.translate(-1, 0, -0.001F);
			break;
		case WEST:
			GlStateManager.rotate(270, 0, 1, 0);
			GlStateManager.translate(0, 0, -1.001F);
			break;
		default:
			break;
		}
		
		GlStateManager.pushMatrix();
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		renderPanel(tile, TRBlocks.REACTOR_CONTROLLER_1.getDefaultState(), wr);
		tess.draw();
		
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}
	
	void renderPanel(TileEntityReactorController tile, IBlockState state, BufferBuilder wr)
	{
		TextureAtlasSprite icon = Util.getTexture(state);
		
		double minU = (double)icon.getMinU();
        double maxU = (double)icon.getMaxU();
        double minV = (double)icon.getMinV();
        double maxV = (double)icon.getMaxV();
        
        int xPos = 8;
        int yPos = 14;
        
        double u = minU + (maxU - minU) * xPos / 16F;
        double v = minV + (maxV - minV) * yPos / 16F;
        
		wr.pos(0.44F, 0.32F, 1F).tex(u, v).normal(0, 1, 0).endVertex();
        wr.pos(0.93F, 0.32F, 1F).tex(u, v).normal(0, 1, 0).endVertex();
        wr.pos(0.93F, 0.81F, 1F).tex(u, v).normal(0, 1, 0).endVertex();
        wr.pos(0.44F, 0.81F, 1F).tex(u, v).normal(0, 1, 0).endVertex();
        
        xPos = 8;
        yPos = 12;
        
        u = minU + (maxU - minU) * xPos / 16F;
        v = minV + (maxV - minV) * yPos / 16F;
        
        float width = (0.94F - 0.06F) * (Util.getEnergyFilledScaled(tile, 100) / 100F);
        
		wr.pos(0.06F, 0.81F, 1.001F).tex(u, v).normal(0, 1, 0).endVertex();
        wr.pos(0.06F + width, 0.81F, 1.001F).tex(u, v).normal(0, 1, 0).endVertex();
        wr.pos(0.06F + width, 0.875F, 1.001F).tex(u, v).normal(0, 1, 0).endVertex();
        wr.pos(0.06F, 0.875F, 1.001F).tex(u, v).normal(0, 1, 0).endVertex();
	}
}
