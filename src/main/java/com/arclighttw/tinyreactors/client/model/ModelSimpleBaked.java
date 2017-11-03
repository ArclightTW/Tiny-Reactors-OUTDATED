package com.arclighttw.tinyreactors.client.model;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class ModelSimpleBaked implements IBakedModel
{
	private IBakedModel existing;
	
	public ModelSimpleBaked()
	{
		this(null);
	}
	
	public ModelSimpleBaked(IBakedModel existing)
	{
		this.existing = existing;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		return existing == null ? Arrays.asList() : existing.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return existing == null ? false : existing.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return existing == null ? false : existing.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return existing == null ? false : existing.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return existing == null ? null : existing.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return existing == null ? null : existing.getOverrides();
	}	
}
