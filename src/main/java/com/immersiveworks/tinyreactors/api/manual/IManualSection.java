package com.immersiveworks.tinyreactors.api.manual;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

public interface IManualSection {

	void drawScreen( ITinyManual manual, ScaledResolution sr, int mouseX, int mouseY, float partialTicks );
	void onGuiResized( ScaledResolution sr );
	
	void addPage( IManualPage page );
	void setPageIndex( int index );
	
	boolean previousPage();
	boolean nextPage();
	
	String getHeader();
	ItemStack getIcon();
	
}
