package com.immersiveworks.tinyreactors.common.blocks;

import org.apache.commons.lang3.StringUtils;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorSurgeProtector;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReactorSurgeProtector extends BlockTinyTile<TileEntityReactorSurgeProtector> implements IEnergyNetworkBlockRenderer {

	public BlockReactorSurgeProtector() {
		super( Material.IRON, TileEntityReactorSurgeProtector.class );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
		
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
		return getDefaultState().withProperty( BlockDirectional.FACING, placer.getHorizontalFacing().getOpposite() );
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
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack, float hitX, float hitY, float hitZ ) {
		TileEntityReactorSurgeProtector surge = getTileEntity( world, pos );
		if( surge == null )
			return false;
		
		if( !world.isRemote ) {
			if( facing == EnumFacing.UP || facing == EnumFacing.DOWN )
				return false;
			
			switch( facing ) {
			case NORTH:
				if( hitY > 0.19F && hitY < 0.31F ) {
					if( hitX > 0.08F && hitX < 0.31F )
						surge.setMaximumThreshold( surge.getMaximumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
					if( hitX > 0.69F && hitX < 0.93F )
						surge.setMinimumThreshold( surge.getMinimumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
				}
				
				break;
			case SOUTH:
				if( hitY > 0.19F && hitY < 0.31F ) {
					if( hitX > 0.08F && hitX < 0.31F )
						surge.setMinimumThreshold( surge.getMinimumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
					if( hitX > 0.69F && hitX < 0.93F )
						surge.setMaximumThreshold( surge.getMaximumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
				}
				
				break;
			case EAST:
				if( hitY > 0.19F && hitY < 0.31F ) {
					if( hitZ > 0.08F && hitZ < 0.31F )
						surge.setMaximumThreshold( surge.getMaximumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
					if( hitZ > 0.69F && hitZ < 0.93F )
						surge.setMinimumThreshold( surge.getMinimumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
				}
				
				break;
			case WEST:
				if( hitY > 0.19F && hitY < 0.31F ) {
					if( hitZ > 0.08F && hitZ < 0.31F )
						surge.setMinimumThreshold( surge.getMinimumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
					if( hitZ > 0.69F && hitZ < 0.93F )
						surge.setMaximumThreshold( surge.getMaximumThreshold() + ( player.isSneaking() ? -50 : 50 ) );
				}
				
				break;
			default:
				return false;
			}
		}
		
		return false;
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, EntityPlayer player, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		TileEntityReactorSurgeProtector surge = getTileEntity( world, pos );
		boolean minThreshold = false;
		String wrenchEffect = "";
		
		if( state.getValue( BlockDirectional.FACING ) == facing && hitY < -0.19F && hitY > -0.31F ) {
			switch( facing ) {
			case NORTH:
				if( ( hitX < -0.08F && hitX > -0.31F ) || ( hitX < -0.69F && hitX > -0.93F ) ) {
					wrenchEffect = String.format( "Click to %s", player.isSneaking() ? "decrease" : "increase" );
					minThreshold = hitX < -0.69F && hitX > -0.93F;
				}
				break;
			case SOUTH:
				if( ( hitX < -0.08F && hitX > -0.31F ) || ( hitX < -0.69F && hitX > -0.93F ) ) {
					wrenchEffect = String.format( "Click to %s", player.isSneaking() ? "decrease" : "increase" );
					minThreshold = hitX < -0.08F && hitX > -0.31F;
				}
				break;
			case EAST:
				if( ( hitZ < -0.08F && hitZ > -0.31F ) || ( hitZ < -0.69F && hitZ > -0.93F ) ) {
					wrenchEffect = String.format( "Click to %s", player.isSneaking() ? "decrease" : "increase" );
					minThreshold = hitZ < -0.69F && hitZ > -0.93F;
				}
				break;
			case WEST:
				if( ( hitZ < -0.08F && hitZ > -0.31F ) || ( hitZ < -0.69F && hitZ > -0.93F ) ) {
					wrenchEffect = String.format( "Click to %s", player.isSneaking() ? "decrease" : "increase" );
					minThreshold = hitZ < -0.08F && hitZ > -0.31F;
				}
				break;
			default:
				break;
			}
		}
		
		if( !StringUtils.isBlank( wrenchEffect ) ) {
			if( minThreshold ) {
				if( player.isSneaking() && surge.getMinimumThreshold() == 0 )
					wrenchEffect = "Cannot decrease threshold";
					
				if( !player.isSneaking() && surge.getMinimumThreshold() >= surge.getMaximumThreshold() - 50 )
					wrenchEffect = "Cannot increase threshold";
			}
			else {
				if( player.isSneaking() && surge.getMaximumThreshold() <= surge.getMinimumThreshold() + 50 )
					wrenchEffect = "Cannot decrease threshold";
				
				if( !player.isSneaking() && surge.getController() != null && surge.getMaximumThreshold() == surge.getController().getStructure().getTemperature().getMaximumTemperature() )
					wrenchEffect = "Cannot increase threshold";
			}
		}
		
		return new String[] {
				wrenchEffect,
				String.format( "Minimum Threshold: %,d C", surge.getMinimumThreshold() ),
				String.format( "Maximum Threshold: %,d C", surge.getMaximumThreshold() )
			};
	}
	
}
