package com.immersiveworks.tinyreactors.client.gui.button;

import com.immersiveworks.tinyreactors.client.gui.GuiTinyWrenchOverlay.GridAlignment;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonAlignment extends GuiButton {

	public GridAlignment anchor;
	
	public GuiButtonAlignment( int id, int x, int y, GridAlignment anchor ) {
		super( id, x, y, 20, 20, "" );
		this.anchor = anchor;
	}
	
}
