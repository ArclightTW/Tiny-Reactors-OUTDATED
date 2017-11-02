package com.arclighttw.tinyreactors.client.model.overrides;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.EnumFacing;

public interface IModelBlock
{
	List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand);
	Pair<? extends IBakedModel, Matrix4f> handlePerspective(IBakedModel model, TransformType cameraTransformType);
}
