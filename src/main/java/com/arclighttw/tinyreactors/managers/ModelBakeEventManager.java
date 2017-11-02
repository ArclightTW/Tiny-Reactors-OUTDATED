package com.arclighttw.tinyreactors.managers;

import com.arclighttw.tinyreactors.client.model.ItemOverrideDegradedReactant;
import com.arclighttw.tinyreactors.client.model.ModelBlockDegradedReactant;
import com.arclighttw.tinyreactors.client.model.overrides.IModelBlock;
import com.arclighttw.tinyreactors.client.model.overrides.ModelReplacementBlockModel;
import com.arclighttw.tinyreactors.client.model.overrides.ModelReplacementItemModel;
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
		replaceBlockEntry(event, TRBlocks.DEGRADED_REACTANT, new ModelBlockDegradedReactant());
		replaceItemEntry(event, TRBlocks.DEGRADED_REACTANT, new ItemOverrideDegradedReactant());
	}
	
	void replaceBlockEntry(ModelBakeEvent event, Block block, IModelBlock model)
	{
		replaceBlockEntry(event, new ModelResourceLocation(block.getRegistryName(), "normal"), model);
	}
	
	void replaceItemEntry(ModelBakeEvent event, Block block, ItemOverrideList overrideList)
	{
		replaceItemEntry(event, new ModelResourceLocation(block.getRegistryName(), "inventory"), overrideList);
	}
	
	void replaceItemEntry(ModelBakeEvent event, Item item, ItemOverrideList overrideList)
	{
		replaceItemEntry(event, new ModelResourceLocation(item.getRegistryName(), "inventory"), overrideList);
	}
	
	void replaceBlockEntry(ModelBakeEvent event, ModelResourceLocation model, IModelBlock bakedModel)
	{
		Object object = event.getModelRegistry().getObject(model);
		
		if(object instanceof IBakedModel)
		{
			ModelReplacementBlockModel newModel = new ModelReplacementBlockModel();
			newModel.applyExisting((IBakedModel)object, bakedModel);
			
			event.getModelRegistry().putObject(model, newModel);
		}
	}
	
	void replaceItemEntry(ModelBakeEvent event, ModelResourceLocation model, ItemOverrideList overrideList)
	{
		Object object = event.getModelRegistry().getObject(model);
		
		if(object instanceof IBakedModel)
		{
			ModelReplacementItemModel newModel = new ModelReplacementItemModel();
			newModel.applyExisting((IBakedModel)object, overrideList);
			
			event.getModelRegistry().putObject(model, newModel);
		}
	}
}
