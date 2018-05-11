package com.immersiveworks.tinyreactors.api;

import com.immersiveworks.tinyreactors.api.util.Registries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class TinyReactorsAPI {

	/**
	 * Used to retreive a Tiny Reactors block from the relevant registry
	 * @param name
	 * @return
	 */
	public static Block getBlock( String name ) {
		return Registries.BLOCKS.getEntry( name );
	}
	
	/**
	 * Used to retreive a Tiny Reactors item from the relevant registry
	 * @param name
	 * @return
	 */
	public static Item getItem( String name ) {
		return Registries.ITEMS.getEntry( name );
	}
	
}
