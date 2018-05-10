package com.immersiveworks.tinyreactors.client.gui.manual.pages;

import java.util.List;

import org.lwjgl.util.Rectangle;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.client.gui.manual.GuiTinyManual;
import com.immersiveworks.tinyreactors.client.util.RenderManualOutput;
import com.immersiveworks.tinyreactors.client.util.RenderUtils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.util.TextTable.Alignment;

public class ManualPageText extends ManualPage {

	private String content;
	private RenderManualOutput render;
	
	public ManualPageText( String title, String... content ) {
		super( title, "textures/gui/manual_page.png" );
		
		for( int i = 0; i < content.length; i++ )
			this.content += content[ i ] + "\n";
		
		this.content = this.content.concat( this.content );
	}
	
	@Override
	public void drawScreen( GuiTinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
		drawContent( bounds );
	}
	
	@Override
	public ManualPage[] getOverflowPages( Rectangle bounds ) {
		if( !hasOverflow( bounds ) )
			return new ManualPage[] { this };
		
		int lineHeight = ( int )( drawContent( bounds ).boundingBox.y / drawContent( bounds ).numberLines );
		int permittedLines = bounds.getHeight() / lineHeight;
		
		List<String[]> lines = splitLines( Lists.newArrayList( RenderUtils.splitLines( content, bounds.getWidth() - 15, 0.75F ) ), permittedLines );
		
		ManualPageText[] pages = new ManualPageText[ lines.size() ];
		for( int i = 0; i < lines.size(); i++ )
			pages[ i ] = new ManualPageText( title, lines.get( i ) );
		
		return pages;
	}
	
	@Override
	public boolean hasOverflow( Rectangle bounds ) {
		return drawContent( bounds ).boundingBox.y >= bounds.getHeight();
	}
	
	private RenderManualOutput drawContent( Rectangle bounds ) {
		if( render == null )
			render = RenderUtils.drawStringWrapped( content, bounds.getWidth() - 15, Alignment.LEFT, bounds.getX() + 15, bounds.getY() + 35, 0xFFFFFF, 0.75F );
		
		return render;
	}
	
	private List<String[]> splitLines( List<String> lines, int count ) {
		List<String[]> chunks = Lists.newLinkedList();
		
		for( int i = 0; i < lines.size(); i++ )
			chunks.add( ( String[] )lines.subList( i, Math.min( lines.size(), i + count ) ).toArray() );
		
		return chunks;
	}
	
}
