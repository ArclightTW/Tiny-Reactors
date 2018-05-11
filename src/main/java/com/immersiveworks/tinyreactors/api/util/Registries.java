package com.immersiveworks.tinyreactors.api.util;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

public class Registries {

	public static Registry<Block> BLOCKS = new Registry<>();
	public static Registry<Item> ITEMS = new Registry<>();
	public static Registry<Entity> ENTITIES = new Registry<>();
	public static Registry<IRecipe> RECIPES = new Registry<>();
	
	public static class Registry<T> {
		
		public int counter;
		
		private Map<String, T> entries;
		
		public Registry() {
			entries = Maps.newHashMap();
		}
		
		public T getEntry( String name ) {
			if( !entries.containsKey( name ) )
				return null;
			
			return entries.get( name );
		}
		
		@SuppressWarnings( "unchecked" )
		public void iterateAll( Class<?> clazz, BiConsumer<T, String> action ) {
			for( Field field : clazz.getDeclaredFields() ) {
				try {
					Object value = field.get( null );
					if( value == null )
						continue;
					
					T obj = ( T )value;
					action.accept( obj, String.format( "%s:%s", "tinyreactors", field.getName().toLowerCase( Locale.ENGLISH ) ) );
				}
				catch( Exception e ) {
				}
			}
		}
		
		public void registerAll( Class<?> clazz, BiConsumer<T, String> registrar ) {
			iterateAll( clazz, ( entry, name ) -> {
				counter++;
				entries.put( name.substring( "tinyreactors:".length() ), entry );
				registrar.accept( entry, name );
			} );
		}
		
	}
	
}
