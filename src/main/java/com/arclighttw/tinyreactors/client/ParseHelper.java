package com.arclighttw.tinyreactors.client;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ParseHelper
{
	public static ItemStack parseItemStack(JsonObject json)
	{
		if(json == null)
			return ItemStack.EMPTY;
		
		String registry = json.has("item") ? json.get("item").getAsString() : null;
		
		if(registry == null)
			return ItemStack.EMPTY;
		
		ItemStack itemstack = parseItemStack(registry);
		
		if(itemstack == ItemStack.EMPTY)
			return itemstack;
		
		int count = json.has("count") ? json.get("count").getAsInt() : 1;
		itemstack.setCount(count);
		
		if(json.has("nbt"))
			itemstack.setTagCompound(parseNBT(json.get("nbt").getAsJsonObject()));
		
		return itemstack;
	}
	
	public static NBTTagCompound parseNBT(JsonObject json)
	{
		if(json == null)
			return null;
		
		NBTTagCompound compound = new NBTTagCompound();
		
		for(Map.Entry<String, JsonElement> entry : json.entrySet())
		{
			if(!entry.getValue().isJsonPrimitive())
				continue;
			
			JsonPrimitive primitive = (JsonPrimitive)entry.getValue();
			
			if(primitive.isBoolean())
				compound.setBoolean(entry.getKey(), primitive.getAsBoolean());
			
			if(primitive.isString())
				compound.setString(entry.getKey(), primitive.getAsString());
			
			if(primitive.isNumber())
				compound.setInteger(entry.getKey(), primitive.getAsInt());
		}
		
		return compound;
	}
	
	public static ItemStack parseItemStack(String registry)
	{
		if(registry == null)
			return ItemStack.EMPTY;
		
		String[] split = registry.split(":");
		
		if(split.length == 2)
			return parseItemStackFromString(registry);
		
		if(split.length == 3)
		{
			ItemStack itemstack = parseItemStackFromString(split[0] + ":" + split[1]);
			itemstack.setItemDamage(Integer.parseInt(split[2]));
			
			return itemstack;
		}
		
		if(split.length == 4)
		{
			ItemStack itemstack = parseItemStackFromString(split[0] + ":" + split[1]);
			itemstack.setItemDamage(Integer.parseInt(split[2]));
			itemstack.setCount(Integer.parseInt(split[3]));
			
			return itemstack;
		}
		
		return ItemStack.EMPTY;
	}
	
	private static ItemStack parseItemStackFromString(String registry)
	{
		ResourceLocation registryName = new ResourceLocation(registry);

		Block block = Block.REGISTRY.getObject(registryName);
		
		if(block != null && block != Blocks.AIR)
			return new ItemStack(block);
		
		Item item = Item.REGISTRY.getObject(registryName);
		
		if(item != null)
			return new ItemStack(item);
		
		return ItemStack.EMPTY;
	}
}
