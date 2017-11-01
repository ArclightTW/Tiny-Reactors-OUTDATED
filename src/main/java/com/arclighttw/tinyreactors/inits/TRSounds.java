package com.arclighttw.tinyreactors.inits;

import java.lang.reflect.Field;
import java.util.Locale;

import com.arclighttw.tinyreactors.main.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TRSounds
{
	public static final SoundEvent REACTOR_KLAXON = new SoundEvent(new ResourceLocation(Reference.ID, "reactor_klaxon"));
	public static final SoundEvent REACTOR_ACTIVE = new SoundEvent(new ResourceLocation(Reference.ID, "reactor_active"));
	
	public static void onRegister()
	{
		try
		{
			for(Field field : TRSounds.class.getDeclaredFields())
			{
				Object obj = field.get(null);
				
				if(!(obj instanceof SoundEvent))
					continue;
				
				SoundEvent sound = (SoundEvent)obj;
				String name = field.getName().toLowerCase(Locale.ENGLISH);
				Registry.register(sound, name);
			}
		}
		catch(IllegalAccessException e)
		{
		}
	}
}
