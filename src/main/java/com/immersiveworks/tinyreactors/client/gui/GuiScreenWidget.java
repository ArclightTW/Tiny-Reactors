package com.immersiveworks.tinyreactors.client.gui;

import java.io.IOException;

import com.immersiveworks.tinyreactors.client.gui.widgets.WidgetContainer;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiScreenWidget extends GuiScreen {
	
	protected WidgetContainer widgets;
	
	public GuiScreenWidget() {
		widgets = new WidgetContainer();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		widgets.clear();
	}
	
	@Override
	public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
		drawWidgets( mouseX, mouseY, partialTicks );
	}
	
	@Override
	public void mouseClicked( int mouseX, int mouseY, int mouseButton ) throws IOException {
		widgets.mouseClicked( mouseX, mouseY, mouseButton );
	}
	
	public void drawWidgets( int mouseX, int mouseY, float partialTicks ) {
		GlStateManager.pushMatrix();
		GlStateManager.color( 1, 1, 1, 1 );
		widgets.drawWidgets( mouseX, mouseY, partialTicks );
		GlStateManager.popMatrix();
	}
	
}
