package com.immersiveworks.tinyreactors.common.blocks;

import java.util.function.BiFunction;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTinyTile<T extends TileEntity> extends BlockTiny {

	private Class<T> clazz;
	private BiFunction<World, IBlockState, T> constructor;
	
	public BlockTinyTile( Material material, Class<T> clazz ) {
		this( material, clazz, ( world, state ) -> {
			try {
				return clazz.newInstance();
			}
			catch( Exception e ) {
				e.printStackTrace();
				return null;
			}
		} );
	}
	
	public BlockTinyTile( Material material, Class<T> clazz, BiFunction<World, IBlockState, T> constructor ) {
		super( material );
		
		this.clazz = clazz;
		this.constructor = constructor;
	}
	
	@Override
	public final TileEntity createTileEntity( World world, IBlockState state ) {
		return constructor.apply( world, state );
	}
	
	@Override
	public boolean hasTileEntity( IBlockState state ) {
		return clazz != null;
	}
	
	@SuppressWarnings( "unchecked" )
	public T getTileEntity( IBlockAccess world, BlockPos pos ) {
		try {
			return ( T )world.getTileEntity( pos );
		}
		catch( ClassCastException e ) {
			return null;
		}
	}
	
}
