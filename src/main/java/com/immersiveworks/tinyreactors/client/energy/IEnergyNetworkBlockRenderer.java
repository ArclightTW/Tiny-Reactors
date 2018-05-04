package com.immersiveworks.tinyreactors.client.energy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEnergyNetworkBlockRenderer {
	
	public default String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		return new String[] { };
	}
	
	public default String[] getWrenchOverlayInfo( World world, EntityPlayer player, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		return getWrenchOverlayInfo( world, pos, state );
	}
	
}
