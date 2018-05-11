package com.immersiveworks.tinyreactors.client.gui.manual.pages;

import java.util.List;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.client.gui.manual.GuiTinyManual;
import com.immersiveworks.tinyreactors.client.util.RenderUtils;
import com.immersiveworks.tinyreactors.common.helpers.ArrayHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.util.TextTable.Alignment;

public class ManualPageText extends ManualPage {

	protected String content;
	
	public ManualPageText( String title, String content ) {
		this( title, content, -1, -1 );
	}
	
	public ManualPageText( String title, String content, int current, int total ) {
		super( title, "textures/gui/manual_page.png", current, total );
		this.content = content;
	}
	
	@Override
	public void drawScreen( GuiTinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
		drawContent( bounds );
	}
	
	@Override
	public ManualPage[] getOverflowPages( Rectangle bounds ) {
		int lineHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3;
		int maxLines = bounds.getHeight() / lineHeight;
		
		List<String[]> lines = ArrayHelper.split( RenderUtils.splitLines( content, bounds.getWidth() - 15, 0.75F ), maxLines );
		
		ManualPage[] pages = new ManualPage[ lines.size() ];
		for( int i = 0; i < pages.length; i++ )
			pages[ i ] = new ManualPageText( title, ArrayHelper.combine( lines.get( i ), "\n" ), i + 1, pages.length );
		
		return pages;
	}
	
	@Override
	public boolean hasOverflow( Rectangle bounds ) {
		return drawContent( bounds ).getHeight() > bounds.getHeight() - ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) * 2;
	}
	
	private Rectangle drawContent( Rectangle bounds ) {
		return RenderUtils.drawStringWrapped( content, bounds.getWidth() - 15, Alignment.LEFT, bounds.getX() + 15, bounds.getY() + 32, 0xFFFFFF, 0.75F );
	}
	
}
