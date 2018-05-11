package com.immersiveworks.tinyreactors.api.manual.pages;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.api.helpers.ArrayHelper;
import com.immersiveworks.tinyreactors.api.manual.ITinyManual;
import com.immersiveworks.tinyreactors.api.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.TextTable.Alignment;

public class ManualPageImage extends ManualPage {

	protected ResourceLocation image;
	protected int srcX;
	protected int srcY;
	protected int width;
	protected int height;
	
	protected float scale;
	protected Alignment alignment;
	protected String tooltip;

	public ManualPageImage( String title, String image, int srcX, int srcY, int width, int height ) {
		this( title, image, srcX, srcY, width, height, 1F );
	}
	
	public ManualPageImage( String title, String image, int srcX, int srcY, int width, int height, float scale ) {
		super( title, "textures/gui/manual_page.png" );
		this.image = new ResourceLocation( image );
		this.srcX = srcX;
		this.srcY = srcY;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.alignment = Alignment.CENTER;
		this.tooltip = "";
	}
	
	@Override
	public void drawScreen( ITinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
		GlStateManager.pushMatrix();
		GlStateManager.color( 1, 1, 1, 1 );
		
		int offsetX = 0;
		switch( alignment ) {
		case LEFT:
			offsetX = 15;
			break;
		case CENTER:
			offsetX = ( int )( bounds.getWidth() / 2 - ( width * scale ) / ( 2 / scale ) );
			break;
		case RIGHT:
			offsetX = ( int )( bounds.getWidth() - ( width * scale ) - 15 );
			break;
		}
		
		Minecraft.getMinecraft().getTextureManager().bindTexture( image );
		RenderUtils.drawTexturedModalRect( bounds.getX() + offsetX, bounds.getY() + 32, srcX, srcY, width, height, scale );
		GlStateManager.popMatrix();
		
		drawContent( bounds );
	}
	
	@Override
	public boolean hasOverflow( Rectangle bounds ) {
		return drawContent( bounds ).getHeight() > bounds.getHeight() - ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) * 2;
	}
	
	@Override
	public String[] getSplitLines( Rectangle bounds ) {
		return RenderUtils.splitLines( tooltip, TextFormatting.ITALIC, bounds.getWidth() - 15, 0.75F );
	}
	
	@Override
	public ManualPage getNewInstance( String[] lines ) {
		return new ManualPageText( title, ArrayHelper.combine( lines, "\n" ) );
	}
	
	public ManualPageImage setAlignment( Alignment alignemnt ) {
		this.alignment = alignemnt;
		return this;
	}
	
	public ManualPageImage setTooltip( String tooltip ) {
		this.tooltip = tooltip;
		return this;
	}
	
	private Rectangle drawContent( Rectangle bounds ) {
		int posX = 0;
		switch( alignment ) {
		case LEFT:
			posX = 15;
			break;
		case CENTER:
			posX = bounds.getWidth() / 2;
			break;
		case RIGHT:
			posX = bounds.getWidth();
			break;
		}
		
		return RenderUtils.drawStringWrapped( tooltip, TextFormatting.ITALIC, bounds.getWidth() - 5, alignment, true, bounds.getX() + posX, bounds.getY() + bounds.getHeight() - 15, 0x999999, 0.6F );
	}
	
}
