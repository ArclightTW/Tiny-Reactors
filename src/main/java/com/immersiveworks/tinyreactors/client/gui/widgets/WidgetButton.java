package com.immersiveworks.tinyreactors.client.gui.widgets;

import java.util.function.Consumer;

import com.immersiveworks.tinyreactors.client.util.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class WidgetButton extends Widget {

	protected ResourceLocation textureDefault, textureHovered;
	protected int textureXDefault, textureXHovered;
	protected int textureYDefault, textureYHovered;
	
	protected Consumer<Integer> onClicked;
	
	public WidgetButton( int x, int y, int width, int height, Consumer<Integer> onClicked ) {
		super( x, y, width, height );
		
		this.onClicked = onClicked;
	}
	
	public WidgetButton setDefaultTexture( String texture, int textureX, int textureY ) {
		this.textureDefault = new ResourceLocation( texture );
		this.textureXDefault = textureX;
		this.textureYDefault = textureY;
		
		return this;
	}
	
	public WidgetButton setHoveredTexture( String texture, int textureX, int textureY ) {
		this.textureHovered = new ResourceLocation( texture );
		this.textureXHovered = textureX;
		this.textureYHovered = textureY;
		
		return this;
	}
	
	@Override
	public void draw( int mouseX, int mouseY, float partialTicks ) {
		boolean hovered = isMouseInBounds( mouseX, mouseY );
		
		Minecraft.getMinecraft().getTextureManager().bindTexture( hovered ? textureHovered : textureDefault );
		RenderUtils.drawTexturedModalRect( x, y, hovered ? textureXHovered : textureXDefault, hovered ? textureYHovered : textureYDefault, width, height );
	}
	
	@Override
	public void mouseClicked( int mouseButton ) {
		if( onClicked != null )
			onClicked.accept( mouseButton );
	}
	
}
