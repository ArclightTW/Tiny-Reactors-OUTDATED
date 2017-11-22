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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Reference.ID)
public class InternalRegistry
{
	private static Map<ResourceLocation, Block> blocks = Maps.newConcurrentMap();
	private static Map<ResourceLocation, Item> items = Maps.newConcurrentMap();
	private static Map<ResourceLocation, IRecipe> recipes = Maps.newConcurrentMap();
	private static Map<ResourceLocation, SoundEvent> sounds = Maps.newConcurrentMap();
	
	private static Map<ResourceLocation, Class<? extends TileEntity>> tiles = Maps.newConcurrentMap();
	
	private static Map<ModelResourceLocation, IRuntimeModel> models = Maps.newConcurrentMap();
	
	public static void registerBlock(Block block, String name)
	{
		if(block == null)
		{
			System.err.println(String.format("Unable to register Block with name '%s' as it is null!", name));
			return;
		}
		
		block.setRegistryName(new ResourceLocation(Reference.ID, name));
		
		if(blocks.containsKey(block.getRegistryName()))
		{
			System.err.println(String.format("Unable to register Block with name '%s' as an entry already exists with this name!\nOriginal: %s\nFailed: %s", name, blocks.get(block.getRegistryName()).toString(), block.toString()));
			return;
		}
		
		blocks.put(block.getRegistryName(), block);
	}
	
	public static void registerItem(Item item, String name)
	{
		if(item == null)
		{
			System.err.println(String.format("Unable to register Item with name '%s' as it is null!", name));
			return;
		}
		
		item.setRegistryName(new ResourceLocation(Reference.ID, name));		
		
		if(items.containsKey(item.getRegistryName()))
		{
			System.err.println(String.format("Unable to register Item with name '%s' as an entry already exists with this name!\nOriginal: %s\nFailed: %s", name, items.get(item.getRegistryName()).toString(), item.toString()));
			return;
		}
		
		items.put(item.getRegistryName(), item);
	}
	
	public static void registerTileEntity(TileEntity tile, String name)
	{
		if(tile == null)
		{
			System.err.println(String.format("Unable to register TileEntity with name '%s' as it is null!", name));
			return;
		}
		
		Class<? extends TileEntity> clazz = tile.getClass();
		ResourceLocation resource = new ResourceLocation(Reference.ID, name);
		
		if(tiles.containsKey(resource))
		{
			System.err.println(String.format("Unable to register TileEntity with name '%s' as an entry already exists with this name!\nOriginal: %s\nFailed: %s", name, tiles.get(resource).toString(), tile.toString()));
			return;
		}
		
		tiles.put(resource, clazz);
	}
	
	public static void registerRecipe(IRecipe recipe, String name)
	{
		if(recipe == null)
		{
			System.err.println(String.format("Unable to register IRecipe with name '%s' as it is null!", name));
			return;
		}
		
		recipe.setRegistryName(new ResourceLocation(Reference.ID, name));
		
		if(recipes.containsKey(recipe.getRegistryName()))
		{
			System.err.println(String.format("Unable to register IRecipe with name '%s' as an entry already exists with this name!\nOriginal: %s\nFailed: %s", name, recipes.get(recipe.getRegistryName()).toString(), recipe.toString()));
			return;
		}
		
		recipes.put(recipe.getRegistryName(), recipe);
	}
	
	public static void registerSound(SoundEvent sound, String name)
	{
		if(sound == null)
		{
			System.err.println(String.format("Unable to register SoundEvent with name '%s' as it is null!", name));
			return;
		}
		
		sound.setRegistryName(new ResourceLocation(Reference.ID, name));
		
		if(sounds.containsKey(sound.getRegistryName()))
		{
			System.err.println(String.format("Unable to register SoundEvent with name '%s' as an entry already exists with this name!\nOriginal: %s\nFailed: %s", name, sounds.get(sound.getRegistryName()).toString(), sound.toString()));
			return;
		}
		
		sounds.put(sound.getRegistryName(), sound);
	}
	
	public static void registerModel(IRuntimeModel model, String name, String variant)
	{
		if(model == null)
		{
			System.err.println(String.format("Unable to register IRuntimeModel with name '%s' as it is null!", name));
			return;
		}
		
		ModelResourceLocation resource = new ModelResourceLocation(new ResourceLocation(Reference.ID, name), variant);
		
		if(models.containsKey(resource))
		{
			System.err.println(String.format("Unable to register IRuntimeModel with name '%s' as an entry already exists with this name!\nOriginal: %s\nFailed: %s", name, models.get(resource).toString(), model.toString()));
			return;
		}
		
		models.put(resource, model);
	}
	
	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(blocks.values().toArray(new Block[blocks.size()]));
		
		for(Map.Entry<ResourceLocation, Class<? extends TileEntity>> entry : tiles.entrySet())
			GameRegistry.registerTileEntity(entry.getValue(), entry.getKey().toString());
	}
	
	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(items.values().toArray(new Item[items.size()]));
	}
	
	@SubscribeEvent
	public static void onRegisterRecipe(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry().registerAll(recipes.values().toArray(new IRecipe[recipes.size()]));
	}
	
	@SubscribeEvent
	public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().registerAll(sounds.values().toArray(new SoundEvent[sounds.size()]));
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRegisterModel(ModelRegistryEvent event)
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
	public static void onBakeModel(ModelBakeEvent event)
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
