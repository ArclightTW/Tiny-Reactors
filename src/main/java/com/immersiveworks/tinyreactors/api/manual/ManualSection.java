package com.immersiveworks.tinyreactors.api.manual;

import java.util.List;

import org.lwjgl.util.Rectangle;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.helpers.ArrayHelper;
import com.immersiveworks.tinyreactors.api.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.TextTable.Alignment;

public class ManualSection implements IManualSection {

	private String header;
	private float headerScale;
	
	private ItemStack icon;
	
	private int pageIndex;
	
	private List<IManualPage> pages;
	private IManualPage page;
	
	public ManualSection( String header, float headerScale, ItemStack icon, IManualPage... pages ) {
		this.header = header;
		this.headerScale = headerScale;
		this.icon = icon;
		this.pages = Lists.newArrayList( pages );
		
		if( this.pages.size() > pageIndex )
			this.page = this.pages.get( pageIndex );
	}
	
	@Override
	public void onGuiResized( ScaledResolution sr ) {
		List<IManualPage> tempPages = Lists.newLinkedList();
		for( int i = 0; i < pages.size(); i++ ) {
			if( !pages.get( i ).hasOverflow( getPageBounds() ) ) {
				tempPages.add( pages.get( i ) );
				continue;
			}

			IManualPage page = pages.get( i );
			
			List<String[]> lines = ArrayHelper.split( page.getSplitLines( getPageBounds() ), getPageBounds().getHeight() / ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) );
			for( int j = 0; j < lines.size(); j++ )
				tempPages.add( page.getNewInstance( lines.get( j ) ) );
		}
		pages.clear();
		pages.addAll( tempPages );
		
		if( pages.size() <= pageIndex )
			pageIndex = pages.size() - 1;
		page = pages.get( pageIndex );
	}
	
	@Override
	public void drawScreen( ITinyManual manual, ScaledResolution sr, int mouseX, int mouseY, float partialTicks ) {
		if( page == null ) {
			previousPage();
			return;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.color( 1, 1, 1, 1 );
		Minecraft.getMinecraft().getTextureManager().bindTexture( page.getBackground() );
		RenderUtils.drawTexturedModalRect( getPageBounds(), 0, 0 );
		GlStateManager.popMatrix();
		
		if( page.shouldDrawHeader() )
			RenderUtils.drawString( header, Alignment.CENTER, sr.getScaledWidth() / 2, getPageBounds().getY() + 10, 0x000000, headerScale );
		
		if( page.shouldDrawTitle() )
			RenderUtils.drawString( page.getTitle(), Alignment.CENTER, sr.getScaledWidth() / 2, getPageBounds().getY() + 22, 0x00AAAA, 0.75F );
		
		GlStateManager.pushMatrix();
		page.drawScreen( manual, sr, getPageBounds(), mouseX, mouseY, partialTicks );
		GlStateManager.popMatrix();
		
		if( page.shouldDrawWidgets() )
			manual.drawWidgets( mouseX, mouseY, partialTicks );
	}
	
	@Override
	public void addPage( IManualPage page ) {
		
	}
	
	@Override
	public void setPageIndex( int index ) {
		if( index <= 0 )
			index = 0;
		if( index >= pages.size() )
			index = pages.size() - 1;
		
		pageIndex = index;
		page = pages.get( pageIndex );
	}
	
	@Override
	public boolean nextPage() {
		boolean shouldBail = pageIndex + 1 >= pages.size();
		setPageIndex( pageIndex + 1 );
		return shouldBail;
	}
	
	@Override
	public boolean previousPage() {
		boolean shouldBail = pageIndex - 1 < 0;
		setPageIndex( pageIndex - 1 );
		return shouldBail;
	}
	
	@Override
	public String getHeader() {
		return header;
	}
	
	@Override
	public ItemStack getIcon() {
		return icon;
	}
	
	private Rectangle getPageBounds() {
		ScaledResolution sr = new ScaledResolution( Minecraft.getMinecraft() );
		return new Rectangle( sr.getScaledWidth() / 2 - 146 / 2, sr.getScaledHeight() / 2 - 180 / 2, 146, 180 );
	}
	
}
