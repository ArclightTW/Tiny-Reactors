package com.immersiveworks.tinyreactors.client.events;

import com.immersiveworks.tinyreactors.api.helpers.PlayerHelper;
import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.client.gui.GuiTinyWrenchOverlay;
import com.immersiveworks.tinyreactors.client.util.ParticleRenderDispatcher;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber( modid = TinyReactors.ID, value = Side.CLIENT )
public class RenderEvents {

	@SubscribeEvent
	public static void onItemTooltip( ItemTooltipEvent event ) {
		if( !event.getItemStack().hasTagCompound() )
			return;
		
		if( !event.getItemStack().getTagCompound().getBoolean( "hasInbuiltWrench" ) )
			return;
		
		event.getToolTip().add( 1, TextFormatting.LIGHT_PURPLE + "[Wrench Included]" );
	}
	
	@SubscribeEvent
	public static void onRenderWorldLast( RenderWorldLastEvent event ) {
		ParticleRenderDispatcher.dispatch();
	}
	
	@SubscribeEvent
	public static void onRenderGameOverlay( RenderGameOverlayEvent.Post event ) {
		if( event.getType() != ElementType.EXPERIENCE )
			return;
		
		Minecraft mc = Minecraft.getMinecraft();
		if( !PlayerHelper.hasWrenchVisibility( mc.player ) )
			return;

		RayTraceResult result = mc.world.rayTraceBlocks( mc.player.getPositionEyes( 1F ), mc.player.getPositionEyes( 1F ).add( mc.player.getLookVec().normalize().scale( 10F ) ), false );
		if( result == null || result.typeOfHit != RayTraceResult.Type.BLOCK )
			return;
		
		IBlockState state = mc.world.getBlockState( result.getBlockPos() );
		if( state == null || !( state.getBlock() instanceof IEnergyNetworkBlockRenderer ) ) 
			return;
		
		GuiTinyWrenchOverlay.instance.render( mc.world, mc.player, result.getBlockPos(), state, ( IEnergyNetworkBlockRenderer )state.getBlock(), mc.world.getTileEntity( result.getBlockPos() ), result.sideHit, result.hitVec );
	}
	
}
