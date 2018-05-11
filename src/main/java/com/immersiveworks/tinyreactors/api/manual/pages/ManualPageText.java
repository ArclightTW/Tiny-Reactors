package com.immersiveworks.tinyreactors.api.manual.pages;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.api.helpers.ArrayHelper;
import com.immersiveworks.tinyreactors.api.manual.ITinyManual;
import com.immersiveworks.tinyreactors.api.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.util.TextTable.Alignment;

public class ManualPageText extends ManualPage {

	protected String content;
	
	public ManualPageText( String title, String content ) {
		super( title, "textures/gui/manual_page.png" );
		this.content = content;
	}
	
	@Override
	public void drawScreen( ITinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
		drawContent( bounds );
	}
	
	@Override
	public boolean hasOverflow( Rectangle bounds ) {
		return drawContent( bounds ).getHeight() > bounds.getHeight() - ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) * 2;
	}
	
	@Override
	public String[] getSplitLines( Rectangle bounds ) {
		return RenderUtils.splitLines( content, bounds.getWidth(), 0.75F );
	}
	
	@Override
	public ManualPage getNewInstance( String[] lines ) {
		return new ManualPageText( title, ArrayHelper.combine( lines, "\n" ) );
	}
	
	private Rectangle drawContent( Rectangle bounds ) {
		return RenderUtils.drawStringWrapped( content, bounds.getWidth(), Alignment.LEFT, bounds.getX() + 15, bounds.getY() + 32, 0xFFFFFF, 0.75F );
	}
	
}
