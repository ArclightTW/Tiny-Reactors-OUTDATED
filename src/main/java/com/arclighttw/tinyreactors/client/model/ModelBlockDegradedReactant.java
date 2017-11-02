package com.arclighttw.tinyreactors.client.model;

import java.util.Arrays;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.arclighttw.tinyreactors.blocks.BlockDegradedReactant;
import com.arclighttw.tinyreactors.client.model.overrides.IModelBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

@SuppressWarnings("deprecation")
public class ModelBlockDegradedReactant implements IModelBlock
{
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState extended = (IExtendedBlockState)state;
			IBlockState representative = extended.getValue(BlockDegradedReactant.REPRESENTATIVE);
			
			if(representative != null)
				return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(representative).getQuads(state, side, rand);
		}
		
		return Arrays.asList();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(IBakedModel model, TransformType cameraTransformType)
	{
		ItemCameraTransforms transforms = model.getItemCameraTransforms();
		ItemTransformVec3f transform = transforms.getTransform(cameraTransformType);
		TRSRTransformation tr = new TRSRTransformation(transform);
		Matrix4f matrix = null;
		
		if(tr != null)
			matrix = tr.getMatrix();
		
		return Pair.of(model, matrix);
	}
}
