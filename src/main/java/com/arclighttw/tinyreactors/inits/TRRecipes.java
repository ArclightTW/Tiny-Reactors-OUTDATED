package com.arclighttw.tinyreactors.inits;

import java.lang.reflect.Field;
import java.util.Locale;

import com.arclighttw.tinyreactors.inits.Registry.ShapedRecipe;
import com.arclighttw.tinyreactors.inits.Registry.ShapelessRecipe;

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
	
	public static IRecipe REACTOR_ENERGY_PORT_TIER_1 = new ShapedRecipe(TRBlocks.REACTOR_ENERGY_PORT_1, new Object[] {
			"CIC",
			"III",
			"CIC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('I'), Blocks.IRON_BARS
	});
	
	public static IRecipe REACTOR_ENERGY_PORT_TIER_2 = new ShapedRecipe(TRBlocks.REACTOR_ENERGY_PORT_2, new Object[] {
			"CIC",
			"RER",
			"CRC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Items.REDSTONE,
				Character.valueOf('E'), TRBlocks.REACTOR_ENERGY_PORT_1
	});
	
	public static IRecipe REACTOR_ENERGY_PORT_TIER_3 = new ShapedRecipe(TRBlocks.REACTOR_ENERGY_PORT_3, new Object[] {
			"CIC",
			"RER",
			"CRC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('I'), Blocks.IRON_BLOCK,
				Character.valueOf('R'), Blocks.REDSTONE_BLOCK,
				Character.valueOf('E'),TRBlocks.REACTOR_ENERGY_PORT_2
	});
	
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
	
	public static IRecipe CAPACITOR_TIER_1 = new ShapedRecipe(TRBlocks.CAPACITOR_1, new Object[] {
			"IGI",
			"IGI",
			"IRI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Items.REDSTONE,
				Character.valueOf('G'), Blocks.GLASS_PANE
	});
	
	public static IRecipe CAPACITOR_TIER_2 = new ShapedRecipe(TRBlocks.CAPACITOR_2, new Object[] {
			"IRI",
			"ICI",
			"IRI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Items.REDSTONE,
				Character.valueOf('C'), TRBlocks.CAPACITOR_1
	});
	
	public static IRecipe CAPACITOR_TIER_3 = new ShapedRecipe(TRBlocks.CAPACITOR_3, new Object[] {
			"IRI",
			"ICI",
			"IRI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Blocks.REDSTONE_BLOCK,
				Character.valueOf('C'), TRBlocks.CAPACITOR_2
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
