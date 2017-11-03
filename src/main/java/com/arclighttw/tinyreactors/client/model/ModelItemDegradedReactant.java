package com.arclighttw.tinyreactors.client.model;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.arclighttw.tinyreactors.inits.Registry.IRuntimeModel;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ModelItemDegradedReactant implements IRuntimeModel
{
	private IBakedModel createActual(IBakedModel existing, String reactantName)
	{
		Block block = Block.REGISTRY.getObject(new ResourceLocation(reactantName));
		final IBlockState representative = block == null ? null : block.getDefaultState();
		
		return new ModelSimpleBaked(existing)
		{
			@Override
			public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
			{
				List<BakedQuad> oldQuads = existing.getQuads(state, side, rand);
				
				if(representative == null)
					return oldQuads;
				
				List<BakedQuad> newQuads = Lists.newArrayList();

				IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(representative);
				List<BakedQuad> modelQuads = model.getQuads(state, side, rand);
				
				if(modelQuads.size() != oldQuads.size())
					return oldQuads;
				
				for(int i = 0; i < modelQuads.size(); i++)
					newQuads.add(new BakedQuadRetextured(oldQuads.get(i), modelQuads.get(i).getSprite()));
				
				return newQuads;
			}
			
			@Override
			public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
			{
				return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(representative).handlePerspective(cameraTransformType);
			}
		};
	}
	
	@Override
	public IBakedModel createModel(IBakedModel existing)
	{
		return new ModelSimpleBaked(existing)
		{
			@Override
			public ItemOverrideList getOverrides()
			{
				return new ItemOverrideList(Collections.emptyList()) {
					@Override
					public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
					{
						String reactantName = null;
						
						if(stack.hasTagCompound())
							reactantName = stack.getTagCompound().getString("registryName");
						
						return new ModelItemDegradedReactant().createActual(originalModel, reactantName);
					}
				};
			}
		};
	}
}
