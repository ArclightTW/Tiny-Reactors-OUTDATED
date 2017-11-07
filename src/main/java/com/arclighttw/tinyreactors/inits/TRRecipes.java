package com.arclighttw.tinyreactors.inits;

import java.lang.reflect.Field;
import java.util.Locale;

import com.arclighttw.tinyreactors.inits.Registry.ShapedRecipe;
import com.arclighttw.tinyreactors.inits.Registry.ShapelessRecipe;
import com.arclighttw.tinyreactors.recipes.TemplateRecipe;
import com.arclighttw.tinyreactors.recipes.UpgradeRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class TRRecipes
{
	public static IRecipe REACTOR_CONTROLLER_TIER_1 = new ShapedRecipe(TRBlocks.REACTOR_CONTROLLER_1, new Object[] {
			"CRC",
			"LGL",
			"CrC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('R'), Blocks.REDSTONE_TORCH,
				Character.valueOf('L'), Blocks.LEVER,
				Character.valueOf('G'), Blocks.GLASS,
				Character.valueOf('r'), Items.REDSTONE
	});
	
	public static IRecipe REACTOR_CONTROLLER_TIER_2 = new ShapelessRecipe(TRBlocks.REACTOR_CONTROLLER_2, new Object[] {
			TRBlocks.REACTOR_CONTROLLER_1, Blocks.REDSTONE_BLOCK
	});
	
	public static IRecipe REACTOR_ENERGY_PORT = new TemplateRecipe(TRBlocks.REACTOR_ENERGY_PORT, 256, 250000, new Object[] {
			"CIC",
			"III",
			"CIC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('I'), Blocks.IRON_BARS
	});
	
	public static IRecipe REACTOR_ENERGY_PORT_UPGRADE = new UpgradeRecipe(TRBlocks.REACTOR_ENERGY_PORT, (TemplateRecipe)REACTOR_ENERGY_PORT);
	
	public static IRecipe REACTOR_CASING = new ShapedRecipe(new ItemStack(TRBlocks.REACTOR_CASING, 8), new Object[] {
			"III",
			"IRI",
			"III",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Items.REDSTONE
	});
	
	public static IRecipe REACTOR_GLASS = new ShapelessRecipe(new ItemStack(TRBlocks.REACTOR_GLASS, 7), new Object[] {
			Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Items.REDSTONE, Items.IRON_INGOT
	});
	
	public static IRecipe REACTOR_HEAT_SINK = new ShapedRecipe(TRBlocks.REACTOR_HEAT_SINK, new Object[] {
			"CRC",
			"IBI",
			"CRC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('R'), Items.REDSTONE,
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('B'), Blocks.IRON_BARS
	});
	
	public static IRecipe REACTOR_VENT_IRON = new ShapedRecipe(new ItemStack(TRBlocks.REACTOR_VENT, 1, 0), new Object[] {
			"IBI",
			"IBI",
			"IBI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('B'), Blocks.IRON_BARS
	});
	
	public static IRecipe REACTOR_VENT_GOLD = new ShapedRecipe(new ItemStack(TRBlocks.REACTOR_VENT, 1, 1), new Object[] {
			"IBI",
			"IBI",
			"IBI",
				Character.valueOf('I'), Items.GOLD_INGOT,
				Character.valueOf('B'), Blocks.IRON_BARS
	});
	
	public static IRecipe REACTOR_VENT_DIAMOND = new ShapedRecipe(new ItemStack(TRBlocks.REACTOR_VENT, 1, 2), new Object[] {
			"IBI",
			"IBI",
			"IBI",
				Character.valueOf('I'), Items.DIAMOND,
				Character.valueOf('B'), Blocks.IRON_BARS
	});
	
	public static IRecipe REACTOR_WASTE_PORT = new ShapedRecipe(TRBlocks.REACTOR_WASTE_PORT, new Object[] {
			"CGC",
			"GBG",
			"CRC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('G'), Blocks.GLASS,
				Character.valueOf('B'), Items.BUCKET,
				Character.valueOf('R'), Items.REDSTONE
	});
	
	public static IRecipe CAPACITOR = new TemplateRecipe(TRBlocks.CAPACITOR, 256, 2500000, new Object[] {
			"IGI",
			"IGI",
			"IRI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Items.REDSTONE,
				Character.valueOf('G'), Blocks.GLASS_PANE
	});
	
	public static IRecipe CAPACITOR_UPGRADE = new UpgradeRecipe(TRBlocks.CAPACITOR, (TemplateRecipe)CAPACITOR);
	
	public static IRecipe REACTANT_COMBINER = new ShapedRecipe(TRBlocks.REACTANT_COMBINER, new Object[] {
			"IGI",
			"IPI",
			"IRI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('G'), Blocks.GLASS,
				Character.valueOf('P'), Blocks.PISTON,
				Character.valueOf('R'), Blocks.REDSTONE_BLOCK
	});
	
	public static IRecipe TINY_MANUAL = new ShapelessRecipe(TRItems.TINY_MANUAL, new Object[] {
			Items.BOOK, Items.IRON_INGOT, Items.REDSTONE
	});
	
	public static void onRegister()
	{
		try
		{
			for(Field field : TRRecipes.class.getDeclaredFields())
			{
				Object obj = field.get(null);
				
				if(!(obj instanceof IRecipe))
					continue;
				
				IRecipe block = (IRecipe)obj;
				String name = field.getName().toLowerCase(Locale.ENGLISH);
				Registry.register(block, name);
			}
		}
		catch(IllegalAccessException e)
		{
		}
	}
}
