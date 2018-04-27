package com.immersiveworks.tinyreactors.common.storage;

import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.common.tiles.IReactorTile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class StorageMultiblock {

	public BlockPos origin;
	
	protected List<BlockPos> structure;
	
	protected BlockPos start;
	protected BlockPos end;
	
	protected boolean isValid;
	protected Runnable runnable;
	
	public StorageMultiblock() {
		structure = Lists.newLinkedList();
		start = end = BlockPos.ORIGIN;
	}
	
	public void setValidationListener( Runnable runnable ) {
		this.runnable = runnable;
	}
	
	public void validateStructure( World world, BlockPos origin, BlockPos removed ) {
		for( int i = 0; i < structure.size(); i++ ) {
			try {
				TileEntity tile = world.getTileEntity( structure.get( i ) );
				if(  tile instanceof IReactorTile )
					( ( IReactorTile )tile ).onStructureValidated( null );
			}
			catch( Exception e ) {
			}
		}
		structure.clear();
		
		this.origin = origin;
		
		BlockPos p = BlockPos.ORIGIN;
		IBlockState b = null;
		
		int yTop = 0, yBot = 0;
		
		p = origin;
		while( true ) {
			p = p.up();
			b = world.getBlockState( p );
			
			if( !isValidCasing( b ) )
				break;
			
			yTop = p.getY();
		}
		
		p = origin;
		while( true ) {
			p = p.down();
			b = world.getBlockState( p );
			
			if( !isValidCasing( b ) )
				break;
			
			yBot = p.getY();
		}
		
		if( yTop - yBot < 2 ) {
			setValid( world, false );
			return;
		}
		
		int xStart, xEnd;
		int zStart, zEnd;
		EnumFacing f = null;
		
		p = origin;
		xStart = xEnd = p.getX();
		zStart = zEnd = p.getZ();
		
		for( EnumFacing facing : EnumFacing.HORIZONTALS ) {
			b = world.getBlockState( p.offset( facing ) );
			
			if( !isValidCasing( b ) )
				continue;
			
			f = facing;
			break;
		}
		
		if( f == null ) {
			setValid( world, false );
			return;
		}
		
		int x1 = xStart, z1 = zStart;
		p = new BlockPos( xStart, yTop, zStart );
		while( true ) {
			p = p.offset( f );
			b = world.getBlockState( p );
			
			if( !isValidCasing( b ) )
				break;
			
			x1 = p.getX();
			z1 = p.getZ();
		}
		
		int x2 = xStart, z2 = zStart;
		f = f.getOpposite();
		while( true ) {
			p = p.offset( f );
			b = world.getBlockState( p );
			
			if( !isValidCasing( b ) )
				break;
			
			x2 = p.getX();
			z2 = p.getZ();
		}
		
		boolean flag = false;
		p = new BlockPos( x1, yTop, z1 );
		for( EnumFacing facing : EnumFacing.HORIZONTALS ) {
			b = world.getBlockState( p.offset( facing ) );
			
			if( !isValidCasing( b ) || facing == f || facing == f.getOpposite() )
				continue;
			
			f = facing;
			flag = true;
			break;
		}
		
		if( !flag ) {
			setValid( world, false );
			return;
		}
		
		int x3 = xStart, z3 = zStart;
		p = new BlockPos( x1, yTop, z1 );
		while( true ) {
			p = p.offset( f );
			b = world.getBlockState( p );
			
			if( !isValidCasing( b ) )
				break;
			
			x3 = p.getX();
			z3 = p.getZ();
		}
		
		xStart = Math.min( Math.min( xStart, x1 ), Math.min( x2, x3 ) );
		xEnd = Math.max( Math.max( xStart, x1 ), Math.max( x2, x3 ) );
		
		zStart = Math.min( Math.min( zStart, z1 ), Math.min( z2, z3 ) );
		zEnd = Math.max( Math.max( zStart, z1 ), Math.max( z2, z3 ) );
		
		if( removed != null ) {
			if( removed.getX() >= xStart && removed.getX() <= xEnd && removed.getZ() >= zStart && removed.getZ() <= zEnd && removed.getY() >= yBot && removed.getY() <= yTop ) {
				setValid( world, false );
				return;
			}
		}
		
		if( !onPreCalculation( world ) ) {
			setValid( world, false );
			return;
		}
		
		for( int x = xStart; x <= xEnd; x++ ) {
			for( int z = zStart; z <= zEnd; z++ ) {
				for( int y = yTop; y >= yBot; y-- ) {
					p = new BlockPos( x, y, z );
					b = world.getBlockState( p );
					
					if( y == yTop ) {
						if( ( x == xStart && z == zStart ) || ( x == xStart && z == zEnd ) || ( x == xEnd && z == zStart ) || ( x == xEnd && z == zEnd ) ) {
							if( b.getBlock() == Blocks.AIR || !isValidRoofCorner( b ) ) {
								setValid( world, false );
								return;
							}
						}
						else if( x == xStart || x == xEnd || z == zStart || z == zEnd ) {
							if( b.getBlock() == Blocks.AIR || !isValidRoofWall( b ) ) {
								setValid( world, false );
								return;
							}
						}
						else {
							if( b.getBlock() == Blocks.AIR || !isValidRoofInterior( b ) ) {
								setValid( world, false );
								return;
							}
						}
						
						structure.add( p );
						onExternalBlockDetected( world, p, b );
					}
					else if( y == yBot ) {
						if( ( x == xStart && z == zStart ) || ( x == xStart && z == zEnd ) || ( x == xEnd && z == zStart ) || ( x == xEnd && z == zEnd ) ) {
							if( b.getBlock() == Blocks.AIR || !isValidBaseCorner( b ) ) {
								setValid( world, false );
								return;
							}
						}
						else if( x == xStart || x == xEnd || z == zStart || z == zEnd ) {
							if( b.getBlock() == Blocks.AIR || !isValidBaseWall( b ) ) {
								setValid( world, false );
								return;
							}
						}
						else {
							if( b.getBlock() == Blocks.AIR || !isValidBaseInterior( b ) ) {
								setValid( world, false );
								return;
							}
						}
						
						structure.add( p );
						onExternalBlockDetected( world, p, b );
					}
					else {
						if( ( x == xStart && z == zStart ) || ( x == xStart && z == zEnd ) || ( x == xEnd && z == zStart ) || ( x == xEnd && z == zEnd ) ) {
							if( b.getBlock() == Blocks.AIR || !isValidCentralCorner( b ) ) {
								setValid( world, false );
								return;
							}
							
							structure.add( p );
							onExternalBlockDetected( world, p, b );
						}
						else if( x == xStart || x == xEnd || z == zStart || z == zEnd ) {
							if( b.getBlock() == Blocks.AIR || !isValidCentralWall( b ) ) {
								setValid( world, false );
								return;
							}
							
							structure.add( p );
							onExternalBlockDetected( world, p, b );
						}
						else {
							if( !isInternalAirPermitted() && b.getBlock() == Blocks.AIR ) {
								if( !isValidCentralInterior( b ) ) {
									setValid( world, false );
									return;
								}
							}
							
							onInternalBlockDetected( world, p, b );
						}
					}
				}
			}
		}
		
		if( !onPostCalculation( world ) ) {
			setValid( world, false );
			return;
		}
		
		start = new BlockPos( xStart, yTop, zStart );
		end = new BlockPos( xEnd, yBot, zEnd );
		
		setValid( world, true );
	}
	
	public void writeToNBT( NBTTagCompound compound ) {
		NBTTagCompound storage = new NBTTagCompound();
		storage.setBoolean( "isValid", isValid );
		
		storage.setTag( "origin", NBTUtil.createPosTag( origin ) );
		storage.setTag( "start", NBTUtil.createPosTag( start ) );
		storage.setTag( "end", NBTUtil.createPosTag( end ) );
		
		NBTTagList structure = new NBTTagList();
		for( int i = 0; i < this.structure.size(); i++ )
			structure.appendTag( NBTUtil.createPosTag( this.structure.get( i ) ) );
		storage.setTag( "structure", structure );
		
		compound.setTag( "storage", storage );
	}
	
	public void readFromNBT( NBTTagCompound compound )
	{
		NBTTagCompound storage = compound.getCompoundTag( "storage" );
		setValid( null, storage.getBoolean( "isValid" ) );
		
		origin = NBTUtil.getPosFromTag( storage.getCompoundTag( "origin" ) );
		start = NBTUtil.getPosFromTag( storage.getCompoundTag( "start" ) );
		end = NBTUtil.getPosFromTag( storage.getCompoundTag( "end" ) );
		
		NBTTagList structure = storage.getTagList( "structure", Constants.NBT.TAG_COMPOUND );
		for( int i = 0; i < structure.tagCount(); i++ )
			this.structure.add( NBTUtil.getPosFromTag( structure.getCompoundTagAt( i ) ) );
	}
	
	/**
	 * @return true to continue calculation, false to stop structure validation early<br/>called only if the reactor has valid bounds
	 */
	public boolean onPreCalculation( World world ) {
		return true;
	}
	
	/**
	 * @return true to confirm the structure as valid, false to mark the structure as invalid<br/>called only if the reactor is found fully valid with regards to bounds
	 */
	public boolean onPostCalculation( World world ) {
		onValidationFinalized( world );
		return true;
	}
	
	/**
	 * @return true if air is permitted inside the structure walls, false if all blocks must be filled
	 */
	public boolean isInternalAirPermitted() {
		return true;
	}
	
	/**
	 * Used for post-validation logic that is called regardless of whether the structure is valid or not
	 */
	public void onValidationFinalized( World world ) {
	}
	
	/**
	 * Call this in an implementing TileEntity to update any required logic
	 */
	public void tick( World world ) {
		
	}
	
	public boolean isValidRoofCorner( IBlockState state ) {
		return false;
	}
	
	public boolean isValidRoofWall( IBlockState state ) {
		return false;
	}
	
	public boolean isValidRoofInterior( IBlockState state ) {
		return false;
	}
	
	public boolean isValidBaseCorner( IBlockState state ) {
		return false;
	}
	
	public boolean isValidBaseWall( IBlockState state ) {
		return false;
	}
	
	public boolean isValidBaseInterior( IBlockState state ) {
		return false;
	}
	
	public boolean isValidCentralCorner( IBlockState state ) {
		return false;
	}
	
	public boolean isValidCentralWall( IBlockState state ) {
		return false;
	}
	
	public boolean isValidCentralInterior( IBlockState state ) {
		return false;
	}
	
	public boolean isValidCasing( IBlockState state ) {
		return isValidRoofCorner( state ) || isValidRoofWall( state ) || isValidRoofInterior( state ) ||
				isValidBaseCorner( state ) || isValidBaseWall( state ) || isValidBaseInterior( state ) ||
				isValidCentralCorner( state ) || isValidCentralWall( state );
	}
	
	public void onExternalBlockDetected( World world, BlockPos pos, IBlockState state ) {
	}
	
	public void onInternalBlockDetected( World world, BlockPos pos, IBlockState state ) {
	}
	
	public final void setValid( World world, boolean valid ) {
		isValid = valid;
		
		if( runnable != null )
			runnable.run();
		
		onValidationFinalized( world );
	}
	
	public final boolean isValid() {
		return isValid;
	}
	
}
