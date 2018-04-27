package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.api.IWrenchable;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork.Priority;
import com.immersiveworks.tinyreactors.common.energy.IEnergyNetworkBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTiny extends Block implements IEnergyNetworkBlock, IWrenchable {

	public BlockTiny( Material material ) {
		super( material );
		setCreativeTab( TinyReactors.TAB );
	}
	
	@Override
	public Priority getEnergyNetworkPriority() {
		return Priority.NORMAL;
	}
	
	@Override
	public void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed ) {
	}
	
	@Override
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		return false;
	}
	
	@Override
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		if( hand == EnumHand.MAIN_HAND && onBlockActivated( world, pos, state, player, facing, hitX, hitY, hitZ ) )
			return true;
		
		return super.onBlockActivated( world, pos, state, player, hand, facing, hitX, hitY, hitZ );
	}
	
	@Override
	public final BlockStateContainer createBlockState() {
		return new ExtendedBlockState( this, getListedProperties(), getUnlistedProperties() );
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public EnumBlockRenderType getRenderType( IBlockState state ) {
		return EnumBlockRenderType.MODEL;
	}
	
	/**
	 * A single-use case of onBlockActivated - only called once and not once per hand per Vanilla code.
	 * @return true to prevent Vanilla handling (e.g block placement), false to continue as normal
	 */
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		return false;
	}
	
	public IProperty<?>[] getListedProperties()
	{
		return new IProperty<?>[ 0 ];
	}
	
	public IUnlistedProperty<?>[] getUnlistedProperties()
	{
		return new IUnlistedProperty<?>[ 0 ];
	}

}
