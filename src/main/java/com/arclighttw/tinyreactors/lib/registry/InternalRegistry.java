package com.arclighttw.tinyreactors.lib.registry;

import java.util.Map;

import com.arclighttw.tinyreactors.lib.models.IModelRegistrar;
import com.arclighttw.tinyreactors.lib.models.IRuntimeModel;
import com.arclighttw.tinyreactors.main.Reference;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Reference.ID)
public class InternalRegistry
{
	private Map<ResourceLocation, Block> blocks;
	private Map<ResourceLocation, Item> items;
	private Map<ResourceLocation, IRecipe> recipes;
	private Map<ResourceLocation, SoundEvent> sounds;
	
	private Map<ResourceLocation, Class<? extends TileEntity>> tiles;
	
	private Map<ModelResourceLocation, IRuntimeModel> models;
	
	public InternalRegistry()
	{
		blocks = Maps.newConcurrentMap();
		items = Maps.newConcurrentMap();
		recipes = Maps.newConcurrentMap();
		sounds = Maps.newConcurrentMap();
		
		tiles = Maps.newConcurrentMap();
		
		models = Maps.newConcurrentMap();
	}
	
	public void registerBlock(Block block, String name)
	{
		block.setRegistryName(new ResourceLocation(Reference.ID, name));
		blocks.put(block.getRegistryName(), block);
	}
	
	public void registerItem(Item item, String name)
	{
		item.setRegistryName(new ResourceLocation(Reference.ID, name));		
		items.put(item.getRegistryName(), item);
	}
	
	public void registerTileEntity(TileEntity tile, String name)
	{
		Class<? extends TileEntity> clazz = tile.getClass();
		tiles.put(new ResourceLocation(Reference.ID, name), clazz);
	}
	
	public void registerRecipe(IRecipe recipe, String name)
	{
		recipe.setRegistryName(new ResourceLocation(Reference.ID, name));		
		recipes.put(recipe.getRegistryName(), recipe);
	}
	
	public void registerSound(SoundEvent sound, String name)
	{
		sound.setRegistryName(new ResourceLocation(Reference.ID, name));		
		sounds.put(sound.getRegistryName(), sound);
	}
	
	public void registerModel(IRuntimeModel model, String name, String variant)
	{
		ModelResourceLocation resource = new ModelResourceLocation(new ResourceLocation(Reference.ID, name), variant);
		models.put(resource, model);
	}
	
	@SubscribeEvent
	public void onRegisterBlock(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(blocks.values().toArray(new Block[blocks.size()]));
	}
	
	@SubscribeEvent
	public void onRegisterItem(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(items.values().toArray(new Item[items.size()]));
	}
	
	@SubscribeEvent
	public void onRegisterRecipe(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry().registerAll(recipes.values().toArray(new IRecipe[recipes.size()]));
	}
	
	@SubscribeEvent
	public void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().registerAll(sounds.values().toArray(new SoundEvent[sounds.size()]));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRegisterModel(ModelRegistryEvent event)
	{
		for(Map.Entry<ResourceLocation, Item> entry : items.entrySet())
		{
			Item item = entry.getValue();
			
			if(item instanceof IModelRegistrar)
			{
				((IModelRegistrar)item).registerModels();
				continue;
			}
			
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Reference.ID, entry.getKey().getResourcePath()), "inventory"));			
		}
		
		for(Map.Entry<ResourceLocation, Block> entry : blocks.entrySet())
		{
			Block block = entry.getValue();
			
			if(block instanceof IModelRegistrar)
			{
				((IModelRegistrar)block).registerModels();
				continue;
			}
			
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.ID, entry.getKey().getResourcePath()), "inventory"));			
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onBakeModel(ModelBakeEvent event)
	{
		for(Map.Entry<ModelResourceLocation, IRuntimeModel> entry : models.entrySet())
		{
			Object existing = event.getModelRegistry().getObject(entry.getKey());
			
			if(!(existing instanceof IBakedModel))
				continue;
			
			event.getModelRegistry().putObject(entry.getKey(), entry.getValue().createModel((IBakedModel)existing));
		}
	}
}
