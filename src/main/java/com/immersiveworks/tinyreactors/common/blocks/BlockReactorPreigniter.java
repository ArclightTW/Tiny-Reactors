package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPreigniter;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReactorPreigniter extends BlockTinyTile<TileEntityReactorPreigniter> {

	public BlockReactorPreigniter() {
		super( Material.IRON, TileEntityReactorPreigniter.class );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
	}
	
	@Override
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		if( player.getHeldItemMainhand().getItem() == Items.TINY_WRENCH )
			return false;
		
		ItemStack itemstack = player.getHeldItemMainhand();
		if( itemstack.isEmpty() ) {
			player.setHeldItem( EnumHand.MAIN_HAND, getTileEntity( world, pos ).retreiveCombustible() );
			return true;
		}
		
		player.setHeldItem( EnumHand.MAIN_HAND, getTileEntity( world, pos ).insertCombustible( itemstack ) );
		return true;
	}
	
	@Override
	public void breakBlock( World world, BlockPos pos, IBlockState state ) {
		TileEntityReactorPreigniter preigniter = getTileEntity( world, pos );
		for( int i = 0; i < preigniter.getInternalItem().getSlots(); i++ )
			InventoryHelper.spawnItemStack( world, pos.getX(), pos.getY(), pos.getZ(), preigniter.getInternalItem().getStackInSlot( i ) );
		
		super.breakBlock( world, pos, state );
	}
	
}
