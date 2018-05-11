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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.TextTable.Alignment;

public class RenderUtils {

	private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
	
	public static void drawItemStack( ItemStack itemstack, int x, int y ) {
		drawItemStack( itemstack, x, y, 1F );
	}
	
	public static void drawItemStack( ItemStack itemstack, int x, int y, float scale ) {
		drawItemStack( itemstack, x, y, itemstack.getCount() <= 1 ? "" : String.format( "%,d", itemstack.getCount() ), scale );
	}
	
	public static void drawItemStack( ItemStack itemstack, int x, int y, String text ) {
		drawItemStack( itemstack, x, y, text, 1F );
	}
	
	public static void drawItemStack( ItemStack itemstack, int x, int y, String text, float scale ) {
		GlStateManager.pushMatrix();
		
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		
		GlStateManager.scale( scale, scale, scale );
		GlStateManager.translate( 0, 0, 32 );
		
		itemRender.zLevel = 200F;
		
		FontRenderer font = itemstack.getItem().getFontRenderer( itemstack );
		if( font == null )
			font = Minecraft.getMinecraft().fontRenderer;
		
		itemRender.renderItemAndEffectIntoGUI( itemstack, ( int )( x / scale ), ( int )( y / scale ) );
		itemRender.renderItemOverlayIntoGUI( font, itemstack, ( int )( x / scale ), ( int )( y / scale ), text );
		
		itemRender.zLevel = 0F;
		
		GlStateManager.scale( 1F / scale, 1F / scale, 1F / scale );
		
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		
		GlStateManager.popMatrix();
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
	
	// TODO: We should ONLY care about the WordUtils.wrap functionality - parsing strings with /n is no longer supported
	public static String[] splitLines( String string, int wrapLength, float scale ) {
		List<String> totalLines = Lists.newLinkedList();
		
		String[] lines = string.split( "\n" );
		for( int i = 0; i < lines.length; i++ ) {
			if( StringUtils.isBlank( lines[ i ] ) )
				continue;
			
			String line = new TextComponentTranslation( lines[ i ] ).getFormattedText();
			String[] wrapped;
			
			if( wrapLength == -1 )
				wrapped = new String[] { line };
			else
				wrapped = WordUtils.wrap( line,( int )( wrapLength / 6 / scale ) ).split( System.getProperty( "line.separator" ) );
	
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
	
	public static Rectangle drawString( String string, Alignment alignment, int x, int y, int color ) {
		return drawString( string, alignment, x, y, color, 1F );
	}
	
	public static Rectangle drawString( String string, Alignment alignment, boolean anchorBottom, int x, int y, int color ) {
		return drawString( string, alignment, anchorBottom, x, y, color, 1F );
	}
	
	public static Rectangle drawString( String string, Alignment alignment, int x, int y, int color, float scale ) {
		return drawString( string, alignment, false, x, y, color, scale );
	}
	
	public static Rectangle drawString( String string, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale ) {
		return drawStringInternal( string, alignment, anchorBottom, x, y, color, scale, StringRenderType.CONTINUOUS, -1 );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, int x, int y, int color ) {
		return drawStringWrapped( string, wrapLength, alignment, x, y, color, 1F );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, boolean anchorBottom, int x, int y, int color ) {
		return drawStringWrapped( string, wrapLength, alignment, anchorBottom, x, y, color, 1F );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, int x, int y, int color, float scale ) {
		return drawStringWrapped( string, wrapLength, alignment, false, x, y, color, scale );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale ) {
		return drawStringInternal( string, alignment, anchorBottom, x, y, color, scale, StringRenderType.WRAPPED, wrapLength );
	}
	
	private static Rectangle drawStringInternal( String string, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale, StringRenderType type, int wrapLength ) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxWidth = Integer.MIN_VALUE;
		int height = 0;
		
		GlStateManager.pushMatrix();
		
		int lineIndex = 0;
		String[] splitLines = splitLines( string, wrapLength, scale );
		
		int offsetY = anchorBottom ? splitLines.length * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) : 0;
		
		for( int i = 0; i < splitLines.length; i++ ) {
			int currentWidth = ( int )( Minecraft.getMinecraft().fontRenderer.getStringWidth( splitLines[ i ] ) / scale );
			if( currentWidth >= maxWidth )
				maxWidth = currentWidth;
			
			int offsetX = 0;
			switch( alignment ) {
			case LEFT:
				offsetX = 0;
				break;
			case CENTER:
				offsetX = ( int )( currentWidth / ( 2 / scale ) );
				break;
			case RIGHT:
				offsetX = ( int )( currentWidth * scale );
				break;
			}
			
			GlStateManager.scale( scale, scale, scale );
			
			int xPos = ( int )( ( x / scale ) - offsetX );
			if( xPos < minX )
				minX = xPos;
			
			int yOff = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3;
			int yPos = ( int )( y / scale ) + ( lineIndex * yOff ) - offsetY;
			if( yPos < minY )
				minY = yPos;
			
			height += yOff;
			
			Minecraft.getMinecraft().fontRenderer.drawString( splitLines[ i ], xPos, yPos, color );
			lineIndex++;
			
			GlStateManager.scale( 1 / scale, 1 / scale, 1 / scale );
		}
		
		GlStateManager.popMatrix();
		
		return new Rectangle( minX, minY, maxWidth, height );
	}
	
	private static enum StringRenderType {
		CONTINUOUS,
		WRAPPED
	}
	
}
