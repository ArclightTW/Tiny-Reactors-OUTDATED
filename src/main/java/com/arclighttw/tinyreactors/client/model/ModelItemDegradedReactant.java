package com.arclighttw.tinyreactors.client.model;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.arclighttw.tinyreactors.inits.TRBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

@SuppressWarnings("deprecation")
public class ModelItemDegradedReactant implements IBakedModel
{
	private IBakedModel originalModel;
	private String registryName;
	
	public ModelItemDegradedReactant(IBakedModel originalModel, String registryName)
	{
		this.originalModel = originalModel;
		this.registryName = registryName;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(TRBlocks.REACTOR_CASING.getDefaultState());
		
		if(registryName != null)
		{
			Block block = Block.REGISTRY.getObject(new ResourceLocation(registryName));
			model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(block.getDefaultState());
		}
		
		return model.getQuads(state, side, rand);
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
	{
		if(originalModel instanceof IBakedModel)
		{
			Pair<? extends IBakedModel, Matrix4f> pair = ((IBakedModel)originalModel).handlePerspective(cameraTransformType);
			return Pair.of(this, pair.getRight());
		}
		
		ItemCameraTransforms transforms = originalModel.getItemCameraTransforms();
		ItemTransformVec3f transform = transforms.getTransform(cameraTransformType);
		TRSRTransformation tr = new TRSRTransformation(transform);
		Matrix4f matrix = null;
		
		if(tr != null)
			matrix = tr.getMatrix();
		
		return Pair.of(this, matrix);
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
}
