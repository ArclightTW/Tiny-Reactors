package com.immersiveworks.tinyreactors.api.manual.pages;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.api.manual.IManualPage;
import com.immersiveworks.tinyreactors.api.manual.ITinyManual;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ManualPage implements IManualPage {

	protected String key;
	protected String title;
	protected ResourceLocation pageBackground;
	
	public ManualPage( String title ) {
		this( title, "textures/gui/manual_page.png" );
	}
	
	public ManualPage( String title, String pageBackground ) {
		this.title = title;
		this.pageBackground = new ResourceLocation( "tinyreactors", pageBackground );
	}
	
	@Override
	public void drawScreen( ITinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
	}
	
	@Override
	public boolean hasOverflow( Rectangle bounds ) {
		return false;
	}
	
	@Override
	public ManualPage getNewInstance( String[] lines ) {
		return new ManualPage( title, pageBackground.toString() );
	}
	
	@Override
	public String[] getSplitLines( Rectangle bounds ) {
		return new String[ 0 ];
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public ResourceLocation getBackground() {
		return pageBackground;
	}
	
	public ManualPage setKey( String key ) {
		this.key = key;
		return this;
	}
	
	public String getKey() {
		return key;
	}
	
}
