package com.immersiveworks.tinyreactors.client.gui.widgets;

import com.immersiveworks.tinyreactors.api.manual.IManualSection;
import com.immersiveworks.tinyreactors.api.util.RenderUtils;
import com.immersiveworks.tinyreactors.client.gui.GuiTinyManual;

import net.minecraftforge.common.util.TextTable.Alignment;

public class WidgetManualBookmark extends WidgetButton {

	private IManualSection section;
	
	public WidgetManualBookmark( IManualSection section, int x, int y, int width, int height ) {
		super( x, y, width, height, null );
		this.section = section;
	}
	
	@Override
	public void drawBackground( int mouseX, int mouseY, float partialTicks ) {
		super.drawBackground( mouseX, mouseY, partialTicks );
		RenderUtils.drawString( section.getHeader(), Alignment.LEFT, x + 12, y + 3, 0xFFFFFF, 0.5F );
	}
	
	@Override
	public void drawForeground( int mouseX, int mouseY, float partialTicks ) {
		RenderUtils.drawItemStack( section.getIcon(), x + 2, y + 1, 0.49F );
	}
	
	@Override
	public void mouseClicked( int mouseButton ) {
		GuiTinyManual.instance.setSection( section );
	}
	
}