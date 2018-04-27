package com.immersiveworks.tinyreactors.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityEnergyCell extends TileEntityTinyEnergy {

	private boolean[] inputs;
	
	public TileEntityEnergyCell() {
		super( 10000000, 10, 2000 );

		registerCapability( "energy", CapabilityEnergy.ENERGY, ( facing ) -> {
			energy.setCanReceive( facing, canInput( facing ) );
			energy.setCanExtract( facing, !canInput( facing ) );
			return energy;
		} );
		
		inputs = new boolean[ EnumFacing.VALUES.length ];
		for( int i = 0; i < inputs.length; i++ )
			inputs[ i ] = false;
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		
		NBTTagList inputs = new NBTTagList();
		for( int i = 0; i < this.inputs.length; i++ )
			inputs.appendTag( new NBTTagFloat( this.inputs[ i ] ? 1 : 0 )  );
		compound.setTag( "inputs", inputs );
		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		
		NBTTagList inputs = compound.getTagList( "inputs", Constants.NBT.TAG_FLOAT );
		for( int i = 0; i < inputs.tagCount(); i++ )
			this.inputs[ i ] = inputs.getFloatAt( i ) == 1;
	}
	
	public void toggleInput( EnumFacing facing ) {
		inputs[ facing.ordinal() ] = !inputs[ facing.ordinal() ];
		syncClient();
	}
	
	public boolean canInput( EnumFacing facing ) {
		if( inputs == null || facing.ordinal() >= inputs.length )
			return false;
		return inputs[ facing.ordinal() ];
	}
	
}
