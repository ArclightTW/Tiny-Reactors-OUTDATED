package com.arclighttw.tinyreactors.client.model;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class ModelDegradedReactant implements IBakedModel
{
	private IBakedModel originalModel;
	private String registryName;
	
	public ModelDegradedReactant(IBakedModel originalModel, String registryName)
	{
		this.originalModel = originalModel;
		this.registryName = registryName;
	}
	
	private List<BakedQuad> getModelQuads()
	{
		String atlasSprite = registryName == null ? "tinyreactors:blocks/reactor_casing" : "minecraft:blocks/diamond_block";
		TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(atlasSprite);

		return Arrays.asList(createBakedQuadForFace(0.5F, 1.0F, 0.5F, 1.0F, 0.5F, 0, texture, EnumFacing.SOUTH));
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if(side != null)
			return originalModel.getQuads(state, side, rand);
		
		return getModelQuads();
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return originalModel.getParticleTexture();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return originalModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return originalModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return originalModel.isBuiltInRenderer();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		throw new UnsupportedOperationException("The finalized model does not contain an ItemOverrideList");
	}
	
	private BakedQuad createBakedQuadForFace(float centreLR, float width, float centreUD, float height, float forwardDisplacement, int itemRenderLayer, TextureAtlasSprite texture, EnumFacing face)
	{
		float x1, x2, x3, x4;
		float y1, y2, y3, y4;
		float z1, z2, z3, z4;
		final float CUBE_MIN = 0.0F;
		final float CUBE_MAX = 1.0F;

		switch(face)
		{
		case UP:
			x1 = x2 = centreLR + width/2.0F;
			x3 = x4 = centreLR - width/2.0F;
			z1 = z4 = centreUD + height/2.0F;
			z2 = z3 = centreUD - height/2.0F;
			y1 = y2 = y3 = y4 = CUBE_MAX + forwardDisplacement;
			break;
		case DOWN:
			x1 = x2 = centreLR + width/2.0F;
			x3 = x4 = centreLR - width/2.0F;
			z1 = z4 = centreUD - height/2.0F;
			z2 = z3 = centreUD + height/2.0F;
			y1 = y2 = y3 = y4 = CUBE_MIN - forwardDisplacement;
			break;
		case WEST:
			z1 = z2 = centreLR + width/2.0F;
			z3 = z4 = centreLR - width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			x1 = x2 = x3 = x4 = CUBE_MIN - forwardDisplacement;
			break;
		case EAST:
			z1 = z2 = centreLR - width/2.0F;
			z3 = z4 = centreLR + width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			x1 = x2 = x3 = x4 = CUBE_MAX + forwardDisplacement;
			break;
		case NORTH:
			x1 = x2 = centreLR - width/2.0F;
			x3 = x4 = centreLR + width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			z1 = z2 = z3 = z4 = CUBE_MIN - forwardDisplacement;
			break;
		case SOUTH:
			x1 = x2 = centreLR + width/2.0F;
			x3 = x4 = centreLR - width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			z1 = z2 = z3 = z4 = CUBE_MAX + forwardDisplacement;
			break;
		default:
			assert false : "Unexpected facing in createBakedQuadForFace:" + face;
			return null;
		}

		return new BakedQuad(Ints.concat(
				vertexToInts(x1, y1, z1, Color.WHITE.getRGB(), texture, 16, 16),
				vertexToInts(x2, y2, z2, Color.WHITE.getRGB(), texture, 16, 0),
				vertexToInts(x3, y3, z3, Color.WHITE.getRGB(), texture, 0, 0),
				vertexToInts(x4, y4, z4, Color.WHITE.getRGB(), texture, 0, 16)
			),
			itemRenderLayer, face, texture, true, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM
		);
}
	
	private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
	  {
	    return new int[] {
	            Float.floatToRawIntBits(x),
	            Float.floatToRawIntBits(y),
	            Float.floatToRawIntBits(z),
	            color,
	            Float.floatToRawIntBits(texture.getInterpolatedU(u)),
	            Float.floatToRawIntBits(texture.getInterpolatedV(v)),
	            0
	    };
	  }
}
