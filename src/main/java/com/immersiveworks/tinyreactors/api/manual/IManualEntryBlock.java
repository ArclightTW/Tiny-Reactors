package com.immersiveworks.tinyreactors.api.manual;

import com.immersiveworks.tinyreactors.client.gui.manual.GuiTinyManual;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPage;

import net.minecraft.item.ItemStack;

public interface IManualEntryBlock {
	
	String getManualKey();
	String getManualHeader();
	ItemStack getManualIcon();
	
	ManualPage[] getManualPages();
	
	default float getManualHeaderScale() {
		return 1F;
	}
	
	default void registerSection() {
		GuiTinyManual.instance.registerSection(
				getManualKey(),
				getManualHeader(),
				getManualHeaderScale(),
				getManualIcon(),
				getManualPages()
				);
	}
	
}
