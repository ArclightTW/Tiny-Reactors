package com.immersiveworks.tinyreactors.api.manual;

import net.minecraft.item.ItemStack;

public interface IManualEntry {
	
	String getManualKey();
	String getManualHeader();
	ItemStack getManualIcon();
	
	IManualPage[] getManualPages();
	
	default float getManualHeaderScale() {
		return 1F;
	}
	
	default void registerSection( ITinyManual manual ) {
		manual.registerSection(
				getManualKey(),
				getManualHeader(),
				getManualHeaderScale(),
				getManualIcon(),
				getManualPages()
				);
	}
	
}
