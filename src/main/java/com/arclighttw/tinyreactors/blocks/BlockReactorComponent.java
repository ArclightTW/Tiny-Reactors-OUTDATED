package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.arclighttw.tinyreactors.lib.EnumFormatting;
import com.arclighttw.tinyreactors.lib.reactor.IReactorComponent;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockReactorComponent extends BlockTiny implements IReactorComponent
{
	public BlockReactorComponent(String name, Material material)
	{
		super(name, material);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(standardTooltip != null)
			for(String line : standardTooltip)
				tooltip.add(new TextComponentTranslation(line).getFormattedText());
		else
		{
			String additional = new TextComponentTranslation(getUnlocalizedName() + ".desc").getUnformattedText();
			if(!(getUnlocalizedName() + ".desc").equals(additional))
				tooltip.add(additional);
		}
	
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(new TextComponentTranslation("tooltip.tinyreactors.positions").getFormattedText());
			
			if(additionalTooltip != null)
				for(String line : additionalTooltip)
					tooltip.add("  " + new TextComponentTranslation(line).getFormattedText());
		}
		else
		{
			String additional = new TextComponentTranslation("tooltip.tinyreactors.additional").getFormattedText();
			additional = String.format(additional.replace("Shift", "%s<Shift>%s"), EnumFormatting.AQUA, EnumFormatting.GRAY);
			tooltip.add(additional);
		}
	}
}
