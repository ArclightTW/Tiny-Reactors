package com.immersiveworks.tinyreactors.client.gui.widgets;

public abstract class Widget {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	public Widget( int x, int y, int width, int height ) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean isMouseInBounds( int mouseX, int mouseY ) {
		return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
	}
	
	public void mouseClicked( int mouseButton ) {
	}
	
	public abstract void draw( int mouseX, int mouseY, float partialTicks );
	
}
