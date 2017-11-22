package com.arclighttw.tinyreactors.inits;

import com.arclighttw.tinyreactors.lib.recipes.ShapedRecipe;
import com.arclighttw.tinyreactors.lib.recipes.ShapelessRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class TRRecipes
{
	public static final IRecipe REACTOR_CASING = new ShapedRecipe(TRBlocks.REACTOR_CASING, new Object[] {
			"III",
			"IRI",
			"III",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Items.REDSTONE
	});
	
	public static final IRecipe REACTOR_GLASS = new ShapelessRecipe(new ItemStack(TRBlocks.REACTOR_GLASS, 7), new Object[] {
			Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Blocks.GLASS, Items.REDSTONE, Items.IRON_INGOT
	});
	
	public static final IRecipe REACTOR_CONTROLLER = new ShapedRecipe(TRBlocks.REACTOR_CONTROLLER, new Object[] {
			"CTC",
			"LGL",
			"CRC",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('T'), Blocks.REDSTONE_TORCH,
				Character.valueOf('L'), Blocks.LEVER,
				Character.valueOf('G'), Blocks.GLASS,
				Character.valueOf('R'), Items.REDSTONE
	});

//	TODO: Energy Port Recipes
//	public static final IRecipe REACTOR_ENERGY_PORT = new TemplateRecipe(TRBlocks.REACTOR_ENERGY_PORT, base_transfer_rate, base_capacity, new Object[] {
//			"   ",
//			"   ",
//			"   "
//	});
//	
//	public static final IRecipe REACTOR_ENERGY_PORT_UPGRADE = new UpgradeRecipe(TRBlocks.REACTOR_ENERGY_PORT, (TemplateRecipe)REACTOR_ENERGY_PORT);
	
	public static final IRecipe REACTOR_HEAT_SINK = new ShapedRecipe(TRBlocks.REACTOR_HEAT_SINK, new Object[] {
			"III",
			"BFB",
			"III",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('B'), Blocks.IRON_BARS,
				Character.valueOf('F'), TRItems.COOLING_COMPONENT
	});
	
//	TODO: Vent Recipes
//	public static final IRecipe REACTOR_VENT = new TemplateRecipe(TRBlocks.REACTOR_VENT, base_fan_rpm, new Object[] {
//			"   ",
//			"   ",
//			"   "
//	});
//	
//	TODO: Combining different materials has more impact (e.g. Iron +128 RPM, Diamond + 1024 RPM)
//	public static final IRecipe REACTOR_VENT_UPGRADE = new UpgradeRecipe(TRBlocks.REACTOR_VENT, (TemplateRecipe)REACTOR_VENT);
	
	public static final IRecipe REACTOR_INPUT_PORT = new ShapedRecipe(TRBlocks.REACTOR_INPUT_PORT, new Object[] {
			"III",
			"IRI",
			"IPI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('R'), Items.REDSTONE,
				Character.valueOf('P'), Blocks.PISTON
	});
	
	public static final IRecipe REACTOR_WASTE_PORT = new ShapedRecipe(TRBlocks.REACTOR_WASTE_PORT, new Object[] {
			"IPI",
			"IRI",
			"IBI",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('P'), Blocks.PISTON,
				Character.valueOf('R'), Items.REDSTONE,
				Character.valueOf('B'), Items.BUCKET
	});
	
	public static final IRecipe TINY_MANUAL = new ShapelessRecipe(TRItems.TINY_MANUAL, new Object[] {
			Items.BOOK, Items.REDSTONE, Items.IRON_INGOT
	});
	
	public static final IRecipe TINY_WRENCH = new ShapedRecipe(TRItems.TINY_WRENCH, new Object[] {
			" C ",
			" IC",
			"I  ",
				Character.valueOf('C'), TRBlocks.REACTOR_CASING,
				Character.valueOf('I'), Items.IRON_INGOT
	});
	
	public static final IRecipe COOLING_COMPONENT = new ShapedRecipe(TRItems.COOLING_COMPONENT, new Object[] {
			"I I",
			" S ",
			"I I",
				Character.valueOf('I'), Items.IRON_INGOT,
				Character.valueOf('S'), Items.STICK
	});
}
