package com.immersiveworks.tinyreactors.common.util;

import java.util.Map;

import com.google.common.collect.Maps;
import com.immersiveworks.tinyreactors.common.inits.Configs;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Reactants {

	private static Map<ResourceLocation, Reactant> reactants = Maps.newHashMap();
	
	public static void populate() {
		reactants.clear();
		
		for( int i = 0; i < Configs.REACTANTS.length; i++ ) {
			String line = Configs.REACTANTS[ i ];
			line = line.replaceAll( " ", "" );
			
			String[] parts = line.split( "=" );
			if( parts.length != 2 ) {
				System.err.println( String.format( "Warning: Tiny Reactors cannot parse the reactant entry '%s' as there are more or less than 1 '='.", line ) );
				continue;
			}
			
			String reactant = parts[ 0 ];
			String rate = parts[ 1 ];
			String metadata = "-1";
			
			if( reactant.contains( "[" ) ) {
				if( !reactant.contains( "]" ) ) {
					System.err.println( String.format( "Warning: Tiny Reactors cannot parse the reactant entry '%s' as the metadata is not defined in a valid format", line ) );
					continue;
				}
				
				metadata = reactant.substring( reactant.indexOf( "[" ) + 1, reactant.indexOf( "]" ) );
				reactant = reactant.substring( 0, reactant.indexOf( "[" ) );
			}
			
			try {
				Integer.parseInt( rate );
			}
			catch( NumberFormatException e ) {
				System.err.println( String.format( "Warning: Tiny Reactors cannot parse the reactant entry '%s' as the provided rate '%s' is not a valid integer", line, rate ) );
				continue;
			}
			
			try {
				Integer.parseInt( metadata );
			}
			catch( NumberFormatException e ) {
				System.err.println( String.format( "Warning: Tiny Reactors cannot parse the reactant entry '%s' as the provided metadata '%s' is not a valid integer", line, metadata ) );
				continue;
			}
			
			Reactant r = new Reactant( reactant, Integer.parseInt( metadata ), Integer.parseInt( rate ) );
			reactants.put( r.name, r );
		}
	}
	
	public static int getReactantRate( IBlockState state ) {
		ResourceLocation name = state.getBlock().getRegistryName();
		if( !reactants.containsKey( name ) )
			return 0;
		
		Reactant reactant = reactants.get( name );
		return reactant.metadata == -1 || reactant.metadata == state.getBlock().getMetaFromState( state ) ? reactant.energy : 0;
	}
	
	@SuppressWarnings( "deprecation" )
	public static int getReactantRate( ItemStack itemstack ) {
		Block block = Block.getBlockFromItem( itemstack.getItem() );
		if( block == null )
			return 0;
		
		return getReactantRate( block.getStateFromMeta( itemstack.getItemDamage() ) );
	}
	
	public static boolean isValidReactant( IBlockState state ) {
		return getReactantRate( state ) > 0;
	}
	
	public static boolean isValidReactant( ItemStack itemstack ) {
		return getReactantRate( itemstack ) > 0;
	}
	
	private static class Reactant {
		
		private ResourceLocation name;
		private int metadata;
		private int energy;
		
		private Reactant( String name, int metadata, int energy ) {
			this.name = new ResourceLocation( name );
			this.metadata = metadata;
			this.energy = energy;
		}
		
	}
	
}
