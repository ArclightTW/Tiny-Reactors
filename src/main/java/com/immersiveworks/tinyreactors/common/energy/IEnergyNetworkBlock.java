package com.immersiveworks.tinyreactors.common.energy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEnergyNetworkBlock {

	EnergyNetwork.Priority getEnergyNetworkPriority();
	void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed );
	
}
