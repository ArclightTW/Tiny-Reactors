package com.immersiveworks.tinyreactors.common.util;

public class HashHelper {

	public static int generateHashCode( Object... fields ) {
		int hash = 227;
		
		for( Object field : fields ) {
			int c = 0;
			
			if( field instanceof Boolean )
				c = ( Boolean )field ? 0 : 1;
			else if( field instanceof Byte || field instanceof Character || field instanceof String || field instanceof Integer )
				c = ( int )field;
			else if( field instanceof Long )
				c = ( int )( ( Long )field ^ ( ( Long )field >>> 32 ) );
			else if( field instanceof Float )
				c = Float.floatToIntBits( ( Float )field );
			else if( field instanceof Double ) {
				long l = Double.doubleToLongBits( ( Double )field );
				c = ( int )( l ^ ( l >>> 32 ) );
			}
			else if( field instanceof Object[] )
				c = generateHashCode( ( Object[] )field );
			else
				c = field == null ? 0 : field.hashCode();
			
			hash = 37 * hash + c;
		}
		
		return hash;
	}
	
}
