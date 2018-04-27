package com.immersiveworks.tinyreactors.common.properties;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.energy.CapabilityEnergy;

public enum EnumConnection {
	
	EAST,
	WEST,
	
	UP,
	DOWN,
	
	NORTH,
	SOUTH;
	
	public static EnumConnection[] VALUES = { EAST, WEST, UP, DOWN, NORTH, SOUTH };
	
	private PropertyBool property;
	private EnumFacing facing;
	
	private EnumConnection() {
		property = PropertyBool.create( name().toLowerCase() );
		facing = EnumFacing.valueOf( name() );
	}
	
	public PropertyBool getProperty() {
		return property;
	}
	
	public EnumFacing getFacing() {
		return facing;
	}
	
	public AxisAlignedBB expand( AxisAlignedBB bounding ) {
		return bounding.expand(
				facing.getDirectionVec().getX() *( 5 / 16F ),
				facing.getDirectionVec().getY() *( 5 / 16F ),
				facing.getDirectionVec().getZ() *( 5 / 16F )
			);
	}
	
	public boolean canConnect( IBlockAccess world, BlockPos pos ) {
		TileEntity tile = world.getTileEntity( pos.offset( facing ) );
		return tile != null && tile.hasCapability( CapabilityEnergy.ENERGY, facing.getOpposite() );
	}
	
}
