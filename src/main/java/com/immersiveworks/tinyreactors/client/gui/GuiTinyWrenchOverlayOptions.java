package com.immersiveworks.tinyreactors.client.gui;

import java.io.IOException;

import com.immersiveworks.tinyreactors.client.gui.GuiTinyWrenchOverlay.GridAlignment;
import com.immersiveworks.tinyreactors.client.gui.button.GuiButtonAlignment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiTinyWrenchOverlayOptions extends GuiScreen {

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		ScaledResolution sr = new ScaledResolution( Minecraft.getMinecraft() );
		
		buttonList.add( new GuiButtonAlignment( 0, 0, 0, GridAlignment.LEFT_TOP ) );
		buttonList.add( new GuiButtonAlignment( 1, sr.getScaledWidth() / 2 - 10, 0, GridAlignment.CENTER_TOP ) );
		buttonList.add( new GuiButtonAlignment( 2, sr.getScaledWidth() - 20, 0, GridAlignment.RIGHT_TOP ) );
		buttonList.add( new GuiButtonAlignment( 3, 0, sr.getScaledHeight() / 2 - 10, GridAlignment.LEFT_MIDDLE ) );
		buttonList.add( new GuiButtonAlignment( 4, sr.getScaledWidth() / 2 - 10, sr.getScaledHeight() / 2 - 10, GridAlignment.CENTER_MIDDLE ) );
		buttonList.add( new GuiButtonAlignment( 5, sr.getScaledWidth() - 20, sr.getScaledHeight() / 2 - 10, GridAlignment.RIGHT_MIDDLE ) );
		buttonList.add( new GuiButtonAlignment( 6, 0, sr.getScaledHeight() - 20, GridAlignment.LEFT_BOTTOM ) );
		buttonList.add( new GuiButtonAlignment( 7, sr.getScaledWidth() / 2 - 10, sr.getScaledHeight() - 20, GridAlignment.CENTER_BOTTOM ) );
		buttonList.add( new GuiButtonAlignment( 8, sr.getScaledWidth() - 20, sr.getScaledHeight() - 20, GridAlignment.RIGHT_BOTTOM ) );
	}
	
	@Override
	protected void actionPerformed( GuiButton button ) throws IOException {
		GuiButtonAlignment alignment = ( GuiButtonAlignment )button;
		GuiTinyWrenchOverlay.setAnchor( alignment.anchor );
	}
	
	@Override
	public void drawScreen( int mouseX, int mouseY, float partialTicks ) {
		drawDefaultBackground();
		
		super.drawScreen( mouseX, mouseY, partialTicks );
		GuiTinyWrenchOverlay.instance.render( new String[] { "Example Overlay", "Tiny Wrench Tooltip", "Position Guide" }, new ItemStack[] { new ItemStack( Items.DIAMOND ), new ItemStack( Blocks.BEDROCK ) } );
	}
	
}
