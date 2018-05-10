package com.immersiveworks.tinyreactors.client.gui.manual.pages;

import org.lwjgl.util.Rectangle;

import com.immersiveworks.tinyreactors.client.gui.manual.GuiTinyManual;
import com.immersiveworks.tinyreactors.client.util.RenderUtils;

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
	public void drawScreen( GuiTinyManual manual, ScaledResolution sr, Rectangle bounds, int mouseX, int mouseY, float partialTicks ) {
		RenderUtils.drawString( TextFormatting.GOLD + title, Alignment.CENTER, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 5 - 15, 0xFFFFFF, 1.5F );
		RenderUtils.drawString( TextFormatting.ITALIC + subtitle, Alignment.CENTER, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 5 + 15, 0xFFFFFF, 1F );
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
