package com.immersiveworks.tinyreactors.client.gui.manual;

import java.util.List;

import org.lwjgl.util.Rectangle;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPage;
import com.immersiveworks.tinyreactors.client.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.TextTable.Alignment;

public class GuiManualSection {

	private Rectangle prevBounds;
	
	private String header;
	private int pageIndex;
	
	private List<ManualPage> sourcePages;
	private List<ManualPage> pages;
	private ManualPage page;
	
	public GuiManualSection( String header, ManualPage... pages ) {
		this.header = header;
		this.pages = Lists.newLinkedList();

		sourcePages = Lists.newArrayList( pages );
		if( sourcePages.size() > 0 )
			page = sourcePages.get( 0 );
	}
	
	public void drawScreen( GuiTinyManual manual, ScaledResolution sr, int mouseX, int mouseY, float partialTicks ) {
		if( page == null )
			return;
		
		Rectangle pageBounds = new Rectangle( sr.getScaledWidth() / 2 - 146 / 2, sr.getScaledHeight() / 2 - 180 / 2, 146, 180 );
		
		// TODO: This should only be called once, ever
//		if( prevBounds != pageBounds ) {
		if( prevBounds == null ) {
			prevBounds = pageBounds;
			pages.clear();
			
			for( int i = 0; i < sourcePages.size(); i++ )
				setPages( sourcePages.get( i ).getOverflowPages( pageBounds ) );
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.color( 1, 1, 1, 1 );
		Minecraft.getMinecraft().getTextureManager().bindTexture( page.getBackground() );
		RenderUtils.drawTexturedModalRect( pageBounds, 0, 0 );
		GlStateManager.popMatrix();
		
		if( page.shouldDrawHeader() )
			RenderUtils.drawString( TextFormatting.BLACK + header, Alignment.CENTER, sr.getScaledWidth() / 2, pageBounds.getY() + 12, 0xFFFFFF, 1F );
		
		if( page.shouldDrawTitle() )
			RenderUtils.drawString( TextFormatting.DARK_AQUA + page.getTitle(), Alignment.CENTER, sr.getScaledWidth() / 2, pageBounds.getY() + 24, 0xFFFFFF, 0.75F );
		
		GlStateManager.pushMatrix();
		page.drawScreen( manual, sr, pageBounds, mouseX, mouseY, partialTicks );
		GlStateManager.popMatrix();
		
		if( page.shouldDrawWidgets() )
			manual.drawWidgets( mouseX, mouseY, partialTicks );
	}
	
	public boolean nextPage() {
		pageIndex++;
		boolean value = pageIndex >= pages.size();
		
		if( pageIndex >= pages.size() )
			pageIndex = pages.size() - 1;
		
		page = pages.get( pageIndex );
		return value;
	}
	
	public boolean previousPage() {
		pageIndex--;
		boolean value = pageIndex < 0;
		
		if( pageIndex <= 0 )
			pageIndex = 0;
		
		page = pages.get( pageIndex );
		return value;
	}
	
	private void setPages( ManualPage... pages ) {
		for( int i = 0; i < pages.length; i++ )
			this.pages.add( pages[ i ] );
	}
	
}
