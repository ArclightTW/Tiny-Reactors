package com.immersiveworks.tinyreactors.common.blocks;

import java.util.List;

import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPlanner;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReactorPlanner extends BlockTinyTile<TileEntityReactorPlanner> {

	public BlockReactorPlanner() {
		super( Material.IRON, TileEntityReactorPlanner.class );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
	}
	
	@Override
	public void addInformation( ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced ) {
		tooltip.add( "W.I.P - DO NOT USE" );
	}
	
	@Override
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ ) {
		if( !player.getHeldItemMainhand().isEmpty() )
			return false;
		
		TileEntityReactorPlanner planner = getTileEntity( world, pos );
		planner.amendSize( facing );
		return true;
	}
	
}
