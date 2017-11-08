package com.arclighttw.tinyreactors.managers;

import java.io.BufferedReader;
import java.io.InputStream;
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
	
	public static boolean hasResource(ResourceLocation resource)
	{
		try
		{
			InputStream input = Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream();
			
			if(input != null)
			{
				input.close();
				return true;
			}
			
			input = ResourceManager.class.getClassLoader().getResourceAsStream("assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath());
			
			if(input != null)
			{
				input.close();
				return true;
			}
		}
		catch(Exception e)
		{
		}
		
		return false;
	}
	
	public static JsonObject loadResource(ResourceLocation resource)
	{
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream()));
			JsonElement element = GSON.fromJson(reader, JsonElement.class);
			JsonObject object = element.getAsJsonObject();
			
			if(reader != null)
				reader.close();
			
			return object;
		}
		catch(Exception e)
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e1)
				{
				}
			}
			
			return new JsonObject();
		}
	}
	
	public static JsonObject[] loadResources(ResourceLocation resource)
	{
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new InputStreamReader(ResourceManager.class.getClassLoader().getResourceAsStream("assets/" + resource.getResourceDomain() + "/" + resource.getResourcePath())));
			
			List<String> files = Lists.newArrayList();
			
			String file;
			while((file = reader.readLine()) != null)
				files.add(file);
			
			JsonObject[] json = new JsonObject[files.size()];
			
			for(int i = 0; i < json.length; i++)
				json[i] = loadResource(new ResourceLocation(resource.getResourceDomain(), resource.getResourcePath() + "/" + files.get(i)));
			
			if(reader != null)
				reader.close();
			
			return json;
		}
		catch(Exception e)
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch(Exception e1)
				{
				}
			}
			
			return new JsonObject[0];
		}
	}
}
