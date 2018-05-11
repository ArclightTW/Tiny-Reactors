package com.immersiveworks.tinyreactors.client.gui.manual;

import java.util.List;

import org.lwjgl.util.Rectangle;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPage;
import com.immersiveworks.tinyreactors.client.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.TextTable.Alignment;

// TODO: We might need a new way of doing this (back to custom resource locations?)
public class GuiManualSection {

	private String header;
	private float headerScale;
	
	private ItemStack icon;
	
	private int pageIndex;
	
	private List<ManualPage> pages;
	private ManualPage page;
	
	public GuiManualSection( String header, float headerScale, ItemStack icon, ManualPage... pages ) {
		this.header = header;
		this.headerScale = headerScale;
		this.icon = icon;
		this.pages = Lists.newArrayList( pages );
		
		if( this.pages.size() > pageIndex )
			this.page = this.pages.get( pageIndex );
	}
	
	public void onGuiResized( ScaledResolution sr ) {
		List<ManualPage> tempPages = Lists.newLinkedList();
		for( int i = 0; i < pages.size(); i++ ) {
			if( !pages.get( i ).hasOverflow( getPageBounds() ) ) {
				tempPages.add( pages.get( i ) );
				continue;
			}
			
			ManualPage[] overflow = pages.get( i ).getOverflowPages( getPageBounds() );
			for( int j = 0; j < overflow.length; j++)
				tempPages.add( overflow[ j ] );
		}
		pages.clear();
		pages.addAll( tempPages );
		
		if( pages.size() <= pageIndex )
			pageIndex = pages.size() - 1;
		page = pages.get( pageIndex );
	}
	
	public void drawScreen( GuiTinyManual manual, ScaledResolution sr, int mouseX, int mouseY, float partialTicks ) {
		if( page == null )
			return;
		
		GlStateManager.pushMatrix();
		GlStateManager.color( 1, 1, 1, 1 );
		Minecraft.getMinecraft().getTextureManager().bindTexture( page.getBackground() );
		RenderUtils.drawTexturedModalRect( getPageBounds(), 0, 0 );
		GlStateManager.popMatrix();
		
		if( page.shouldDrawHeader() )
			RenderUtils.drawString( header, Alignment.CENTER, sr.getScaledWidth() / 2, getPageBounds().getY() + 10, 0xFFFFFF, headerScale );
		
		if( page.shouldDrawTitle() ) {
			RenderUtils.drawString( page.getTitle(), Alignment.CENTER, sr.getScaledWidth() / 2, getPageBounds().getY() + 22, 0xFFFFFF, 0.75F );
			
			if( page.totalCount > 0 )
				RenderUtils.drawString( TextFormatting.DARK_AQUA + String.format( "%d/%d", page.currentIndex, page.totalCount ), Alignment.RIGHT, getPageBounds().getX() + getPageBounds().getWidth() - 15, getPageBounds().getY() + 22, 0xFFFFFF, 0.75F );
		}
		
		GlStateManager.pushMatrix();
		page.drawScreen( manual, sr, getPageBounds(), mouseX, mouseY, partialTicks );
		GlStateManager.popMatrix();
		
		if( page.shouldDrawWidgets() )
			manual.drawWidgets( mouseX, mouseY, partialTicks );
	}
	
	public boolean nextPage() {
		boolean shouldBail = pageIndex + 1 >= pages.size();
		setPageIndex( pageIndex + 1 );
		return shouldBail;
	}
	
	public boolean previousPage() {
		boolean shouldBail = pageIndex - 1 < 0;
		setPageIndex( pageIndex - 1 );
		return shouldBail;
	}
	
	public void setPageIndex( int index ) {
		if( index <= 0 )
			index = 0;
		if( index >= pages.size() )
			index = pages.size() - 1;
		
		pageIndex = index;
		page = pages.get( pageIndex );
	}
	
	public String getHeader() {
		return header;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	private Rectangle getPageBounds() {
		ScaledResolution sr = new ScaledResolution( Minecraft.getMinecraft() );
		return new Rectangle( sr.getScaledWidth() / 2 - 146 / 2, sr.getScaledHeight() / 2 - 180 / 2, 146, 180 );
	}
	
}
