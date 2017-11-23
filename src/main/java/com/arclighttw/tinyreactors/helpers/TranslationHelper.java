package com.arclighttw.tinyreactors.helpers;

import net.minecraft.client.resources.I18n;

public class TranslationHelper
{
	public static String translate(String translateKey, Object... parameters)
	{
		return I18n.format(translateKey, parameters);
	}
}
