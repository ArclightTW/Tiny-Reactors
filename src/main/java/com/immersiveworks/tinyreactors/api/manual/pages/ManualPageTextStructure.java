package com.immersiveworks.tinyreactors.api.manual.pages;

import com.immersiveworks.tinyreactors.api.manual.IManualEntry;
import com.immersiveworks.tinyreactors.api.util.Reactor;

import net.minecraft.block.Block;
import net.minecraft.util.text.TextComponentTranslation;

public class ManualPageTextStructure extends ManualPageText {

	public ManualPageTextStructure( IManualEntry entry ) {
		super( "tiny_manual.header.structure", String.format( "tiny_manual.page.%s.structure", entry.getManualKey() ) );
		
		if( !( entry instanceof Block ) )
			return;
		
		String[] locations = Reactor.retreiveValidPlaces( ( Block )entry );
		
		content += new TextComponentTranslation("\ntiny_manual.page.structure :\n", entry.getManualHeader() ).getFormattedText();
		
		for( int i = 0; i < locations.length; i++ )
			content += locations[ i ] + ", ";
		
		content = content.substring( 0, content.length() - 2 );
	}
	
}
