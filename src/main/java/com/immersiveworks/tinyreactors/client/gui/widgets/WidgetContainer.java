package com.immersiveworks.tinyreactors.client.gui.widgets;

import java.util.List;

import com.google.common.collect.Lists;

public class WidgetContainer {

	private List<Widget> widgets;
	
	public WidgetContainer() {
		widgets = Lists.newLinkedList();
	}
	
	public void drawWidgets( int mouseX, int mouseY, float partialTicks ) {
		for( int i = 0; i < widgets.size(); i++ )
			widgets.get( i ).drawBackground( mouseX, mouseY, partialTicks );
		
		for( int i = 0; i < widgets.size(); i++ )
			widgets.get( i ).drawForeground( mouseX, mouseY, partialTicks );
	}
	
	public void mouseClicked( int mouseX, int mouseY, int mouseButton ) {
		for( int i = 0; i < widgets.size(); i++ ) {
			if( !widgets.get( i ).isMouseInBounds( mouseX, mouseY ) )
				continue;
			
			widgets.get( i ).mouseClicked( mouseButton );
			break;
		}
	}
	
	public void addWidget( Widget widget ) {
		widgets.add( widget );
	}
	
	public void clear() {
		widgets.clear();
	}
	
}
