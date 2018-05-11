package com.immersiveworks.tinyreactors.api.manual.pages;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.api.manual.ITinyManual;
import com.immersiveworks.tinyreactors.api.util.RenderUtils;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.TextTable.Alignment;

public class ManualPageCover extends ManualPage {

	private String title;
	private String subtitle;
	
	public ManualPageCover( String title, String subtitle ) {
		super( "", "textures/gui/cover_front.png" );
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public void drawScreen( ITinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
		RenderUtils.drawString( title, TextFormatting.GOLD, Alignment.CENTER, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 5 - 15, 0xFFFFFF, 1.5F );
		RenderUtils.drawString( subtitle, TextFormatting.ITALIC, Alignment.CENTER, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 5 + 15, 0xFFFFFF, 1F );
	}
	
	@Override
	public boolean shouldDrawHeader() {
		return false;
	}
	
	@Override
	public boolean shouldDrawTitle() {
		return false;
	}
	
}
