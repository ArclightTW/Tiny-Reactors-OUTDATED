package com.arclighttw.tinyreactors.client.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;

public interface IReplacementModel extends IBakedModel
{
	void applyExisting(IBakedModel model, ItemOverrideList list);
}
