package com.immersiveworks.tinyreactors.api.manual;

import org.lwjgl.util.Rectangle;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public interface IManualPage {

	public String getTitle();
	public ResourceLocation getBackground();
	
	public void drawScreen( ITinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks );
	
	public boolean hasOverflow( Rectangle bounds );
	public String[] getSplitLines( Rectangle bounds );
	
	public IManualPage getNewInstance( String[] lines );
	
	public default boolean shouldDrawHeader() {
		return true;
	}
	
	public default boolean shouldDrawTitle() {
		return true;
	}
	
	public default boolean shouldDrawWidgets() {
		return true;
	}
	
}
