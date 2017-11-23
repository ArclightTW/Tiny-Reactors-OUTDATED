package com.arclighttw.tinyreactors.items;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.arclighttw.tinyreactors.helpers.TranslationHelper;
import com.arclighttw.tinyreactors.lib.EnumFormatting;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTiny extends Item
{
	private List<String> standardTooltip;
	private List<String> additionalTooltip;
	
	public ItemTiny(String name)
	{
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ItemTiny> T setStandardTooltip(String... tooltip)
	{
		standardTooltip = Arrays.asList(tooltip);
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ItemTiny> T setAdditionalTooltip(String... tooltip)
	{
		additionalTooltip = Arrays.asList(tooltip);
		return (T)this;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(standardTooltip != null)
			for(String line : standardTooltip)
				tooltip.add(TranslationHelper.translate(line));
		else
		{
			String additional = TranslationHelper.translate(getUnlocalizedName() + ".desc");
			if(!(getUnlocalizedName() + ".desc").equals(additional))
				tooltip.add(additional);
		}
		
		if(additionalTooltip != null)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				for(String line : additionalTooltip)
					tooltip.add(TranslationHelper.translate(line));
			else
			{
				String additional = TranslationHelper.translate("tooltip.tinyreactors.additional");
				additional = String.format(additional.replace("Shift", "%s<Shift>%s"), EnumFormatting.AQUA, EnumFormatting.GRAY);
				tooltip.add(additional);
			}
		}
	}
}
