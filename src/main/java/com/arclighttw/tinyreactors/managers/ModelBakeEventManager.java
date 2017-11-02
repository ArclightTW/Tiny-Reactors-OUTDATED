package com.arclighttw.tinyreactors.managers;

import com.arclighttw.tinyreactors.client.model.ModelOverrideDegradedReactant;
import com.arclighttw.tinyreactors.client.model.ModelReplacementModel;
import com.arclighttw.tinyreactors.inits.TRBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBakeEventManager
{
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event)
	{
		replaceEntry(event, TRBlocks.DEGRADED_REACTANT, new ModelOverrideDegradedReactant());
	}
	
	void replaceEntry(ModelBakeEvent event, Block block, ItemOverrideList overrideList)
	{
		replaceEntry(event, new ModelResourceLocation(block.getRegistryName(), "inventory"), overrideList);
	}
	
	void replaceEntry(ModelBakeEvent event, Item item, ItemOverrideList overrideList)
	{
		replaceEntry(event, new ModelResourceLocation(item.getRegistryName(), "inventory"), overrideList);
	}
	
	void replaceEntry(ModelBakeEvent event, ModelResourceLocation model, ItemOverrideList overrideList)
	{
		Object object = event.getModelRegistry().getObject(model);
		
		if(object instanceof IBakedModel)
		{
			ModelReplacementModel newModel = new ModelReplacementModel();
			newModel.applyExisting((IBakedModel)object, overrideList);
			
			event.getModelRegistry().putObject(model, newModel);
		}
	}
}
