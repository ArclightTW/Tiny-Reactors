package com.immersiveworks.tinyreactors.api.manual;

import com.immersiveworks.tinyreactors.client.gui.manual.GuiManualSection;
import com.immersiveworks.tinyreactors.client.gui.manual.GuiTinyManual;

public interface IManualEntryBlock {
	
	String getKey();
	GuiManualSection getSection();
	
	default void registerSection() {
		GuiTinyManual.instance.registerSection( getKey(), getSection() );
	}
	
}
