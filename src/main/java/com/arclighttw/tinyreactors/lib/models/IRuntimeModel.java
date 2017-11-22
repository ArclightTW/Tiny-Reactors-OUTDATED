package com.arclighttw.tinyreactors.lib.models;

import net.minecraft.client.renderer.block.model.IBakedModel;

public interface IRuntimeModel
{
	IBakedModel createModel(final IBakedModel existing);
}
