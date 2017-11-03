package com.arclighttw.tinyreactors.inits;

import java.lang.reflect.Field;
import java.util.Locale;

import com.arclighttw.tinyreactors.blocks.BlockCapacitor;
import com.arclighttw.tinyreactors.blocks.BlockDegradedReactant;
import com.arclighttw.tinyreactors.blocks.BlockReactantCombiner;
import com.arclighttw.tinyreactors.blocks.BlockReactorComponent;
import com.arclighttw.tinyreactors.blocks.BlockReactorController;
import com.arclighttw.tinyreactors.blocks.BlockReactorEnergyPort;
import com.arclighttw.tinyreactors.blocks.BlockReactorVent;
import com.arclighttw.tinyreactors.blocks.BlockReactorWastePort;
import com.arclighttw.tinyreactors.properties.EnumCapacitorTier;
import com.arclighttw.tinyreactors.properties.EnumControllerTier;
import com.arclighttw.tinyreactors.properties.EnumEnergyPortTier;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class TRBlocks
{
	public static Block REACTOR_CONTROLLER_1 = new BlockReactorController(EnumControllerTier.I).setHardness(50F).setResistance(50F);
	public static Block REACTOR_CONTROLLER_2 = new BlockReactorController(EnumControllerTier.II).setHardness(50F).setResistance(50F);
	
	public static Block REACTOR_ENERGY_PORT_1 = new BlockReactorEnergyPort(EnumEnergyPortTier.I).setHardness(50F).setResistance(50F);
	public static Block REACTOR_ENERGY_PORT_2 = new BlockReactorEnergyPort(EnumEnergyPortTier.II).setHardness(50F).setResistance(50F);
	public static Block REACTOR_ENERGY_PORT_3 = new BlockReactorEnergyPort(EnumEnergyPortTier.III).setHardness(50F).setResistance(50F);
	
	public static Block REACTOR_CASING = new BlockReactorComponent(Material.IRON, "reactor_casing").setHardness(50F).setResistance(50F);
	public static Block REACTOR_GLASS = new BlockReactorComponent(Material.GLASS, "reactor_glass").setHardness(10F).setResistance(50F);
	
	public static Block REACTOR_HEAT_SINK = new BlockReactorComponent(Material.IRON, "reactor_heat_sink").setTooltip("Increases Reactor Heat Capacity by 50 C").setHardness(50F).setResistance(50F);
	
	public static Block REACTOR_VENT = new BlockReactorVent().setHardness(50F).setResistance(50F);
	
	public static Block REACTOR_WASTE_PORT = new BlockReactorWastePort().setHardness(50F).setResistance(50F);
	
	public static Block CAPACITOR_1 = new BlockCapacitor(EnumCapacitorTier.I).setHardness(50F).setResistance(50F);
	public static Block CAPACITOR_2 = new BlockCapacitor(EnumCapacitorTier.II).setHardness(50F).setResistance(50F);
	public static Block CAPACITOR_3 = new BlockCapacitor(EnumCapacitorTier.III).setHardness(50F).setResistance(50F);
	
	public static Block REACTANT_COMBINER = new BlockReactantCombiner().setHardness(50F).setResistance(50F);
	
	public static Block DEGRADED_REACTANT = new BlockDegradedReactant().setHardness(5F).setResistance(5F);
	
	public static void onRegister()
	{
		try
		{
			for(Field field : TRBlocks.class.getDeclaredFields())
			{
				Object obj = field.get(null);
				
				if(!(obj instanceof Block))
					continue;
				
				Block block = (Block)obj;
				String name = field.getName().toLowerCase(Locale.ENGLISH);
				Registry.register(block, name);
			}
		}
		catch(IllegalAccessException e)
		{
		}
	}
}
