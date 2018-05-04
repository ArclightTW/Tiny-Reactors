package com.immersiveworks.tinyreactors.common.inits;

import com.immersiveworks.tinyreactors.common.blocks.BlockEnergyCell;
import com.immersiveworks.tinyreactors.common.blocks.BlockEnergyConduit;
import com.immersiveworks.tinyreactors.common.blocks.BlockEnergyRelay;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorAirVent;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorCasing;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorController;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorGlass;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorHeatSink;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorSurgeProtector;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorTransferPort;

import net.minecraft.block.Block;

public class Blocks {

	public static Block ENERGY_RELAY = new BlockEnergyRelay();
	public static Block ENERGY_CONDUIT = new BlockEnergyConduit();
	public static Block ENERGY_CELL = new BlockEnergyCell();
	
	public static Block REACTOR_CONTROLLER = new BlockReactorController();
	public static Block REACTOR_TRANSFER_PORT = new BlockReactorTransferPort();
	public static Block REACTOR_HEAT_SINK = new BlockReactorHeatSink();
	public static Block REACTOR_AIR_VENT = new BlockReactorAirVent();
	
	public static Block REACTOR_CASING = new BlockReactorCasing();
	public static Block REACTOR_GLASS = new BlockReactorGlass();
	
	public static Block REACTOR_SURGE_PROTECTOR = new BlockReactorSurgeProtector();
	
//	public static Block REACTOR_PLANNER = new BlockReactorPlanner();
	
}
