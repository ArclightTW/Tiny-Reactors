package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyRelay;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnergyRelay extends BlockTinyTile<TileEntityEnergyRelay> implements IEnergyNetworkBlockRenderer {

	public BlockEnergyRelay() {
		super( Material.IRON, TileEntityEnergyRelay.class );
		
		setHardness( 5F );
		setResistance( 15F );
		
		setLightLevel( 0.5F );
		setSoundType( SoundType.METAL );
		
		setDefaultState( blockState.getBaseState().withProperty( BlockDirectional.FACING, EnumFacing.NORTH ) );
	}
	
	@Override
	public IProperty<?>[] getListedProperties() {
		return new IProperty<?>[] {
			BlockDirectional.FACING
		};
	}
	
	@Override
	public IBlockState getStateForPlacement( World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand ) {
		return getStateFromMeta( facing.ordinal() );
	}
	
	@Override
	public IBlockState getStateFromMeta( int meta ) {
		return getDefaultState().withProperty( BlockDirectional.FACING, EnumFacing.getFront( meta ) );
	}
	
	@Override
	public int getMetaFromState( IBlockState state ) {
		return state.getValue( BlockDirectional.FACING ).getIndex();
	}
	
	@Override
	public IBlockState withRotation( IBlockState state, Rotation rot ) {
		return state.withProperty( BlockDirectional.FACING, rot.rotate( state.getValue( BlockDirectional.FACING ) ) );
	}
	
	@Override
	public IBlockState withMirror( IBlockState state, Mirror mirror ) {
		return withRotation( state, mirror.toRotation( state.getValue( BlockDirectional.FACING ) ) );
	}
	
	@Override
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		if( !world.isRemote ) {
			if( itemstack.getTagCompound().hasKey( "linkedPosition" ) )
				return false;
			
			itemstack.getTagCompound().setTag( "linkedPosition", NBTUtil.createPosTag( pos ) );
			player.sendMessage( new TextComponentString( "Success: Energy Relay position stored" ) );
		}
		
		return true;
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		return new String[] {
				String.format( "Power: %,d RF", getTileEntity( world, pos ).getInternalEnergy().getEnergyStored() ),
				String.format( "Capacity: %,d RF", getTileEntity( world, pos ).getInternalEnergy().getMaxEnergyStored() ),
				String.format( "Outgoing Connections: %,d", getTileEntity( world, pos ).getNumberDestinations() )
		};
	}
	
	@Override
	public void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed ) {
		TileEntityEnergyRelay relay = getTileEntity( world, pos );
		if( relay == null )
			return;
		
		if( !world.isRemote )
			relay.removeDestination( removed );
	}
	
	@Override
	@SuppressWarnings( "deprecation" )
	public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess source, BlockPos pos ) {
		IBlockState actual = source.getBlockState( pos );
		if( actual.getBlock() != Blocks.ENERGY_RELAY )
			return actual.getBlock().getBoundingBox( state, source, pos );
		
		switch( actual.getValue( BlockDirectional.FACING ) ) {
		case UP:
			return new AxisAlignedBB( 0.3F, 0F, 0.3F, 0.7F, 0.7F, 0.7F );
		case DOWN:
			return new AxisAlignedBB( 0.3F, 0.3F, 0.3F, 0.7F, 1F, 0.7F );
			
		case NORTH:
			return new AxisAlignedBB( 0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 1F );
		case SOUTH:
			return new AxisAlignedBB( 0.3F, 0.3F, 0F, 0.7F, 0.7F, 0.7F );
			
		case EAST:
			return new AxisAlignedBB( 0F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F );
		case WEST:
			return new AxisAlignedBB( 0.3F, 0.3F, 0.3F, 1F, 0.7F, 0.7F );
		}
		
		return new AxisAlignedBB( 0, 0, 0, 1, 1, 1 );
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
