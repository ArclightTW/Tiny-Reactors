package com.immersiveworks.tinyreactors.common.blocks;

import java.util.List;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.properties.EnumAirVent;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorAirVent;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

// TODO: Make texture look likes it's spinning, not spazzing
public class BlockReactorAirVent extends BlockTinyTile<TileEntityReactorAirVent> implements IEnergyNetworkBlockRenderer {

	private static final PropertyBool OPERATIONAL = PropertyBool.create( "operational" );
	
	public BlockReactorAirVent() {
		super( Material.IRON, TileEntityReactorAirVent.class );
		setDefaultState( blockState.getBaseState().withProperty( OPERATIONAL, false ).withProperty( EnumAirVent.PROPERTY, EnumAirVent.EMPTY ) );
	}
	
	@Override
	public IProperty<?>[] getListedProperties() {
		return new IProperty<?>[] {
			OPERATIONAL,
			EnumAirVent.PROPERTY
		};
	}
	
	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess world, BlockPos pos ) {
		TileEntityReactorAirVent airVent = getTileEntity( world, pos );
		return state.withProperty( OPERATIONAL, airVent.isOperational() ).withProperty( EnumAirVent.PROPERTY, airVent.getVentType() );
	}
	
	@Override
	public int getMetaFromState( IBlockState state ) {
		return 0;
	}
	
	@Override
	public boolean isOpaqueCube( IBlockState state ) {
		return false;
	}
	
	@Override
	public boolean isFullCube( IBlockState state ) {
		return false;
	}
	
	@Override
	public void addInformation( ItemStack itemstack, World player, List<String> tooltip, ITooltipFlag advanced ) {
		if( !Configs.REACTOR_TEMPERATURE )
			tooltip.add( "Reactors do not generate heat." );
		else if( !Configs.REACTOR_AIR_VENT )
			tooltip.add( "Air Vents do not decrease temperature." );
		
		if( !Configs.REACTOR_TEMPERATURE || !Configs.REACTOR_AIR_VENT )
			tooltip.add( TextFormatting.RED + "This block has no operational use with current configuration settings." );
	}
	
	@Override
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		getTileEntity( world, pos ).incrementTier();
		return true;
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		TileEntityReactorAirVent airVent = getTileEntity( world, pos );
		if( airVent.getVentType() == EnumAirVent.EMPTY )
			return new String[] { "Missing Vent Component" };
		
		return new String[] {
				String.format( "Component: %s", airVent.getVentType() ),
				String.format( "Operational: %s", airVent.isOperational() )
		};
	}
	
	@Override
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		if( !world.isRemote && player.isSneaking() ) {
			getTileEntity( world, pos ).toggleOperational();
			return true;
		}
		
		return false;
	}
	
}
