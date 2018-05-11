package com.immersiveworks.tinyreactors.api.manual;

import net.minecraft.item.ItemStack;

public interface ITinyManual {

	IManualSection registerSection( String key, String header, float headerScale, ItemStack icon, IManualPage... pages );
	IManualSection registerPage( String sectionID, IManualPage page );
	
	void drawWidgets( int mouseX, int mouseY, float partialTicks );
	
}
