package com.arclighttw.tinyreactors.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ResourceManager
{
	private static final Gson GSON = new Gson();
	
	public static JsonObject loadResource(ResourceLocation resource)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream()));
			JsonElement element = GSON.fromJson(reader, JsonElement.class);
			return element.getAsJsonObject();
		}
		catch(Exception e)
		{
			return new JsonObject();
		}
	}
	
	public static JsonObject[] loadResources(ResourceLocation resource)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceManager.class.getClassLoader().getResourceAsStream("assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath())));
			
			List<String> files = Lists.newArrayList();
			
			String file;
			while((file = reader.readLine()) != null)
				files.add(file);
			
			JsonObject[] json = new JsonObject[files.size()];
			
			for(int i = 0; i < json.length; i++)
				json[i] = loadResource(new ResourceLocation(resource.getResourceDomain(), resource.getResourcePath() + "/" + files.get(i)));
			
			return json;
		}
		catch(Exception e)
		{
			return new JsonObject[0];
		}
	}
}
