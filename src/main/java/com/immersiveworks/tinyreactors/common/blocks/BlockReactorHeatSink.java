package com.immersiveworks.tinyreactors.common.blocks;

import java.util.List;

import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorHeatSink;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockReactorHeatSink extends BlockTinyTile<TileEntityReactorHeatSink> {

	public BlockReactorHeatSink() {
		super( Material.IRON, TileEntityReactorHeatSink.class );
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
	
}
