package com.immersiveworks.tinyreactors.common.inits;

import java.io.File;

import com.immersiveworks.tinyreactors.client.gui.GuiTinyWrenchOverlay;
import com.immersiveworks.tinyreactors.client.gui.GuiTinyWrenchOverlay.GridAlignment;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.util.Reactants;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class Configs {

	public static Configuration config;
	
	public static boolean changed;
	
	// General Settings
	
	public static boolean WRENCH_ON_HELMETS = true;
	public static String WRENCH_ON_HELMETS_LABEL = "Whether the Tiny Wrench can be added to any Helmet.";
	
	// Performance Settings
	
	public static int NETWORK_REFRESH_BLOCK_COUNT = 10;
	public static String NETWORK_REFRESH_BLOCK_COUNT_LABEL = "The number of blocks to update per tick when the Energy Network refreshes.";
	
	// Energy Settings
	
	public static boolean REACTOR_ENERGY = true;
	public static String REACTOR_ENERGY_LABEL = "Whether Reactors generate energy whilst operational.";
	
	public static float REACTOR_ENERGY_GENERATION_MULTIPLIER = 2F;
	public static String REACTOR_ENERGY_GENERATION_MULTIPLIER_LABEL = "The max multiplier a Reactor at max efficiency has.";
	
	public static float REACTOR_ENERGY_EFFICIENCY_PERCENTAGE = 80F;
	public static String REACTOR_ENERGY_EFFICIENCY_PERCENTAGE_LABEL = "Percentage of the Reactor's temperature required for it to be at max efficiency.";
	
	// Temperature Settings
	
	public static boolean REACTOR_TEMPERATURE = true;
	public static String REACTOR_TEMPERATURE_LABEL = "Whether Reactors generate heat whilst operational.";

	public static float REACTOR_TEMPERATURE_THRESHOLD = 1000F;
	public static String REACTOR_TEMPERATURE_THRESHOLD_LABEL = "Base threshold for a Reactors maximum temperature.";
	
	public static float REACTOR_TEMPERATURE_CRITICAL_PERCENTAGE = 90F;
	public static String REACTOR_TEMPERATURE_CRITICAL_PERCENTAGE_LABEL = "Percentage of the Reactor's temperature required for it to be in 'Critical Temperature' mode.";
	
	public static boolean REACTOR_HEAT_SINK = true;
	public static String REACTOR_HEAT_SINK_LABEL = "Whether Reactor Heat Sinks can increase a Reactor's maximum temperature threshold.";
	
	public static float REACTOR_HEAT_SINK_AMOUNT = 100F;
	public static String REACTOR_HEAT_SINK_AMOUNT_LABEL = "Temperature threshold increase amount per Reactor Heat Sink in a Reactor structure.";
	
	public static boolean REACTOR_AIR_VENT = true;
	public static String REACTOR_AIR_VENT_LABEL = "Whether Reactor Air Vents can decrease a Reactor's temperature increase.";
	
	public static float REACTOR_AIR_VENT_AMOUNT = 0.1F;
	public static String REACTOR_AIR_VENT_AMOUNT_LABEL = "Temperature cooldown amount (per tick) per Reactor Air Vent in a Reactor structure.";
	
	public static float REACTOR_AIR_TEMPERATURE_GAIN = 0.1F;
	public static String REACTOR_AIR_TEMPERATURE_GAIN_LABEL = "Temperature gain amount (per tick) per air block within the Reactor structure.";
	
	public static float REACTOR_REACTANT_TEMPERATURE_GAIN = 0.01F;
	public static String REACTOR_REACTANT_TEMPERATURE_GAIN_LABEL = "Temperature gain amount (per tick) per valid reactant block within the Reactor structure.";
	
	// Meltdown Settings
	
	public static boolean REACTOR_MELTDOWN = true;
	public static String REACTOR_MELTDOWN_LABEL = "Whether Reactors can go into meltdown.";
	
	public static int REACTOR_MELTDOWN_MODE = 0;
	public static String REACTOR_MELTDOWN_MODE_LABEL = "Which form of meltdown a Reactor will enter (visit the Wiki for info)";
	
	// Reactant Settings
	
	public static String[] REACTANTS = { 
			"minecraft:coal_ore = 1", "minecraft:iron_ore = 2", "minecraft:lapis_ore = 4", "minecraft:quartz_ore = 4", "minecraft:redstone_ore = 8", "minecraft:gold_ore = 8", "minecraft:diamond_ore = 16", "minecraft:emerald_ore = 32",
			"minecraft:coal_block = 8", "minecraft:iron_block = 16", "minecraft:lapis_block = 32", "minecraft:quartz_block = 16", "minecraft:redstone_block = 64", "minecraft:gold_block = 64", "minecraft:diamond_block = 128", "minecraft:emerald_block = 256"
		};
	public static String REACTANTS_LABEL = "A list of Reactants suitable for placement in the Reactor structure.";
	
	// Transfer Settings
	
	public static int TRANSFER_ITEM_ENERGY_USAGE = 100;
	public static String TRANSFER_ITEM_ENERGY_USAGE_LABEL = "The amount of RF a Reactor Transfer Port in ITEM MODE consumes inputting Reactants into a Reactor structure.";
	
	public static int TRANSFER_ITEM_USAGE_DELAY = 100;
	public static String TRANSFER_ITEM_USAGE_DELAY_LABEL = "The number of ticks between each insertion operation of a Reactor Transfer Port in ITEM mode.";
	
	public static int TRANSFER_LIQUID_ENERGY_USAGE = 1;
	public static String TRANSFER_LIUQUID_ENERGY_USAGE_LABEL = "The amount of RF/tick a Reactor Transfer Port consumes in LIQUID MODE.";
	
	// UI Settings
	
	public static int WRENCH_OVERLAY_SCALE = 0;
	public static String WRENCH_OVERLAY_SCALE_LABEL = "The scale factor of the Tiny Wrench overlay.";
	
	private static String WRENCH_OVERLAY_ANCHOR = "CENTER_MIDDLE";
	
	public static String WRENCH_OVERLAY_DO_NOT_USE_LABEL = "DO NOT EDIT THIS VALUE IN THE CONFIG FILE OR MOD OPTIONS; USE THE TINY WRENCH OVERLAY CONFIGURATION SCREEN.";
	
	@SubscribeEvent
	public static void onConfigChanged( ConfigChangedEvent.OnConfigChangedEvent event ) {
		if( event.getModID().equals( TinyReactors.ID ) )
			syncConfiguration();
	}
	
	public static void initialize( File file ) {
		config = new Configuration( file );
		syncConfiguration();
	}
	
	public static void syncConfiguration() {
		String category;
		
		category = "General";
		config.addCustomCategoryComment( category, "General Settings for Tiny Reactors" );
		
		WRENCH_ON_HELMETS = config.getBoolean( "Wrench on Helmets", category, WRENCH_ON_HELMETS, WRENCH_ON_HELMETS_LABEL );
		
		category = "Performance";
		config.addCustomCategoryComment( category, "Performance Settings for Tiny Reactors" );
		
		NETWORK_REFRESH_BLOCK_COUNT = config.getInt( "Network Refresh Block Count", category, NETWORK_REFRESH_BLOCK_COUNT, -1, 100, NETWORK_REFRESH_BLOCK_COUNT_LABEL );
		if( NETWORK_REFRESH_BLOCK_COUNT == 0 ) NETWORK_REFRESH_BLOCK_COUNT = 1;
		
		category = "Energy";
		config.addCustomCategoryComment( category, "Settings related to Energy controls (requires REACTOR_ENERGY to be true)" );
		
		REACTOR_ENERGY = config.getBoolean( "Energy", category, REACTOR_ENERGY, REACTOR_ENERGY_LABEL );
		REACTOR_ENERGY_GENERATION_MULTIPLIER = config.getFloat( "Energy Generation Multiplier", category, REACTOR_ENERGY_GENERATION_MULTIPLIER, 1, Float.MAX_VALUE, REACTOR_ENERGY_GENERATION_MULTIPLIER_LABEL );
		REACTOR_ENERGY_EFFICIENCY_PERCENTAGE = config.getFloat( "Energy Efficiency Percentage", category, REACTOR_ENERGY_EFFICIENCY_PERCENTAGE, 0, 100, REACTOR_ENERGY_EFFICIENCY_PERCENTAGE_LABEL );
		
		category = "Temperature";
		config.addCustomCategoryComment( category, "Settings related to Temperature controls (requires REACTOR_TEMPERATURE to be true)" );
		
		REACTOR_TEMPERATURE = config.getBoolean( "Temperature", category, REACTOR_TEMPERATURE, REACTOR_TEMPERATURE_LABEL );
		REACTOR_TEMPERATURE_THRESHOLD = config.getFloat( "Temperature Threshold", category, REACTOR_TEMPERATURE_THRESHOLD, 0, Float.MAX_VALUE, REACTOR_TEMPERATURE_THRESHOLD_LABEL );
		REACTOR_TEMPERATURE_CRITICAL_PERCENTAGE = config.getFloat( "Temperature Critical Percentage", category, REACTOR_TEMPERATURE_CRITICAL_PERCENTAGE, 0, 100, REACTOR_TEMPERATURE_CRITICAL_PERCENTAGE_LABEL );
		
		REACTOR_HEAT_SINK = config.getBoolean( "Heat Sink", category, REACTOR_HEAT_SINK, REACTOR_HEAT_SINK_LABEL );
		REACTOR_HEAT_SINK_AMOUNT = config.getFloat( "Heat Sink Amount", category, REACTOR_HEAT_SINK_AMOUNT, 0, Float.MAX_VALUE, REACTOR_HEAT_SINK_AMOUNT_LABEL );
		
		REACTOR_AIR_VENT = config.getBoolean( "Air Vent", category, REACTOR_AIR_VENT, REACTOR_AIR_VENT_LABEL );
		REACTOR_AIR_VENT_AMOUNT = config.getFloat( "Air Vent Amount", category, REACTOR_AIR_VENT_AMOUNT, 0, Float.MAX_VALUE, REACTOR_AIR_VENT_AMOUNT_LABEL );
		
		REACTOR_AIR_TEMPERATURE_GAIN = config.getFloat( "Air Temperature Gain", category, REACTOR_AIR_TEMPERATURE_GAIN, 0, Float.MAX_VALUE, REACTOR_AIR_TEMPERATURE_GAIN_LABEL );
		REACTOR_REACTANT_TEMPERATURE_GAIN = config.getFloat( "Reactant Temperature Gain", category, REACTOR_REACTANT_TEMPERATURE_GAIN, 0, Float.MAX_VALUE, REACTOR_REACTANT_TEMPERATURE_GAIN_LABEL );
		
		category = "Meltdown";
		config.addCustomCategoryComment( category, "Settings related to Meltdown controls (requires REACTOR_MELTDOWN to be true)" );
		
		REACTOR_MELTDOWN = config.getBoolean( "Meltdown", category, REACTOR_MELTDOWN, REACTOR_MELTDOWN_LABEL );
		REACTOR_MELTDOWN_MODE = config.getInt( "Meltdown Mode", category, REACTOR_MELTDOWN_MODE, 0, 5, REACTOR_MELTDOWN_MODE_LABEL );
		
		category = "Reactants";
		config.addCustomCategoryComment( category, "Settings related to Reactants (visit the Wiki for tips/tricks)" );
		
		REACTANTS = config.getStringList( "Reactants", category, REACTANTS, REACTANTS_LABEL );
		
		category = "Transfer";
		config.addCustomCategoryComment( category, "Settings related to Reactor Transfer Nodes" );
		
		TRANSFER_ITEM_ENERGY_USAGE = config.getInt( "Item Energy Usage", category, TRANSFER_ITEM_ENERGY_USAGE, 0, Integer.MAX_VALUE, TRANSFER_ITEM_ENERGY_USAGE_LABEL );
		TRANSFER_ITEM_USAGE_DELAY = config.getInt( "Item Usage Delay", category, TRANSFER_ITEM_USAGE_DELAY, 0, Integer.MAX_VALUE, TRANSFER_ITEM_USAGE_DELAY_LABEL );
		TRANSFER_LIQUID_ENERGY_USAGE = config.getInt( "Liquid Energy Usage", category, TRANSFER_LIQUID_ENERGY_USAGE, 0, Integer.MAX_VALUE, TRANSFER_LIUQUID_ENERGY_USAGE_LABEL );
		
		category = "UI";
		config.addCustomCategoryComment( category, "Do not change these settings via the Configuration file or Mod-Options screen, use the Tiny Wrench Overlay Configuration screen." );
		
//		WRENCH_OVERLAY_SCALE = config.getInt( "Wrench Overlay Scale", category, WRENCH_OVERLAY_SCALE, 0, 2, WRENCH_OVERLAY_SCALE_LABEL );
		WRENCH_OVERLAY_ANCHOR = config.getString( "Wrench Overlay Anchor", category, WRENCH_OVERLAY_ANCHOR, WRENCH_OVERLAY_DO_NOT_USE_LABEL );
		
		Reactants.populate();
		
		GuiTinyWrenchOverlay.setAnchor( GridAlignment.valueOf( WRENCH_OVERLAY_ANCHOR ) );
		
		if( config.hasChanged() ) {
			config.save();
			changed = true;
		}
	}
	
}
