package com.immersiveworks.tinyreactors.client.gui.manual.pages;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.client.gui.manual.GuiTinyManual;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ManualPage {

	protected String title;
	protected ResourceLocation pageBackground;
	
	public int currentIndex;
	public int totalCount;
	
	public ManualPage( String title ) {
		this( title, "textures/gui/manual_page.png" );
	}
	
	public ManualPage( String title, String pageBackground ) {
		this( title, pageBackground, -1, -1 );
	}
	
	public ManualPage( String title, String pageBackground, int currentIndex, int totalCount ) {
		this.title = title;
		this.pageBackground = new ResourceLocation( TinyReactors.ID, pageBackground );
		
		this.currentIndex = currentIndex;
		this.totalCount = totalCount;
	}
	
	public void drawScreen( GuiTinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
	}
	
	public boolean hasOverflow( Rectangle bounds ) {
		return false;
	}
	
	public ManualPage[] getOverflowPages( Rectangle bounds ) {
		return new ManualPage[] { this };
	}
	
	public boolean shouldDrawHeader() {
		return true;
	}
	
	public boolean shouldDrawTitle() {
		return true;
	}
	
	public boolean shouldDrawWidgets() {
		return true;
	}
	
	public ResourceLocation getBackground() {
		return pageBackground;
	}
	
	public String getTitle() {
		return title;
	}
	
}
