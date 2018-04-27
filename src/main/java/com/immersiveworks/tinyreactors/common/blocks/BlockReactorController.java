package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork.Priority;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorController;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReactorController extends BlockTinyTile<TileEntityReactorController> implements IEnergyNetworkBlockRenderer {

	public BlockReactorController() {
		super( Material.IRON, TileEntityReactorController.class );
		
		setDefaultState( blockState.getBaseState().withProperty( BlockDirectional.FACING, EnumFacing.NORTH ) );
	}
	
	@Override
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		if( !world.isRemote ) {
			StorageReactor reactor = getTileEntity( world, pos ).getStructure();
			System.out.println(
					"Energy: " + reactor.getEnergyGain() + " RF/tick " +
					"(Multiplier: " + reactor.getEnergyMultiplier() + "x); " +
					"Temperature: " + reactor.getTemperature().getCurrentTemperature() + "C / " + reactor.getTemperature().getMaximumTemperature() + "C " +
					"(Gain: " + reactor.getTemperatureGain() + "C/tick / Cooldown: " + reactor.getTemperatureCooldown() + "C/tick) "
				);
		}
		
		return super.onBlockActivated( world, pos, state, player, facing, hitX, hitY, hitZ);
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
	public Priority getEnergyNetworkPriority() {
		return Priority.HIGH;
	}
	
	@Override
	public void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed ) {
		getTileEntity( world, pos ).getStructure().validateStructure( world, pos, removed );
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		return new String[] {
				String.format( "Operational: %s", getTileEntity( world, pos ).getStructure().isValid() )
		};
	}
	
}
