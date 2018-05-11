package com.immersiveworks.tinyreactors.api.manual.pages;

import com.immersiveworks.tinyreactors.api.helpers.ArrayHelper;

public class ManualPageRequirement extends ManualPageText {

	public enum Requirement { REQUIRED, OPTIONAL }
	
	private Requirement requirement;
	
	public ManualPageRequirement( Requirement requirement) {
		this( requirement, String.format( "tiny_manual.page.%s", requirement.name().toLowerCase() ) );
	}
	
	private ManualPageRequirement( Requirement requirement, String content ) {
		super( String.format( "tiny_manual.block_type_%s", requirement.name().toLowerCase() ), content );
		this.requirement = requirement;
	}
	
	@Override
	public ManualPage getNewInstance( String[] lines ) {
		return new ManualPageRequirement( requirement, ArrayHelper.combine( lines, "\n" ) );
	}

}
