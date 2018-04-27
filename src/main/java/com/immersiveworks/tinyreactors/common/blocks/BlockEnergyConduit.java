package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.properties.EnumConnection;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyConduit;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnergyConduit extends BlockTinyTile<TileEntityEnergyConduit> {

	private static final PropertyBool CORE = PropertyBool.create( "core" );
	
	public BlockEnergyConduit() {
		super( Material.IRON, TileEntityEnergyConduit.class );
		
		setHardness( 2F );
		setResistance( 6F );
		
		setSoundType( SoundType.METAL );
		
		IBlockState state = blockState.getBaseState();
		state = state.withProperty( CORE, true );
		
		for( EnumConnection connection : EnumConnection.VALUES )
			state = state.withProperty( connection.getProperty(), false );
		
		setDefaultState( state );
	}
	
	@Override
	public IProperty<?>[] getListedProperties() {
		IProperty<?>[] properties = new IProperty<?>[ 1 + EnumConnection.VALUES.length ];
		properties[ 0 ] = CORE;
		
		for( EnumConnection connection : EnumConnection.VALUES )
			properties[ connection.ordinal() + 1 ] = connection.getProperty();
		
		return properties;
	}
	
	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess world, BlockPos pos ) {
		state = state.withProperty( CORE, true );
		
		for( EnumConnection connection : EnumConnection.VALUES )
			state = state.withProperty( connection.getProperty(), connection.canConnect( world, pos ) );
		
		return state;
	}
	
	@Override
	public int getMetaFromState( IBlockState state ) {
		return 0;
	}
	
	@Override
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		return true;
	}
	
	@Override
	public void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed ) {
	}
	
	@Override
	@SuppressWarnings( "deprecation" )
	public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess source, BlockPos pos ) {
		IBlockState actual = source.getBlockState( pos );
		if( actual.getBlock() != Blocks.ENERGY_CONDUIT )
			return actual.getBlock().getBoundingBox( state, source, pos );
		
		actual = actual.getBlock().getActualState( actual, source, pos );
		
		AxisAlignedBB bounding = new AxisAlignedBB( 5 / 16F, 5 / 16F, 5 / 16F, 11 / 16F, 11 / 16F, 11 / 16F );
		for( EnumConnection connection : EnumConnection.VALUES ) {
			Boolean link = actual.getValue( connection.getProperty() );
			if( link == null || !link )
				continue;
			
			bounding = connection.expand( bounding );
		}
		
		return bounding;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox( IBlockState state, IBlockAccess world, BlockPos pos ) {
		return getBoundingBox( state, world, pos );
	}
	
	@Override
	public boolean isOpaqueCube( IBlockState state ) {
		return false;
	}
	
	@Override
	public boolean isFullCube( IBlockState state ) {
		return false;
	}
	
}
