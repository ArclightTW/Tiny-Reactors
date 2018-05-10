package com.immersiveworks.tinyreactors.client.gui.manual;

import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.manual.IManualEntryBlock;
import com.immersiveworks.tinyreactors.client.gui.GuiScreenWidget;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPageCover;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPageText;
import com.immersiveworks.tinyreactors.client.gui.widgets.WidgetButton;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class GuiTinyManual extends GuiScreenWidget {

	public static GuiTinyManual instance = new GuiTinyManual();
	
	private ScaledResolution sr;
	
	private List<String> sectionIDs;
	private List<GuiManualSection> sectionKeys;
	private GuiManualSection section;
	
	// TODO: Pages need to dynamically decide if they are too long and, if so, add an additional page with the overflow content. Yeah. YEAH.
	private GuiTinyManual() {
		sectionIDs = Lists.newLinkedList();
		sectionKeys = Lists.newLinkedList();
		registerSection( "overview", section = new GuiManualSection( "Overview",
				new ManualPageCover( TinyReactors.NAME, String.format( "%s\nby ArclightTW", TinyReactors.VERSION ) ),
				new ManualPageText( "Introduction", "Welcome to the Tiny Manual, your one-stop guide to everything you need to know about Tiny Reactors!\nAt this point, we are purposely overflowing the page to try and work out just HOW we are going to make this work as I want it to work in my head." )
				) );
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void updateScreen() {
		sr = new ScaledResolution( Minecraft.getMinecraft() );
	}
	
	@Override
	public void initGui() {
		super.initGui();
		sr = new ScaledResolution( Minecraft.getMinecraft() );
		
		widgets.addWidget(
				new WidgetButton( sr.getScaledWidth() / 2 - 146 / 2 + 15, sr.getScaledHeight() / 2 - 180 / 2 + 10, 10, 8, ( button ) -> changePage( PageDirection.PREVIOUS ) ).
				setDefaultTexture( "tinyreactors:textures/gui/widgets.png", 0, 8 ).
				setHoveredTexture( "tinyreactors:textures/gui/widgets.png", 10, 8 )
				);
		widgets.addWidget(
				new WidgetButton( sr.getScaledWidth() / 2 + 146 / 2 - 25, sr.getScaledHeight() / 2 - 180 / 2 + 10, 10, 8, ( button ) -> changePage( PageDirection.NEXT ) ).
				setDefaultTexture( "tinyreactors:textures/gui/widgets.png", 0, 0 ).
				setHoveredTexture( "tinyreactors:textures/gui/widgets.png", 10, 0 )
				);
	}
	
	@Override
	public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
		drawDefaultBackground();
		if( section != null )
			section.drawScreen( this, sr, mouseX, mouseY, partialTicks );
	}
	
	public void changePage( PageDirection direction ) {
		if( section == null )
			return;
		
		int index = -1;
		
		switch( direction ) {
		case PREVIOUS:
			if( !section.previousPage() )
				break;
			
			index = sectionKeys.indexOf( section );
			if( index == -1 || index == 0 )
				break;
			
			section = getSection( sectionIDs.get( index - 1 ) );
			break;
		case NEXT:
			if( !section.nextPage() )
				break;
			
			index = sectionKeys.indexOf( section );
			if( index == -1 || index >= sectionIDs.size() - 1 )
				break;
			
			section = getSection( sectionIDs.get( index + 1 ) );
			break;
		}
	}
	
	public void registerSection( String key, GuiManualSection section ) {
		sectionKeys.add( section );
		sectionIDs.add( key );
	}
	
	public void openCurrent() {
		Minecraft.getMinecraft().displayGuiScreen( instance );
	}
	
	public void openPage( IManualEntryBlock entry ) {
		instance.section = getSection( entry.getKey() );
		Minecraft.getMinecraft().displayGuiScreen( instance );
	}
	
	private GuiManualSection getSection( String key ) {
		int index = sectionIDs.indexOf( key );
		if( index == -1 || index >= sectionKeys.size() )
			return null;
		
		return sectionKeys.get( index );
	}
	
	public enum PageDirection {
		NEXT,
		PREVIOUS
	}
	
}
