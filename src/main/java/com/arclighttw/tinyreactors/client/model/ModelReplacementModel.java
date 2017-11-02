package com.arclighttw.tinyreactors.client.model;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class ModelReplacementModel implements IReplacementModel
{
	private IBakedModel baseModel;
	private ItemOverrideList overrideList;
	
	@Override
	public void applyExisting(IBakedModel model, ItemOverrideList list)
	{
		baseModel = model;
		overrideList = list;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return baseModel.getParticleTexture();
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		return baseModel.getQuads(state, side, rand);
	}
	
	@Override
	public ItemOverrideList getOverrides()
	{
		return overrideList;
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return baseModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return baseModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}
}
