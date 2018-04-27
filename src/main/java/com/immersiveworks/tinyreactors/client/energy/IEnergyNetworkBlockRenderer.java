package com.immersiveworks.tinyreactors.client.energy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEnergyNetworkBlockRenderer {
	
	String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state );
	
}
