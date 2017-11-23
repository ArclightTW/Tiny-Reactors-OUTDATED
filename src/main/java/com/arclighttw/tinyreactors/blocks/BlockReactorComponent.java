package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.arclighttw.tinyreactors.helpers.TranslationHelper;
import com.arclighttw.tinyreactors.lib.EnumFormatting;
import com.arclighttw.tinyreactors.lib.reactor.IReactorComponent;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
	public void onStructureValidityChanged(World world, BlockPos pos, BlockPos origin, boolean isValid)
	{
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
	
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TranslationHelper.translate("tooltip.tinyreactors.positions"));
			
			if(additionalTooltip != null)
				for(String line : additionalTooltip)
					tooltip.add("  " + TranslationHelper.translate(line));
		}
		else
		{
			String additional = TranslationHelper.translate("tooltip.tinyreactors.additional");
			additional = String.format(additional.replace("Shift", "%s<Shift>%s"), EnumFormatting.AQUA, EnumFormatting.GRAY);
			tooltip.add(additional);
		}
	}
}
