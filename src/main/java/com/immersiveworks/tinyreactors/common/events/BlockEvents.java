package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork;
import com.immersiveworks.tinyreactors.common.energy.IEnergyNetworkBlock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class BlockEvents {

	@SubscribeEvent
	public static void onPlace( BlockEvent.PlaceEvent event ) {
		if( event.getPlacedBlock().getBlock() instanceof IEnergyNetworkBlock ) {
			EnergyNetwork.get( event.getWorld() ).addComponent( event.getWorld(), event.getPos(), ( IEnergyNetworkBlock )event.getPlacedBlock().getBlock() );
			return;
		}
		
		TileEntity tile = event.getBlockSnapshot().getTileEntity();
		if( tile == null )
			return;
		
		for( EnumFacing facing : EnumFacing.VALUES ) {
			IEnergyStorage energy = tile.getCapability( CapabilityEnergy.ENERGY, facing );
			if( energy == null )
				continue;
			
			EnergyNetwork.get( event.getWorld() ).refreshAll( event.getWorld(), null );
			break;
		}
	}
	
	@SubscribeEvent
	public static void onBreak( BlockEvent.BreakEvent event ) {
		if( event.getState().getBlock() instanceof IEnergyNetworkBlock ) {
			EnergyNetwork.get( event.getWorld() ).removeComponent( event.getWorld(), event.getPos() );
			return;
		}
		
		TileEntity tile = event.getWorld().getTileEntity( event.getPos() );
		if( tile == null )
			return;
		
		for( EnumFacing facing : EnumFacing.VALUES ) {
			IEnergyStorage energy = tile.getCapability( CapabilityEnergy.ENERGY, facing );
			if( energy == null )
				continue;
			
			EnergyNetwork.get( event.getWorld() ).removeComponent( event.getWorld(), event.getPos() );
			break;
		}
	}
	
}
