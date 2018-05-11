package com.immersiveworks.tinyreactors.api.helpers;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class ArrayHelper {

	public static String combine( String[] array, String separator ) {
		String combined = "";
		
		for( int i = 0; i < array.length; i++ )
			combined += array[ i ] + separator;
		
		if( array.length > 0 )
			combined = combined.substring( 0, combined.length() - separator.length() );
		
		return combined;
	}
	
	public static <T> List<T[]> split( T[] originalArray, int chunkSize ) {
		List<T[]> arrays = Lists.newLinkedList();
		
		int totalSize = originalArray.length;
		if( totalSize < chunkSize )
			chunkSize = totalSize;
		
		int from = 0;
		int to = chunkSize;
		
		while( from < totalSize ) {
			arrays.add( Arrays.copyOfRange( originalArray, from, to ) );
			
			from += chunkSize;
			to = from + chunkSize;
			
			if( to > totalSize )
				to = totalSize;
		}
		
		return arrays;
	}
	
}
