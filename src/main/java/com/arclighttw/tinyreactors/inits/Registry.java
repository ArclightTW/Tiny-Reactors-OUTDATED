package com.arclighttw.tinyreactors.inits;

import java.util.Map;

import javax.annotation.Nonnull;

import com.arclighttw.tinyreactors.main.Reference;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Registry
{
	static Map<ResourceLocation, Block> BLOCKS = Maps.newHashMap();
	static Map<ResourceLocation, Item> ITEMS = Maps.newHashMap();
	static Map<ResourceLocation, IRecipe> RECIPES = Maps.newHashMap();
	
	static Map<ResourceLocation, Class<? extends TileEntity>> TILES = Maps.newHashMap();
	
	public static void register(Block block, String name)
	{
		registerBlock(block, name);
		registerItem(block instanceof IItemProvider ? ((IItemProvider)block).getItemBlock() : new ItemBlock(block), name);
		
		if(block instanceof ITileEntityProvider)
			registerTileEntity(((ITileEntityProvider)block).createNewTileEntity(null, -1), name);
	}
	
	public static void register(Item item, String name)
	{
		registerItem(item, name);
	}
	
	public static void register(IRecipe recipe, String name)
	{
		registerRecipe(recipe, name);
	}
	
	private static void registerBlock(Block block, String name)
	{
		if(block == null)
		{
			System.err.println(String.format("Unable to register Block with name '%s' as the provided Block is null.", name));
			return;
		}
		
		block.setRegistryName(new ResourceLocation(Reference.ID, name));
		
		if(BLOCKS.containsKey(block.getRegistryName()))
		{
			System.err.println(String.format("Unable to register Block with registry name '%s' as an entry already exists with this name.", block.getRegistryName().toString()));
			return;
		}
		
		BLOCKS.put(block.getRegistryName(), block);
	}
	
	private static void registerItem(Item item, String name)
	{
		if(item == null)
		{
			System.err.println(String.format("Unable to register Item with name '%s' as the provided Item is null.", name));
			return;
		}
		
		item.setRegistryName(new ResourceLocation(Reference.ID, name));
		
		if(ITEMS.containsKey(item.getRegistryName()))
		{
			System.err.println(String.format("Unable to register Item with registry name '%s' as an entry already exists with this name.", item.getRegistryName().toString()));
			return;
		}
		
		ITEMS.put(item.getRegistryName(), item);
	}
	
	private static void registerRecipe(IRecipe recipe, String name)
	{
		if(recipe == null)
		{
			System.err.println(String.format("Unable to register IRecipe with name '%s' as the provided IRecipe is null.", name));
			return;
		}
		
		recipe.setRegistryName(new ResourceLocation(Reference.ID, name));
		
		if(RECIPES.containsKey(recipe.getRegistryName()))
		{
			System.err.println(String.format("Unable to register Recipe with registry name '%s' as an entry already exists with this name.", recipe.getRegistryName().toString()));
			return;
		}
		
		RECIPES.put(recipe.getRegistryName(), recipe);
	}
	
	private static void registerTileEntity(TileEntity tileEntity, String name)
	{
		if(tileEntity == null)
		{
			System.err.println(String.format("Unable to register TileEntity with name '%s' as the provided TileEntity is null.", name));
			return;
		}
		
		ResourceLocation tileResource = new ResourceLocation(Reference.ID, name);
		Class<? extends TileEntity> tileClass = tileEntity.getClass();
		
		if(TILES.containsKey(tileResource))
		{
			System.err.println(String.format("Unable to register TileEntity with registry name '%s' as an entry already exists with this name.", tileResource));
			return;
		}
		
		if(TILES.containsValue(tileClass))
			return;
		
		TILES.put(tileResource, tileClass);
	}
	
	public interface IItemProvider
	{
		ItemBlock getItemBlock();
	}
	
	public interface IModelProvider
	{
		@SideOnly(Side.CLIENT)
		void registerModels();
	}
	
	@SubscribeEvent
	public void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		TRBlocks.onRegister();
		
		for(Map.Entry<ResourceLocation, Block> block : BLOCKS.entrySet())
			event.getRegistry().register(block.getValue());
		
		for(Map.Entry<ResourceLocation, Class<? extends TileEntity>> tileEntity : TILES.entrySet())
			GameRegistry.registerTileEntity(tileEntity.getValue(), tileEntity.getKey().toString());
	}
	
	@SubscribeEvent
	public void onItemRegister(RegistryEvent.Register<Item> event)
	{
		for(Map.Entry<ResourceLocation, Item> item : ITEMS.entrySet())
			event.getRegistry().register(item.getValue());			
	}
	
	@SubscribeEvent
	public void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
	{
		TRRecipes.onRegister();
		
		for(Map.Entry<ResourceLocation, IRecipe> recipe : RECIPES.entrySet())
			event.getRegistry().register(recipe.getValue());	
	}
	
	@SubscribeEvent
	public void onModelRegister(ModelRegistryEvent event)
	{
		for(Map.Entry<ResourceLocation, Block> block : BLOCKS.entrySet())
		{
			if(block.getValue() instanceof IModelProvider)
			{
				((IModelProvider)block.getValue()).registerModels();
				continue;
			}
			
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block.getValue()), 0, new ModelResourceLocation(new ResourceLocation(Reference.ID, block.getKey().getResourcePath()), "inventory"));
		}
	}
	
	static class ShapedRecipe extends ShapedOreRecipe
	{
		public ShapedRecipe(Block result, Object... recipe) { this(new ItemStack(result), recipe); }
		public ShapedRecipe(Item result, Object... recipe) { this(new ItemStack(result), recipe); }
		public ShapedRecipe(@Nonnull ItemStack result, Object... recipe) { this(result.getItem().getRegistryName(), result, CraftingHelper.parseShaped(recipe)); }
		
		ShapedRecipe(ResourceLocation group, @Nonnull ItemStack result, ShapedPrimer primer)
		{
			super(group, result, primer);
		}
	}
	
	static class ShapelessRecipe extends ShapelessOreRecipe
	{
		public ShapelessRecipe(Block result, Object... recipe) { this(new ItemStack(result), recipe); }
		public ShapelessRecipe(Item result, Object... recipe) { this(new ItemStack(result), recipe); }
		
		public ShapelessRecipe(@Nonnull ItemStack result, NonNullList<Ingredient> input)
		{
			super(result.getItem().getRegistryName(), input, result);
		}
		
		public ShapelessRecipe(@Nonnull ItemStack result, Object... recipe)
		{
			super(result.getItem().getRegistryName(), result, recipe);
		}
	}
}
