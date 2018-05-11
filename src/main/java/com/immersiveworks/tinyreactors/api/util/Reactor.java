package com.immersiveworks.tinyreactors.api.util;

import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.TinyReactorsAPI;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class Reactor {

	public static Block[] ROOF_CORNERS;
	public static Block[] ROOF_WALLS;
	public static Block[] ROOF_INTERIORS;
	
	public static Block[] BASE_CORNERS;
	public static Block[] BASE_WALLS;
	public static Block[] BASE_INTERIORS;
	
	public static Block[] CENTRAL_CORNERS;
	public static Block[] CENTRAL_WALLS;
	
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
		if( ROOF_CORNERS == null )
			initialize();
		
		for( int i = 0; i < list.length; i++ )
			if( list[ i ].getRegistryName().equals( block.getRegistryName() ) )
				return true;
		
		return false;
	}
	
	public static String[] retreiveValidPlaces( Block block ) {
		List<String> valid = Lists.newLinkedList();
		
		if( contained( block, ROOF_CORNERS ) ) valid.add( "TC" );
		if( contained( block, ROOF_WALLS ) ) valid.add( "TE" );
		if( contained( block, ROOF_INTERIORS ) ) valid.add( "TI" );
		
		if( contained( block, BASE_CORNERS ) ) valid.add( "BC" );
		if( contained( block, BASE_WALLS ) ) valid.add( "BE" );
		if( contained( block, BASE_INTERIORS ) ) valid.add( "BI" );
		
		if( contained( block, CENTRAL_CORNERS ) ) valid.add( "MC" );
		if( contained( block, CENTRAL_WALLS ) ) valid.add( "ME" );
		
		return valid.toArray( new String[ valid.size() ] );
	}
	
	private static void initialize() {
		if( TinyReactorsAPI.getBlock( "reactor_casing" ) == null )
			return;
		
		ROOF_CORNERS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" )
		};
		
		ROOF_WALLS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" )
		};
		
		ROOF_INTERIORS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" ),
				TinyReactorsAPI.getBlock( "reactor_air_vent" )
		};
		
		BASE_CORNERS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" )
		};
		
		BASE_WALLS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" )
		};
		
		BASE_INTERIORS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" ),
				TinyReactorsAPI.getBlock( "reactor_preigniter" )
		};
		
		CENTRAL_CORNERS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" ),
				TinyReactorsAPI.getBlock( "reactor_controller" ),
				TinyReactorsAPI.getBlock( "reactor_transfer_port" ),
				TinyReactorsAPI.getBlock( "reactor_surge_protector" )
		};
		
		CENTRAL_WALLS = new Block[] {
				TinyReactorsAPI.getBlock( "reactor_casing" ),
				TinyReactorsAPI.getBlock( "reactor_glass" ),
				TinyReactorsAPI.getBlock( "reactor_controller" ),
				TinyReactorsAPI.getBlock( "reactor_transfer_port" ),
				TinyReactorsAPI.getBlock( "reactor_heat_sink" ),
				TinyReactorsAPI.getBlock( "reactor_surge_protector" )
		};
	}
	
}
