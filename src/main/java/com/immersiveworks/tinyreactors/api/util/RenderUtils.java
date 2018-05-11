package com.immersiveworks.tinyreactors.api.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import net.minecraft.util.text.TextFormatting;
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
		drawTexturedModalRect( bounds, textureX, textureY, 1F );
	}
	
	public static void drawTexturedModalRect( Rectangle bounds, int textureX, int textureY, float scale ) {
		drawTexturedModalRect( bounds.getX(), bounds.getY(), textureX, textureY, bounds.getWidth(), bounds.getHeight(), scale );
	}
	
	public static void drawTexturedModalRect( int x, int y, int textureX, int textureY, int width, int height ) {
		drawTexturedModalRect( x, y, textureX, textureY, width, height, 1F );
	}
	
	public static void drawTexturedModalRect( int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, float scale ) {
		int posWidth = ( int )( textureWidth * scale );
		int posHeight = ( int )( textureHeight * scale );
		
        float texScalar = 0.00390625F;
        
        GlStateManager.pushMatrix();
        GlStateManager.scale( scale, scale, scale );
        
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();
        
        bb.begin( 7, DefaultVertexFormats.POSITION_TEX );
        bb.pos( ( x / scale ) + 0, ( y / scale ) + posHeight, 0 ).tex( ( textureX + 0 ) * texScalar, ( textureY + textureHeight ) * texScalar ).endVertex();
        bb.pos( ( x / scale ) + posWidth, ( y / scale ) + posHeight, 0 ).tex( ( textureX + textureWidth ) * texScalar, ( textureY + textureHeight ) * texScalar ).endVertex();
        bb.pos( ( x / scale ) + posWidth, ( y / scale ) + 0, 0 ).tex( ( textureX + textureWidth ) * texScalar, ( textureY + 0 ) * texScalar ).endVertex();
        bb.pos( ( x / scale ) + 0, ( y / scale ) + 0, 0 ).tex( ( textureX + 0 ) * texScalar, ( textureY + 0 ) * texScalar ).endVertex();
        tess.draw();

        GlStateManager.scale( 1F / scale, 1F / scale, 1F / scale );        
        GlStateManager.popMatrix();
    }
	
	public static String[] splitLines( String string, int wrapLength, float scale  ) {
		return splitLines( string, null, wrapLength, scale );
	}
	
	public static String[] splitLines( String string, TextFormatting format, int wrapLength, float scale  ) {
		List<String> totalLines = Lists.newLinkedList();
		
		String[] words = string.split( "\\s+" );
		
		for( int i = 0; i < words.length; i++ )
			string = string.replace( words[ i ], new TextComponentTranslation( words[ i ] ).getFormattedText() + ( format != null ? format : "" ) );
		
		string = string.replaceAll( "<link>", "" + TextFormatting.ITALIC + TextFormatting.UNDERLINE );
		string = string.replaceAll( "</link>", "" + TextFormatting.RESET + ( format == null ? "" : format ) );
		
		List<String> wrapped = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth( string, wrapLength == -1 ? 1000 : wrapLength );
		
		for( int i = 0; i < wrapped.size(); i++ ) {
			if( StringUtils.isBlank( wrapped.get( i ) ) || wrapped.get( i ) == "§r" )
				continue;
			
			totalLines.add( wrapped.get( i ) );
		}
		
		return totalLines.toArray( new String[ totalLines.size() ] );
	}
	
	public static Rectangle drawString( String string, Alignment alignment, int x, int y, int color ) {
		return drawString( string, null, alignment, x, y, color );
	}
	
	public static Rectangle drawString( String string, TextFormatting format, Alignment alignment, int x, int y, int color ) {
		return drawString( string, format, alignment, false, x, y, color );
	}
	
	public static Rectangle drawString( String string, Alignment alignment, boolean anchorBottom, int x, int y, int color ) {
		return drawString( string, null, alignment, anchorBottom, x, y, color );
	}
	
	public static Rectangle drawString( String string, TextFormatting format, Alignment alignment, boolean anchorBottom, int x, int y, int color ) {
		return drawString( string, format, alignment, anchorBottom, x, y, color, 1F );
	}
	
	public static Rectangle drawString( String string, Alignment alignment, int x, int y, int color, float scale ) {
		return drawString( string, null, alignment, x, y, color, scale );
	}
	
	public static Rectangle drawString( String string, TextFormatting format, Alignment alignment, int x, int y, int color, float scale ) {
		return drawString( string, format, alignment, false, x, y, color, scale );
	}
	
	public static Rectangle drawString( String string, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale ) {
		return drawString( string, null, alignment, anchorBottom, x, y, color, scale );
	}
	
	public static Rectangle drawString( String string, TextFormatting format, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale ) {
		return drawStringInternal( string, format, alignment, anchorBottom, x, y, color, scale, StringRenderType.CONTINUOUS, -1 );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, int x, int y, int color ) {
		return drawStringWrapped( string, null, wrapLength, alignment, x, y, color );
	}
	
	public static Rectangle drawStringWrapped( String string, TextFormatting format, int wrapLength, Alignment alignment, int x, int y, int color ) {
		return drawStringWrapped( string, format, wrapLength, alignment, false, x, y, color );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, boolean anchorBottom, int x, int y, int color ) {
		return drawStringWrapped( string, null, wrapLength, alignment, anchorBottom, x, y, color );
	}
	
	public static Rectangle drawStringWrapped( String string, TextFormatting format, int wrapLength, Alignment alignment, boolean anchorBottom, int x, int y, int color ) {
		return drawStringWrapped( string, format, wrapLength, alignment, anchorBottom, x, y, color, 1F );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, int x, int y, int color, float scale ) {
		return drawStringWrapped( string, null, wrapLength, alignment, x, y, color, scale );
	}
	
	public static Rectangle drawStringWrapped( String string, TextFormatting format, int wrapLength, Alignment alignment, int x, int y, int color, float scale ) {
		return drawStringWrapped( string, format, wrapLength, alignment, false, x, y, color, scale );
	}
	
	public static Rectangle drawStringWrapped( String string, int wrapLength, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale ) {
		return drawStringWrapped( string, null, wrapLength, alignment, false, x, y, color, scale );
	}
	
	public static Rectangle drawStringWrapped( String string, TextFormatting format, int wrapLength, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale ) {
		return drawStringInternal( string, format, alignment, anchorBottom, x, y, color, scale, StringRenderType.WRAPPED, wrapLength );
	}
	
	private static Rectangle drawStringInternal( String string, TextFormatting format, Alignment alignment, boolean anchorBottom, int x, int y, int color, float scale, StringRenderType type, int wrapLength ) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxWidth = Integer.MIN_VALUE;
		int height = 0;
		
		GlStateManager.pushMatrix();
		
		int lineIndex = 0;
		String[] splitLines = splitLines( string, format, wrapLength, scale );
		
		int offsetY = anchorBottom ? splitLines.length * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) : 0;
		
		for( int i = 0; i < splitLines.length; i++ ) {
			String splitLine = String.format( "%s%s", format != null ? format : "", splitLines[ i ] );
			
			int currentWidth = ( int )( Minecraft.getMinecraft().fontRenderer.getStringWidth( splitLine ) / scale );
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
			
			Minecraft.getMinecraft().fontRenderer.drawString( splitLine, xPos, yPos, color );
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
