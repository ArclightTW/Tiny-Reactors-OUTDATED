package com.arclighttw.tinyreactors.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class ResourceManager implements IResourceManagerReloadListener
{
	/**
	 * for all elements of java.class.path get a Collection of resources Pattern
	 * pattern = Pattern.compile(".*"); gets all resources
	 * 
	 * @param pattern
	 * 				the pattern to match
	 * @return the resources in the order they are found
	 */
	public static Collection<String> getResources(final Pattern pattern)
	{
		final List<String> retval = Lists.newArrayList();
		final String classPath = System.getProperty("java.class.path", ".");
		final String[] classPathElements = classPath.split(System.getProperty("path.separator"));
		
		for(final String element : classPathElements)
			retval.addAll(getResources(element, pattern));
		
		return retval;
	}
	
	private static Collection<String> getResources(final String element, final Pattern pattern)
	{
		final List<String> retval = Lists.newArrayList();
		final File file = new File(element);
		
		if(file.isDirectory())
			retval.addAll(getResourcesFromDirectory(file, pattern));
		else
			retval.addAll(getResourcesFromJarFile(file, pattern));
		
		return retval;
	}
	
	private static Collection<String> getResourcesFromJarFile(final File file, final Pattern pattern)
	{
		final List<String> retval = Lists.newArrayList();
		ZipFile zf = null;
		
		try
		{
			zf = new ZipFile(file);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		final Enumeration<? extends ZipEntry> e = zf.entries();
		while(e.hasMoreElements())
		{
			final ZipEntry ze = e.nextElement();
			final String fileName = ze.getName();
			
			if(pattern.matcher(fileName).matches())
				retval.add(fileName);
		}
		
		try
		{
			zf.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return retval;
	}
	
	private static Collection<String> getResourcesFromDirectory(final File directory, final Pattern pattern)
	{
		final List<String> retval = Lists.newArrayList();
		final File[] fileList = directory.listFiles();
		
		for(final File file : fileList)
		{
			if(file.isDirectory())
				retval.addAll(getResourcesFromDirectory(file, pattern));
			else
			{
				try
				{
					final String fileName = file.getCanonicalPath();
					
					if(pattern.matcher(fileName).matches())
						retval.add(fileName);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		
		return retval;
	}
	
	public static JsonObject loadResource(String path)
	{
		BufferedReader reader = null;
		
		try
		{
			InputStream inputStream = ResourceManager.class.getResourceAsStream(path);
			
			if(inputStream == null)
				return null;
			
			reader = new BufferedReader(new InputStreamReader(inputStream));
			JsonElement element = new Gson().fromJson(reader, JsonElement.class);
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
			
			return null;
		}
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		GuiTinyManual.refreshPageEntries(true);
	}
}
