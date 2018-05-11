package com.immersiveworks.tinyreactors.client.gui.manual;

import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.manual.IManualEntryBlock;
import com.immersiveworks.tinyreactors.client.gui.GuiScreenWidget;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPage;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPageCover;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPageText;
import com.immersiveworks.tinyreactors.client.gui.widgets.WidgetButton;
import com.immersiveworks.tinyreactors.client.gui.widgets.WidgetManualBookmark;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

public class GuiTinyManual extends GuiScreenWidget {

	public static GuiTinyManual instance = new GuiTinyManual();
	
	private ScaledResolution sr;
	
	private List<String> sectionIDs;
	private List<GuiManualSection> sectionKeys;
	
	private GuiManualSection section;
	
	private GuiTinyManual() {
		sectionIDs = Lists.newLinkedList();
		sectionKeys = Lists.newLinkedList();
		section = registerSection( "cover", "tiny_manual:section.cover", 1F, ItemStack.EMPTY,
				new ManualPageCover( TinyReactors.NAME, String.format( "%s\nby ArclightTW", TinyReactors.VERSION ) )
				);
		
		registerSection( "overview", "tiny_manual:section.overview", 1F, new ItemStack( Items.TINY_WRENCH ),
				new ManualPageText( "tiny_manual:header.introduction", "tiny_manual:page.introduction" )
				);
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
		
		int counter = 0;
		for( int i = 0; i < sectionIDs.size(); i++ ) {
			String key = sectionIDs.get( i );
			if( key.contains( "cover" ) )
				continue;
			
			widgets.addWidget(
					new WidgetManualBookmark( sectionKeys.get( i ), sr.getScaledWidth() / 2 + 146 / 2, sr.getScaledHeight() / 2 - 180 / 2 + 7 + ( counter * 10 ), 62, 9 ).
					setDefaultTexture( "tinyreactors:textures/gui/widgets.png", 0, 54 ).
					setHoveredTexture( "tinyreactors:textures/gui/widgets.png", 0, 63 )
					);
			counter++;
		}
		
		for( int i = 0; i < sectionKeys.size(); i++ )
			sectionKeys.get( i ).onGuiResized( sr );
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
			section.setPageIndex( Integer.MAX_VALUE );
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
	
	public GuiManualSection registerSection( String key, String header, float headerScale, ItemStack icon, ManualPage... pages ) {
		GuiManualSection section = new GuiManualSection( header, headerScale, icon, pages );
		sectionKeys.add( section );
		sectionIDs.add( key );
		return section;
	}
	
	public void openCurrent() {
		Minecraft.getMinecraft().displayGuiScreen( instance );
	}
	
	public void openPage( IManualEntryBlock entry ) {
		instance.section = getSection( entry.getManualKey() );
		Minecraft.getMinecraft().displayGuiScreen( instance );
	}
	
	public void setSection( String key ) {
		setSection( getSection( key ) );
	}
	
	public void setSection( GuiManualSection section ) {
		if( section == null )
			return;
		
		this.section.setPageIndex( 0 );
		this.section = section;
	}
	
	private GuiManualSection getSection( String key ) {
		if( key == null )
			return null;
		
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
