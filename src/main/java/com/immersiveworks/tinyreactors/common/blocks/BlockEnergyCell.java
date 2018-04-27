package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork;
import com.immersiveworks.tinyreactors.common.properties.EnumConnection;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyCell;

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

public class BlockEnergyCell extends BlockTinyTile<TileEntityEnergyCell> implements IEnergyNetworkBlockRenderer {

	private static final PropertyBool CORE = PropertyBool.create( "core" );
	
	public BlockEnergyCell() {
		super( Material.IRON, TileEntityEnergyCell.class );
		
		setHardness( 5F );
		setResistance( 15F );
		
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
		TileEntityEnergyCell cell = getTileEntity( world, pos );
		
		state = state.withProperty( CORE, true );
		
		for( EnumConnection connection : EnumConnection.VALUES )
			state = state.withProperty( connection.getProperty(), cell.canInput( connection.getFacing() ) );
		
		return state;
	}
	
	@Override
	public int getMetaFromState( IBlockState state ) {
		return 0;
	}

	@Override
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		TileEntityEnergyCell cell = getTileEntity( world, pos );
		cell.toggleInput( facing );
		
		if( !world.isRemote )
			EnergyNetwork.get( world ).refreshAll( world, null );
		
		return true;
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		return new String[] {
				String.format( "Power: %,d RF", getTileEntity( world, pos ).getInternalEnergy().getEnergyStored() ),
				String.format( "Capacity: %,d RF", getTileEntity( world, pos ).getInternalEnergy().getMaxEnergyStored() )
		};
	}
	
	@Override
	public void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed ) {
		if( !world.isRemote && pos == removed )
			getTileEntity( world, pos ).onBlockBreak();
	}
	
	@Override
	public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess source, BlockPos pos ) {
		return new AxisAlignedBB( 0F, 0F, 0F, 1F, 1F, 1F );
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
