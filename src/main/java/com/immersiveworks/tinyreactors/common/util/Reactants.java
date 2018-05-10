package com.immersiveworks.tinyreactors.common.util;

import java.util.Map;

import com.google.common.collect.Maps;
import com.immersiveworks.tinyreactors.common.inits.Configs;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Reactants {

	private static Map<String, Reactant> reactants = Maps.newHashMap();
	
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
			
			Reactant r = null;
			
			if( reactants.containsKey( reactant ) )
				r = reactants.get( reactant );
			else
				r = new Reactant( reactant );
			
			r.addRate( Integer.parseInt( metadata ), Integer.parseInt( rate ) );			
			reactants.put( reactant, r );
		}
	}
	
	public static int getReactantRate( IBlockState state ) {
		ResourceLocation name = state.getBlock().getRegistryName();
		if( !reactants.containsKey( name.toString() ) )
			return 0;
		
		return reactants.get( name.toString() ).getRate( state.getBlock().getMetaFromState( state ) );
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
		private Map<Integer, Integer> rates;
		
		private Reactant( String name ) {
			this.name = new ResourceLocation( name );
			rates = Maps.newHashMap();
		}
		
		private void addRate( int metadata, int energy ) {
			rates.put( metadata, energy );
		}
		
		private int getRate( int metadata ) {
			if( !rates.containsKey( metadata ) ) {
				if( rates.containsKey( -1 ) )
					return rates.get( -1 );
				
				return 0;
			}
			
			return rates.get( metadata );
		}
		
		@Override
		public boolean equals( Object obj ) {
			if( !( obj instanceof Reactant ) )
				return false;
			
			Reactant other = ( Reactant )obj;
			return name.equals( other.name );
		}
		
		@Override
		public int hashCode() {
			return HashHelper.generateHashCode( name );
		}
		
	}
	
}
