package com.immersiveworks.tinyreactors.common.blocks;

import java.util.List;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorHeatSink;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockReactorHeatSink extends BlockTinyTile<TileEntityReactorHeatSink> implements IEnergyNetworkBlockRenderer {

	public BlockReactorHeatSink() {
		super( Material.IRON, TileEntityReactorHeatSink.class );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
	}
	
	@Override
	public void addInformation( ItemStack itemstack, World player, List<String> tooltip, ITooltipFlag advanced ) {
		if( !Configs.REACTOR_TEMPERATURE )
			tooltip.add( "Reactors do not generate heat." );
		else if( !Configs.REACTOR_HEAT_SINK )
			tooltip.add( "Heat Sinks do not increase temperature threshold." );
		
		if( !Configs.REACTOR_TEMPERATURE || !Configs.REACTOR_HEAT_SINK )
			tooltip.add( TextFormatting.RED + "This block has no operational use with current configuration settings." );
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		if( !Configs.REACTOR_TEMPERATURE || !Configs.REACTOR_HEAT_SINK )
			return new String[] { TextFormatting.RED + "No operational usage" };
		
		return new String[] { };
	}
	
}
