package com.immersiveworks.tinyreactors.api.manual.pages;

import com.immersiveworks.tinyreactors.api.manual.IManualEntry;

public class ManualPageTextDetails extends ManualPageText {

	public ManualPageTextDetails( IManualEntry entry ) {
		super( "tiny_manual.header.details", String.format( "tiny_manual.page.%s.details", entry.getManualKey() ) );
	}
	
}
