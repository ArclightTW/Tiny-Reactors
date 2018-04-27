package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.properties.EnumTransferPort;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorTransferPort;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReactorTransferPort extends BlockTinyTile<TileEntityReactorTransferPort> implements IEnergyNetworkBlockRenderer {

	public BlockReactorTransferPort() {
		super( Material.IRON, TileEntityReactorTransferPort.class );
		
		setDefaultState( blockState.getBaseState().withProperty( BlockDirectional.FACING, EnumFacing.NORTH ).withProperty( EnumTransferPort.PROPERTY, EnumTransferPort.ENERGY ) );
	}
	
	@Override
	public IProperty<?>[] getListedProperties() {
		return new IProperty<?>[] {
			BlockDirectional.FACING,
			EnumTransferPort.PROPERTY
		};
	}
	
	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess world, BlockPos pos ) {
		TileEntityReactorTransferPort transferPort = getTileEntity( world, pos );
		return state.withProperty( EnumTransferPort.PROPERTY, transferPort.getTransferMode() );
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
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		if( player.getHeldItemMainhand().getItem() == Items.TINY_WRENCH )
			return false;
		
		switch( getTileEntity( world, pos ).getTransferMode() ) {
		case ENERGY:
			break;
		case ITEM:
			ItemStack itemstack = player.getHeldItemMainhand();
			if( itemstack.isEmpty() ) {
				player.setHeldItem( EnumHand.MAIN_HAND, getTileEntity( world, pos ).retreiveReactant() );
				return true;
			}
			
			player.setHeldItem( EnumHand.MAIN_HAND, getTileEntity( world, pos ).insertReactant( itemstack ) );
			return true;
		case LIQUID:
			break;
		}
		
		return false;
	}
	
	@Override
	public void breakBlock( World world, BlockPos pos, IBlockState state ) {
		TileEntityReactorTransferPort transferPort = getTileEntity( world, pos );
		for( int i = 0; i < transferPort.getInternalItem().getSlots(); i++ )
			InventoryHelper.spawnItemStack( world, pos.getX(), pos.getY(), pos.getZ(), transferPort.getInternalItem().getStackInSlot( i ) );
		
		super.breakBlock( world, pos, state );
	}
	
	@Override
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		getTileEntity( world, pos ).toggleTransferMode();
		return true;
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		TileEntityReactorTransferPort transferPort = getTileEntity( world, pos );
		
		return new String[] {
				String.format( "Mode: %s", transferPort.getTransferMode().getDisplay() ),
				String.format( "Status: %s", transferPort.getStatus() ),
				String.format( "Power: %,d RF", transferPort.getInternalEnergy().getEnergyStored() )
		};
	}
	
}
