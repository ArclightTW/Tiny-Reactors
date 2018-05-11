package com.immersiveworks.tinyreactors.client.gui.manual.pages;

import java.util.List;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.client.util.RenderUtils;
import com.immersiveworks.tinyreactors.common.helpers.ArrayHelper;

import net.minecraft.client.Minecraft;

public class ManualPageReactorBlock extends ManualPageText {

	public enum Requirement { REQUIRED, OPTIONAL }
	
	private Requirement requirement;
	
	public ManualPageReactorBlock( Requirement requirement, String content ) {
		this( requirement, content, -1, -1 );
	}
	
	public ManualPageReactorBlock( Requirement requirement, String content, int current, int total ) {
		super( String.format( "tiny_manual:block_type_%s", requirement.name().toLowerCase() ), content, current, total );
		this.requirement = requirement;
	}
	
	@Override
	public ManualPage[] getOverflowPages( Rectangle bounds ) {
		int lineHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3;
		int maxLines = bounds.getHeight() / lineHeight;
		
		List<String[]> lines = ArrayHelper.split( RenderUtils.splitLines( content, bounds.getWidth() - 15, 0.75F ), maxLines );
		
		ManualPage[] pages = new ManualPage[ lines.size() ];
		for( int i = 0; i < pages.length; i++ )
			pages[ i ] = new ManualPageReactorBlock( requirement, ArrayHelper.combine( lines.get( i ), "\n" ), i + 1, pages.length );
		
		return pages;
	}

}
