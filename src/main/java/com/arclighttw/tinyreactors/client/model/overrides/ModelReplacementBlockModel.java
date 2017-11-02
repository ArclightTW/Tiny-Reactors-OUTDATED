package com.arclighttw.tinyreactors.client.model.overrides;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class ModelReplacementBlockModel implements IBakedModel
{
	private IBakedModel baseModel;
	private IModelBlock replacement;
	
	public void applyExisting(IBakedModel model, IModelBlock replacement)
	{
		baseModel = model;
		this.replacement = replacement;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return baseModel.getParticleTexture();
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		return replacement.getQuads(state, side, rand);
	}
	
	@Override
	public ItemOverrideList getOverrides()
	{
		return baseModel.getOverrides();
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
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		return replacement.handlePerspective(baseModel, cameraTransformType);
	}
}
