package com.arclighttw.tinyreactors.inits;

import com.arclighttw.tinyreactors.blocks.BlockReactorComponent;
import com.arclighttw.tinyreactors.blocks.BlockReactorController;
import com.arclighttw.tinyreactors.blocks.BlockReactorEnergyPort;
import com.arclighttw.tinyreactors.blocks.BlockReactorGlass;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class TRBlocks
{
	public static final Block REACTOR_CASING		= new BlockReactorComponent("reactor_casing", Material.IRON)
															.setAdditionalTooltip("tooltip.tinyreactors.positions.everywhere");
	public static final Block REACTOR_GLASS		= new BlockReactorGlass("reactor_glass")
															.setAdditionalTooltip("tooltip.tinyreactors.positions.walls");
	
	public static final Block REACTOR_CONTROLLER	= new BlockReactorController("reactor_controller")
															.setAdditionalTooltip("tooltip.tinyreactors.positions.walls", "tooltip.tinyreactors.positions.pillars");
	public static final Block REACTOR_ENERGY_PORT	= new BlockReactorEnergyPort("reactor_energy_port")
															.setAdditionalTooltip("tooltip.tinyreactors.positions.walls", "tooltip.tinyreactors.positions.pillars");
	
	// TODO: new BlockReactorHeatSink();
	public static final Block REACTOR_HEAT_SINK 	= new BlockReactorComponent("reactor_heat_sink", Material.IRON)
															.setAdditionalTooltip("tooltip.tinyreactors.positions.walls");
	// TODO: new BlockReactorVent();
	// TODO: Multi-tiered Vents.
	// TODO: Fan RPM can be increased/decreased (higher tier has higher max RPM).
	public static final Block REACTOR_VENT			= new BlockReactorComponent("reactor_vent", Material.IRON)
															.setAdditionalTooltip("tooltip.tinyreactors.positions.roof");
	
	// TODO: new BlockReactorInputPort();
	// TODO: Consume RF to input (from reactor structure directly).
	// TODO: Animated opening/closing door
	public static final Block REACTOR_INPUT_PORT		= new BlockReactorComponent("reactor_input_port", Material.IRON)
																.setAdditionalTooltip("tooltip.tinyreactors.positions.walls");
	// TODO: new BlockReactorWastePort();
	// TODO: Consume RF to input (from reactor structure directly).
	// TODO: Vent with animated fluid.
	// TODO: Should Radioactive Waste be a fluid? That can be turned into an ingot?
	public static final Block REACTOR_WASTE_PORT		= new BlockReactorComponent("reactor_waste_port", Material.IRON)
																.setAdditionalTooltip("tooltip.tinyreactors.positions.walls");
	
	// TODO: new BlockCapacitor();
	public static final Block CAPACITOR					= new BlockReactorComponent("capacitor", Material.IRON);
	
	// TODO: new BlockReactantCombiner();
	public static final Block REACTANT_COMBINER		= new BlockReactorComponent("reactant_combiner", Material.IRON);
	// TODO: new BlockDegradedReactant();
	public static final Block DEGRADED_REACTANT		= new BlockReactorComponent("degraded_reactant", Material.ROCK);
}
