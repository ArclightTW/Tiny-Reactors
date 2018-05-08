package com.immersiveworks.tinyreactors.common.util;

import java.util.List;

import com.immersiveworks.tinyreactors.common.inits.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class Reactor {

	public static Block[] ROOF_CORNERS = {
			Blocks.REACTOR_CASING
	};
	
	public static Block[] ROOF_WALLS = {
			Blocks.REACTOR_CASING
	};
	
	public static Block[] ROOF_INTERIORS = {
			Blocks.REACTOR_CASING,
			Blocks.REACTOR_AIR_VENT
	};
	
	public static Block[] BASE_CORNERS = {
			Blocks.REACTOR_CASING
	};
	
	public static Block[] BASE_WALLS = {
			Blocks.REACTOR_CASING
	};
	
	public static Block[] BASE_INTERIORS = {
			Blocks.REACTOR_CASING
			// TODO: Blocks.REACTOR_PREIGNITER
	};
	
	public static Block[] CENTRAL_CORNERS = {
			Blocks.REACTOR_CASING,
			Blocks.REACTOR_CONTROLLER,
			Blocks.REACTOR_TRANSFER_PORT,
			Blocks.REACTOR_SURGE_PROTECTOR
	};
	
	public static Block[] CENTRAL_WALLS = {
			Blocks.REACTOR_CASING,
			Blocks.REACTOR_GLASS,
			Blocks.REACTOR_CONTROLLER,
			Blocks.REACTOR_TRANSFER_PORT,
			Blocks.REACTOR_HEAT_SINK,
			Blocks.REACTOR_SURGE_PROTECTOR
	};
	
	public static boolean contained( IBlockState state, List<IBlockState> list ) {
		return contained( state, list.toArray( new IBlockState[ list.size() ] ) );
	}
	
	public static boolean contained( IBlockState state, IBlockState[] list ) {
		Block[] blockArr = new Block[ list.length ];
		for( int i = 0; i < list.length; i++ )
			blockArr[ i ] = list[ i ].getBlock();
		
		return contained( state.getBlock(), blockArr );
	}
	
	public static boolean contained( IBlockState state, Block[] list ) {
		return contained( state.getBlock(), list );
	}
	
	public static boolean contained( Block block, Block[] list ) {
		for( int i = 0; i < list.length; i++ )
			if( list[ i ] == block )
				return true;
		
		return false;
	}
	
}
