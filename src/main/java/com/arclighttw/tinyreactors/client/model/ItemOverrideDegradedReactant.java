package com.arclighttw.tinyreactors.client.model;

import java.util.Collections;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemOverrideDegradedReactant extends ItemOverrideList
{
	public ItemOverrideDegradedReactant()
	{
		super(Collections.emptyList());
	}
	
	@Override
	public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
	{
		String reactantName = null;
		
		if(stack.hasTagCompound())
			reactantName = stack.getTagCompound().getString("registryName");
		
		return new ModelItemDegradedReactant(originalModel, reactantName);
	}
}
