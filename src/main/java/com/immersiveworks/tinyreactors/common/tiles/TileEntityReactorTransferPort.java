package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.api.item.IInternalInventory;
import com.immersiveworks.tinyreactors.api.item.IItemHandlerNBT;
import com.immersiveworks.tinyreactors.api.item.ItemHandlerNBT;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.properties.EnumTransferPort;
import com.immersiveworks.tinyreactors.common.util.Reactants;

import net.minecraft.block.BlockDirectional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityReactorTransferPort extends TileEntityTinyEnergy implements IReactorTile, IInternalInventory {

	public int powerDemand;
	
	private IItemHandlerNBT item;
	private FluidTank liquid;
	
	private int counter;
	private EnumTransferPort mode;
	
	private BlockPos controllerPos;
	private TileEntityReactorController controller;
		
	public TileEntityReactorTransferPort() {
		super( 1000000, 1, 1024 );
		registerCapability( "energy", CapabilityEnergy.ENERGY, ( facing ) -> {
			return world.getBlockState( pos ).getValue( BlockDirectional.FACING ) == facing && mode == EnumTransferPort.ENERGY ? energy : null;
		} );
		
		item = new ItemHandlerNBT( 9 );
		registerCapability( "item", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ( facing ) -> {
			return world.getBlockState( pos ).getValue( BlockDirectional.FACING ) == facing && mode == EnumTransferPort.ITEM ? item : null;
		} );
		
		liquid = new FluidTank( Fluid.BUCKET_VOLUME * 16 );
		registerCapability( "liquid", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, ( facing ) -> {
			return world.getBlockState( pos ).getValue( BlockDirectional.FACING ) == facing && mode == EnumTransferPort.LIQUID ? liquid : null;
		} );
		
		counter = 0;
		mode = EnumTransferPort.ENERGY;
	}
	
	@Override
	protected void handleEnergyTransfer() {
		switch( mode ) {
		case ENERGY:
			powerDemand = -1;;
			super.handleEnergyTransfer();
			break;
		case ITEM:
			powerDemand = 0;
			
			int maxRate = -1;
			int slotToUse = -1;
			for( int i = 0; i < item.getSlots(); i++ ) {
				ItemStack itemstack = item.getStackInSlot( i );
				if( !Reactants.isValidReactant( itemstack ) )
					continue;
				
				int rate = Reactants.getReactantRate( itemstack );
				if( rate < maxRate )
					continue;
				
				maxRate = rate;
				slotToUse = i;
			}
			
			try {
				if( slotToUse == -1 || maxRate <= 0 || controller == null || !controller.getStructure().hasAvailableSpace() ) {
					counter = 0;
					return;
				}
			}
			catch( Exception e ) {
				counter = 0;
				return;
			}
			
			counter++;
			if( counter < Configs.TRANSFER_ITEM_USAGE_DELAY )
				break;
			powerDemand = Configs.TRANSFER_ITEM_ENERGY_USAGE;
			
			if( energy.getEnergyStored() <= Configs.TRANSFER_ITEM_ENERGY_USAGE )
				return;
			counter = 0;
			powerDemand = 0;
			
			energy.extractEnergy( Configs.TRANSFER_ITEM_ENERGY_USAGE, false );
			controller.getStructure().insertBlock( world, item.extractItem( slotToUse, 1, false ) );
			syncClient();
			
			break;
		case LIQUID:
			powerDemand = 0; //Configs.TRANSFER_LIQUID_ENERGY_USAGE;
			// TODO: Handle LIQUID mode (see Tips and Tricks)
			//energy.extractEnergy( Configs.TRANSFER_LIQUID_ENERGY_USAGE, false );
			break;
		}
	}
	
	@Override
	protected void transferEnergy( BlockPos pos, IEnergyStorageWrapper energy, int amount ) {
		if( mode != EnumTransferPort.ENERGY )
			return;
		
		super.transferEnergy( pos, energy, amount );
	}
	
	@Override
	public void onStructureValidated( TileEntityReactorController controller ) {
		this.controller = controller;
		this.controllerPos = this.controller != null ? this.controller.getPos() : null;
		syncClient();
	}
	
	@Override
	public void onLoad() {
		controller = null;
		if( controllerPos == null )
			return;
		
		TileEntity tile = world.getTileEntity( controllerPos );
		if( tile != null && tile instanceof TileEntityReactorController )
			controller = ( TileEntityReactorController )tile;
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		item.writeToNBT( compound );
		liquid.writeToNBT( compound );
		
		NBTTagCompound transferPort = new NBTTagCompound();
		transferPort.setInteger( "powerDemand", powerDemand );
		transferPort.setInteger( "counter", counter );
		transferPort.setString( "mode", mode.name() );
		
		if( controllerPos != null ) transferPort.setTag( "controller", NBTUtil.createPosTag( controllerPos ) );
		
		compound.setTag( "transferPort", transferPort );
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		item.readFromNBT( compound );
		liquid.readFromNBT( compound );
		
		NBTTagCompound transferPort = compound.getCompoundTag( "transferPort" );
		powerDemand = transferPort.getInteger( "powerDemand" );
		counter = transferPort.getInteger( "counter" );
		mode = EnumTransferPort.valueOf( transferPort.getString( "mode" ) );
		
		controllerPos = transferPort.hasKey( "controller" ) ? NBTUtil.getPosFromTag( transferPort.getCompoundTag( "controller" ) ) : null;
		if( world != null )
			onLoad();
	}
	
	@Override
	public boolean isAccessible() {
		return mode == EnumTransferPort.ITEM;
	}
	
	@Override
	public IItemHandlerNBT getInternalItem() {
		return item;
	}
	
	public ItemStack insertReactant( ItemStack itemstack ) {
		if( mode != EnumTransferPort.ITEM || !Reactants.isValidReactant( itemstack ) )
			return itemstack;
		
		int emptySlot = -1;
		
		for( int i = 0; i < item.getSlots(); i++ ) {
			ItemStack current = item.getStackInSlot( i );
			if( current.isEmpty() ) {
				if( emptySlot == -1 )
					emptySlot = i;
				
				continue;
			}
			
			if( !current.isItemEqual( itemstack ) )
				continue;
			
			itemstack = item.insertItem( i, itemstack, false );
			if( itemstack.isEmpty() )
				return ItemStack.EMPTY;
		}
		
		if( emptySlot == -1 )
			return itemstack;
		
		ItemStack remaining = item.insertItem( emptySlot, itemstack, false );
		syncClient();
		return remaining;
	}
	
	public ItemStack retreiveReactant() {
		for( int i = item.getSlots() - 1; i >= 0; i-- ) {
			if( item.getStackInSlot( i ).isEmpty() )
				continue;
			
			ItemStack taken = item.extractItem( i, Integer.MAX_VALUE, false );
			syncClient();
			return taken;
		}
		
		return ItemStack.EMPTY;
	}
	
	public void toggleTransferMode() {
		mode = mode.next();
		syncClient();
	}
	
	public EnumTransferPort getTransferMode() {
		return mode;
	}
	
	public String getStatus() {
		switch( mode ) {
		case ENERGY:
			return "Running";
		case ITEM:
			if( powerDemand > 0 )
				return "Charging";
			
			if( controller != null && !controller.getStructure().hasAvailableSpace() )
				return "Reactor Full";
			
			for( int i = 0; i < item.getSlots(); i++ ) {
				if( !item.getStackInSlot( i ).isEmpty() )
					return String.format( "Cooling (%d ticks)", Configs.TRANSFER_ITEM_USAGE_DELAY - counter );
			}
			
			return "Missing Inventory";
		case LIQUID:
			return "Inoperable";
		default:
			return "Invalid";
		}
	}
	
}
