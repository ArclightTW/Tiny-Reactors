package com.immersiveworks.tinyreactors.client.gui;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.manual.ManualSection;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageCover;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageImage;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageText;
import com.immersiveworks.tinyreactors.api.manual.IManualEntry;
import com.immersiveworks.tinyreactors.api.manual.IManualPage;
import com.immersiveworks.tinyreactors.api.manual.IManualSection;
import com.immersiveworks.tinyreactors.api.manual.ITinyManual;
import com.immersiveworks.tinyreactors.client.gui.widgets.WidgetButton;
import com.immersiveworks.tinyreactors.client.gui.widgets.WidgetContainer;
import com.immersiveworks.tinyreactors.client.gui.widgets.WidgetManualBookmark;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiTinyManual extends GuiScreenWidget implements ITinyManual {

	public static GuiTinyManual instance = new GuiTinyManual();
	
	private WidgetContainer bookmarks;
	private ScaledResolution sr;
	
	private List<String> sectionIDs;
	private List<IManualSection> sectionKeys;
	
	private IManualSection section;
	
	private GuiTinyManual() {
		bookmarks = new WidgetContainer();
		
		sectionIDs = Lists.newLinkedList();
		sectionKeys = Lists.newLinkedList();
		registerSection( "cover", "tiny_manual.section.cover", 1F, ItemStack.EMPTY,
				new ManualPageCover( TinyReactors.NAME, String.format( "%s\nby ArclightTW", TinyReactors.VERSION ) )
				);
		
		registerSection( "overview", "tiny_manual.section.overview", 1F, new ItemStack( Items.NETHER_STAR ),
				new ManualPageText( "tiny_manual.header.introduction", "tiny_manual.page.introduction" ),
				new ManualPageImage("tiny_manual.header.structure_overview_side", "tinyreactors:textures/manual/reactor_side_view.png", 0, 0, 256, 256, 0.5F ).
					setTooltip( "TC: tiny_manual.reactor.top_corner\nTE: tiny_manual.reactor.top_edge\nMC: tiny_manual.reactor.middle_corner\nME: tiny_manual.reactor.middle_edge\nBC: tiny_manual.reactor.bottom_corner\nBE: tiny_manual.reactor.bottom_edge" ).
					setKey( "structure_overview" )
				);
		
		section = sectionKeys.get( 0 );
	}
	
	public static GuiTinyManual createInstance() {
		return new GuiTinyManual();
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
		bookmarks.clear();
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
			
			bookmarks.addWidget(
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
		
		GlStateManager.pushMatrix();
		GlStateManager.color( 1, 1, 1, 1 );
		bookmarks.drawWidgets( mouseX, mouseY, partialTicks );
		GlStateManager.popMatrix();
		
		RenderHelper.enableGUIStandardItemLighting();
		
		if( section != null )
			section.drawScreen( this, sr, mouseX, mouseY, partialTicks );
	}
	
	@Override
	public void mouseClicked( int mouseX, int mouseY, int mouseButton ) throws IOException {
		super.mouseClicked( mouseX, mouseY, mouseButton );
		bookmarks.mouseClicked( mouseX, mouseY, mouseButton );
	}
	
	@Override
	public IManualSection registerSection( String key, String header, float headerScale, ItemStack icon, IManualPage... pages ) {
		ManualSection section = new ManualSection( header, headerScale, icon, pages );
		sectionKeys.add( section );
		sectionIDs.add( key );
		return section;
	}
	
	@Override
	public IManualSection registerPage( String sectionID, IManualPage page ) {
		if( !sectionIDs.contains( sectionID ) )
			return null;
		
		int index = sectionIDs.indexOf( sectionID );
		IManualSection section = sectionKeys.get( index );
		section.addPage( page );
		
		sectionKeys.set( index, section );
		return section;
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
	
	public void registerBackPage() {
		registerSection( "back_cover", "tiny_manual.section.back_cover", 1F, ItemStack.EMPTY,
				new ManualPageCover( "", "" )
				);
	}
	
	public void openCurrent() {
		Minecraft.getMinecraft().displayGuiScreen( instance );
	}
	
	public void openPage( IManualEntry entry ) {
		instance.section = getSection( entry.getManualKey() );
		Minecraft.getMinecraft().displayGuiScreen( instance );
	}
	
	public void setSection( String key ) {
		setSection( getSection( key ) );
	}
	
	public void setSection( IManualSection section ) {
		if( section == null )
			return;
		
		this.section.setPageIndex( 0 );
		this.section = section;
	}
	
	private IManualSection getSection( String key ) {
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
