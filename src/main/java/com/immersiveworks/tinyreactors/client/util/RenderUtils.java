package com.immersiveworks.tinyreactors.client.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.util.Rectangle;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.common.util.TextTable.Alignment;

public class RenderUtils {

	private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
	
	public static void drawItemStack( ItemStack itemstack, int x, int y ) {
		drawItemStack( itemstack, x, y, String.format( "%,d", itemstack.getCount() ) );
	}
	
	public static void drawItemStack( ItemStack itemstack, int x, int y, String text ) {
		GlStateManager.translate( 0, 0, 32 );
		itemRender.zLevel = 200F;
		
		FontRenderer font = itemstack.getItem().getFontRenderer( itemstack );
		if( font == null )
			font = Minecraft.getMinecraft().fontRenderer;
		
		itemRender.renderItemAndEffectIntoGUI( itemstack, x, y );
		itemRender.renderItemOverlayIntoGUI( font, itemstack, x, y, text );
		
		itemRender.zLevel = 0F;
	}
	
	public static void drawTexturedModalRect( Rectangle bounds, int textureX, int textureY ) {
		drawTexturedModalRect( bounds.getX(), bounds.getY(), textureX, textureY, bounds.getWidth(), bounds.getHeight() );
	}
	
	public static void drawTexturedModalRect( int x, int y, int textureX, int textureY, int width, int height ) {
		GlStateManager.pushMatrix();
        float scale = 0.00390625F;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();
        
        bb.begin( 7, DefaultVertexFormats.POSITION_TEX );
        bb.pos( x + 0,  y + height, 0 ).tex( ( textureX + 0 ) * scale, ( textureY + height ) * scale ).endVertex();
        bb.pos( x + width, y + height, 0 ).tex( ( textureX + width ) * scale, ( textureY + height ) * scale ).endVertex();
        bb.pos( x + width, y + 0, 0 ).tex( ( textureX + width ) * scale, ( textureY + 0 ) * scale ).endVertex();
        bb.pos( x + 0, y + 0, 0 ).tex( ( textureX + 0 ) * scale, ( textureY + 0 ) * scale ).endVertex();
        tess.draw();
        GlStateManager.popMatrix();
    }
	
	public static String[] splitLines( String string, int wrapLength, float scale ) {
		List<String> totalLines = Lists.newLinkedList();
		
		String[] lines = string.split( "\n" );
		for( int i = 0; i < lines.length; i++ ) {
			if( StringUtils.isBlank( lines[ i ] ) )
				continue;

			String[] wrapped = WordUtils.wrap( lines[ i ],( int )( wrapLength / ( 6 * scale ) ) ).split( System.getProperty( "line.separator" ) );
	
			for( int j = 0; j < wrapped.length; j++ ) {
				if( StringUtils.isBlank( wrapped[ j ] ) )
					continue;
				
				String[] finalWrap = wrapped[ j ].split( "\n" );
				for( int k = 0; k < finalWrap.length; k++ ) {
					if( StringUtils.isBlank( finalWrap[ k ] ) )
						continue;
					
					totalLines.add( finalWrap[ k ] );
				}
			}
		}
		
		return totalLines.toArray( new String[ totalLines.size() ] );
	}
	
	// TODO: AMEND THE OVERLAY TEXT TO USE THIS NEW SCALABLE VERSION OF DRAW STRING
	public static RenderManualOutput drawString( String string, Alignment alignment, int x, int y, int color ) {
		return drawString( string, alignment, x, y, color, 1F );
	}
	
	public static RenderManualOutput drawString( String string, Alignment alignment, int x, int y, int color, float scale ) {
		return drawStringInternal( string, alignment, x, y, color, scale, StringRenderType.CONTINUOUS, -1 );
	}
	
	public static RenderManualOutput drawStringWrapped( String string, int wrapLength, Alignment alignment, int x, int y, int color ) {
		return drawStringWrapped( string, wrapLength, alignment, x, y, color, 1F );
	}
	
	public static RenderManualOutput drawStringWrapped( String string, int wrapLength, Alignment alignment, int x, int y, int color, float scale ) {
		return drawStringInternal( string, alignment, x, y, color, scale, StringRenderType.WRAPPED, wrapLength );
	}
	
	// TODO: Should use split lines function
	private static RenderManualOutput drawStringInternal( String string, Alignment alignment, int x, int y, int color, float scale, StringRenderType type, int wrapLength ) {
		int width = 0;
		
		GlStateManager.pushMatrix();
		
		int lineIndex = 0;
		
		String[] lines = string.split( "\n" );
		for( int i = 0; i < lines.length; i++ ) {
			if( StringUtils.isBlank( lines[ i ] ) )
				continue;
			
			int currentWidth = ( int )( Minecraft.getMinecraft().fontRenderer.getStringWidth( lines[ i ] ) / scale );
			if( currentWidth >= width )
				width = currentWidth;
			
			int offsetX = 0;
			switch( alignment ) {
			case LEFT:
				offsetX = 0;
				break;
			case CENTER:
				offsetX = ( int )( currentWidth / ( 2 / scale ) );
				break;
			case RIGHT:
				offsetX = currentWidth;
				break;
			}
			
			GlStateManager.scale( scale, scale, scale );

			switch( type ) {
			case CONTINUOUS:
				Minecraft.getMinecraft().fontRenderer.drawString( lines[ i ], ( int )( ( x / scale ) - offsetX ), ( int )( y / scale ) + ( lineIndex * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) ), color );
				lineIndex++;
				break;
			case WRAPPED:
				String[] wrapped = WordUtils.wrap( lines[ i ],( int )( wrapLength / ( 6 * scale ) ) ).split( System.getProperty( "line.separator" ) );
				for( int j = 0; j < wrapped.length; j++ ) {
					if( StringUtils.isBlank( wrapped[ j ] ) )
						continue;
					
					String[] finalWrap = wrapped[ j ].split( "\n" );
					for( int k = 0; k < finalWrap.length; k++ ) {
						if( StringUtils.isBlank( finalWrap[ k ] ) )
							continue;
						
						Minecraft.getMinecraft().fontRenderer.drawString( finalWrap[ k ], ( int )( ( x / scale ) - offsetX ), ( int )( y / scale ) + ( lineIndex * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) ), color );
						lineIndex++;
					}
				}
				
				break;
			}
			
			GlStateManager.scale( 1 / scale, 1 / scale, 1 / scale );
		}
		
		GlStateManager.popMatrix();
		
		return new RenderManualOutput( new Vec2f( width, lineIndex * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) ), lineIndex );
	}
	
	private static enum StringRenderType {
		CONTINUOUS,
		WRAPPED
	}
	
}
