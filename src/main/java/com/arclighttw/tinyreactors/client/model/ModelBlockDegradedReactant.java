package com.arclighttw.tinyreactors.client.model;

import java.util.List;

import com.arclighttw.tinyreactors.blocks.BlockDegradedReactant;
import com.arclighttw.tinyreactors.inits.Registry.IRuntimeModel;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBlockDegradedReactant implements IRuntimeModel
{
	@Override
	public IBakedModel createModel(IBakedModel existing)
	{
		return new IBakedModel()
		{
			@Override
			public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
			{
				List<BakedQuad> oldQuads = existing.getQuads(state, side, rand);
				List<BakedQuad> newQuads = Lists.newArrayList();
				
				IBlockState representative = null;
				
				if(state instanceof IExtendedBlockState)
				{
					IExtendedBlockState extended = (IExtendedBlockState)state;
					representative = extended.getValue(BlockDegradedReactant.REPRESENTATIVE);
				}
				
				if(representative == null)
					return oldQuads;
				
				IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(representative);
				List<BakedQuad> modelQuads = model.getQuads(state, side, rand);
				
				if(modelQuads.size() != oldQuads.size())
					return oldQuads;
				
				for(int i = 0; i < modelQuads.size(); i++)
					newQuads.add(new BakedQuadRetextured(oldQuads.get(i), modelQuads.get(i).getSprite()));
				
				return newQuads;
			}
			
			@Override
			public TextureAtlasSprite getParticleTexture()
			{
				return existing.getParticleTexture();
			}
			
			@Override
			public ItemOverrideList getOverrides()
			{
				return existing.getOverrides();
			}
			
			@Override
			public boolean isGui3d()
			{
				return existing.isGui3d();
			}
			
			@Override
			public boolean isBuiltInRenderer()
			{
				return existing.isBuiltInRenderer();
			}
			
			@Override
			public boolean isAmbientOcclusion()
			{
				return existing.isAmbientOcclusion();
			}
		};
	}
}
