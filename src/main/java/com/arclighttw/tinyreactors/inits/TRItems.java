package com.arclighttw.tinyreactors.inits;

import java.lang.reflect.Field;
import java.util.Locale;

import com.arclighttw.tinyreactors.items.ItemTiny;

import net.minecraft.item.Item;

public class TRItems
{
	public static Item RADIOACTIVE_INGOT = new ItemTiny("radioactive_ingot");
	
	public static void onRegister()
	{
		try
		{
			for(Field field : TRItems.class.getDeclaredFields())
			{
				Object obj = field.get(null);
				
				if(!(obj instanceof Item))
					continue;
				
				Item item = (Item)obj;
				String name = field.getName().toLowerCase(Locale.ENGLISH);
				Registry.register(item, name);
			}
		}
		catch(IllegalAccessException e)
		{
		}
	}
}
