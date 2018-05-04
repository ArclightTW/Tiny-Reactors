package com.immersiveworks.tinyreactors.client.render;

import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorSurgeProtector;

import net.minecraft.block.BlockDirectional;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TESRReactorSurgeProtector extends TileEntitySpecialRenderer<TileEntityReactorSurgeProtector> {

	@Override
	public void render( TileEntityReactorSurgeProtector surge, double x, double y, double z, float partialTicks, int destroyStage, float alpha ) {
		GlStateManager.pushMatrix();
		GlStateManager.translate( x + 0.5, y + 0.5, z + 0.5 );
		
		EnumFacing facing = surge.getWorld().getBlockState( surge.getPos() ).getValue( BlockDirectional.FACING );
		
		GlStateManager.pushMatrix();
		GlStateManager.depthMask( false );
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		
		GlStateManager.rotate( -facing.getHorizontalAngle(), 0, 1, 0 );
		GlStateManager.translate( -0.4, -0.195, 0.51 );
		GlStateManager.scale( 0.005, -0.005, 0.005 );
		
		getFontRenderer().drawString( "Minimum:", 0, 0, 0xFFFFFF );
		getFontRenderer().drawString( String.format( "%,d C", surge.getMinimumThreshold() ), 0, 10, 0xFFFFFF );
		
		String maximum = String.format( "%,d C", surge.getMaximumThreshold() );
		
		getFontRenderer().drawString( ":Maximum", 162 - getFontRenderer().getStringWidth( ":Maximum" ), 0, 0xFFFFFF );
		getFontRenderer().drawString( maximum, 162 - getFontRenderer().getStringWidth( maximum ), 10, 0xFFFFFF );
		
		GlStateManager.enableLighting();
		GlStateManager.depthMask( true );
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
	
}
