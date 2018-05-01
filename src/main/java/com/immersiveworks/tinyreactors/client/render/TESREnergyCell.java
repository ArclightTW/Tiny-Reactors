package com.immersiveworks.tinyreactors.client.render;

import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyCell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class TESREnergyCell extends TileEntitySpecialRenderer<TileEntityEnergyCell> {

	@Override
	public void render( TileEntityEnergyCell cell, double x, double y, double z, float partialTicks, int destroyStage, float alpha ) {
		if( Minecraft.getMinecraft().player.getHeldItem( EnumHand.MAIN_HAND ).getItem() != Items.TINY_WRENCH && Minecraft.getMinecraft().player.getHeldItem( EnumHand.OFF_HAND ).getItem() != Items.TINY_WRENCH )
			return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate( x + 0.5, y + 0.5, z + 0.5 );
		
		for( EnumFacing facing : EnumFacing.VALUES ) {
			String display = cell.canInput( facing ) ? "Input" : "Output";
			
			GlStateManager.pushMatrix();

			GlStateManager.rotate( -facing.getHorizontalAngle(), 0, 1, 0 );
			
			if( facing == EnumFacing.UP ) {
				GlStateManager.rotate( -90, 1, 0, 0 );
				GlStateManager.rotate( -90, 0, 0, 1 );
			}
			
			if( facing == EnumFacing.DOWN ) {
				GlStateManager.rotate( -90, 1, 0, 0 );
				GlStateManager.rotate( 90, 0, 0, 1 );
				GlStateManager.rotate( 180, 0, 1, 0 );
				
				GlStateManager.translate( 0, 0.5, 0.51 );
				GlStateManager.scale( 0.01, -0.01, 0.01 );
				
				GlStateManager.pushMatrix();
				GlStateManager.rotate( 180, 0, 0, 1 );
				getFontRenderer().drawString( display, -getFontRenderer().getStringWidth( display ) / 2,  -getFontRenderer().FONT_HEIGHT, 0xFF0000 );
				GlStateManager.popMatrix();
			}
			else
			{
				GlStateManager.translate( 0, 0.48, 0.51 );
				GlStateManager.scale( 0.01, -0.01, 0.01 );
				getFontRenderer().drawString( display, -getFontRenderer().getStringWidth( display ) / 2, 0, 0xFF0000 );
			}
			
			GlStateManager.popMatrix();
		}
		
		GlStateManager.popMatrix();
	}
	
}
