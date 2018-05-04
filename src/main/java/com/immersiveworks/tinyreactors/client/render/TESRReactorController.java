package com.immersiveworks.tinyreactors.client.render;

import org.apache.commons.lang3.StringUtils;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorController;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TESRReactorController extends TileEntitySpecialRenderer<TileEntityReactorController> {

	@Override
	public void render( TileEntityReactorController controller, double x, double y, double z, float partialTicks, int destroyStage, float alpha ) {
		GlStateManager.pushMatrix();
		GlStateManager.translate( x + 0.5, y + 0.5, z + 0.5 );
		
		EnumFacing facing = controller.getWorld().getBlockState( controller.getPos() ).getValue( BlockDirectional.FACING );
		
		GlStateManager.pushMatrix();
		GlStateManager.depthMask( false );
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		
		GlStateManager.rotate( -facing.getHorizontalAngle(), 0, 1, 0 );
		GlStateManager.translate( -0.038, 0.285, 0.51 );
		GlStateManager.scale( 0.0025, -0.0025, 0.0025 );
		
		String[] display = new String[ 0 ];
		IBlockState state = controller.getWorld().getBlockState( controller.getPos() );
		
		if( state.getBlock() instanceof IEnergyNetworkBlockRenderer )
			display = ( ( IEnergyNetworkBlockRenderer )state.getBlock() ).getWrenchOverlayInfo( controller.getWorld(), controller.getPos(), state );
		
		int lineIndex = 0;
		
		for( int i = 0; i < display.length; i++ ) {
			if( StringUtils.isBlank( display[ i ] ) )
				continue;
			
			String[] lines = display[ i ].split( "\n" );
			for( int j = 0; j < lines.length; j++ ) {
				if( StringUtils.isBlank( lines[ j ] ) )
					continue;
				
				getFontRenderer().drawString( lines[ j ], 0, lineIndex * 10, 0xFFFFFF );
				lineIndex++;
			}
		}
		
		getFontRenderer().drawString( "------------------------------", 0, lineIndex * 10, 0xFFFFFF );
		
		int max = Math.min( 18 - lineIndex - 1, controller.getConsoleDisplay().size() );
		
		for( int i = 0; i < max; i++ )
			getFontRenderer().drawString( controller.getConsoleDisplay().get( controller.getConsoleDisplay().size() - 1 - i ), 0, ( lineIndex + max - i ) * 10, 0xFFFFFF );
		
		GlStateManager.enableLighting();
		GlStateManager.depthMask( true );
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
	
}
