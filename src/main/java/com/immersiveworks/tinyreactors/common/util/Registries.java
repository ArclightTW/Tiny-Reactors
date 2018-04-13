package com.immersiveworks.tinyreactors.common.util;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.function.BiConsumer;

import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public class Registries {

	public static Registry<Block> BLOCKS = new Registry<>();
	public static Registry<Item> ITEMS = new Registry<>();
	public static Registry<Entity> ENTITIES = new Registry<>();
	
	public static class Registry<T> {
		
		public int counter;
		
		@SuppressWarnings( "unchecked" )
		public void registerAll( Class<?> clazz, BiConsumer<T, String> registrar ) {
			for( Field field : clazz.getDeclaredFields() ) {
				try {
					Object value = field.get( null );
					if( value == null )
						continue;
					
					T obj = ( T )value;
					counter++;
					registrar.accept( obj, String.format( "%s:%s", TinyReactors.ID, field.getName().toLowerCase( Locale.ENGLISH ) ) );
				}
				catch( Exception e ) {
					
				}
			}
		}
		
	}
	
}
