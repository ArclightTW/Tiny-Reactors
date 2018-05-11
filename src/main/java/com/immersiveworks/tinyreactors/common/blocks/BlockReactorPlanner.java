package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.api.manual.pages.ManualPage;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageTextDetails;
import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPlanner;

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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockReactorPlanner extends BlockTinyTile<TileEntityReactorPlanner> implements IEnergyNetworkBlockRenderer {

	public BlockReactorPlanner() {
		super( Material.IRON, TileEntityReactorPlanner.class );
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
	
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		if( !world.isRemote ) {
			TileEntityReactorPlanner planner = getTileEntity( world, pos );
			
			if( player.getHeldItemMainhand().isEmpty() ) {
				planner.toggleColorOverlay();
				player.sendMessage( new TextComponentString( String.format( "Overlay Mode: %s", planner.getColorOverlay() ) ) );
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, EntityPlayer player, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		TileEntityReactorPlanner planner = getTileEntity( world, pos );
		boolean invalid = planner.getMaxX() - planner.getMinX() + 1 < 3 || planner.getMaxY() - planner.getMinY() + 1 < 3 || planner.getMaxZ() - planner.getMinZ() + 1 < 3;
		
		return new String[] {
				String.format( "Click to %s", player.isSneaking() ? "shrink" : "expand" ),
				String.format( "Size: %,d x %,d x %,d", planner.getMaxX() - planner.getMinX() + 1, planner.getMaxY() - planner.getMinY() + 1, planner.getMaxZ() - planner.getMinZ() + 1 ),
				String.format( "%s%s", TextFormatting.RED, invalid ? "Invalid Reactor Size" : "" )
				};
	}
	
	@Override
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		if( !world.isRemote ) {
			getTileEntity( world, pos ).amendSize( facing, player.isSneaking() );
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getManualKey() {
		return "reactor_planner";
	}
	
	@Override
	public ManualPage[] getManualPages() {
		return new ManualPage[] {
				new ManualPageTextDetails( this )
		};
	}
	
}
