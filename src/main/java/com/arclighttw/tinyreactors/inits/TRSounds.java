package com.arclighttw.tinyreactors.inits;

import java.lang.reflect.Field;
import java.util.Locale;

import com.arclighttw.tinyreactors.main.Reference;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TRSounds
{
	public static final SoundEvent REACTOR_KLAXON = registerSoundResource("reactor_klaxon");
	public static final SoundEvent REACTOR_ACTIVE = registerSoundResource("reactor_active");
	public static final SoundEvent REACTOR_WARMING = registerSoundResource("reactor_warming");
	public static final SoundEvent REACTOR_OVERHEAT = registerSoundResource("reactor_overheat");
	
	public static final SoundEvent TINY_WRENCH = registerSoundResource("tiny_wrench");
	
	private static SoundEvent registerSoundResource(String name)
	{
		return new SoundEvent(new ResourceLocation(Reference.ID, name));
	}
	
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
