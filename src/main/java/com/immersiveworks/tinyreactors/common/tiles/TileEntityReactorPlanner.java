package com.immersiveworks.tinyreactors.common.tiles;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.util.Reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TileEntityReactorPlanner extends TileEntityTiny {

	private EnumColorOverlay overlay;
	
	private int prevMinX, prevMinY, prevMinZ;
	private int prevMaxX, prevMaxY, prevMaxZ;
	
	private int minX = -1, minY = -1, minZ = -1;
	private int maxX =  1, maxY =  1, maxZ =  1;
	
	private Map<BlockPos, List<IBlockState>> blocks;
	
	public TileEntityReactorPlanner() {
		blocks = Maps.newHashMap();
		overlay = EnumColorOverlay.ALL;
	}
	
	@Override
	public void onLoad() {
		amendStructure();
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		
		NBTTagCompound planner = new NBTTagCompound();
		planner.setInteger( "overlay", overlay.ordinal() );
		planner.setInteger( "prevMinX", prevMinX );
		planner.setInteger( "prevMinY", prevMinY );
		planner.setInteger( "prevMinZ", prevMinZ );
		planner.setInteger( "prevMaxX", prevMaxX );
		planner.setInteger( "prevMaxY", prevMaxY );
		planner.setInteger( "prevMaxZ", prevMaxZ );
		planner.setInteger( "minX", minX );
		planner.setInteger( "minY", minY );
		planner.setInteger( "minZ", minZ );
		planner.setInteger( "maxX", maxX );
		planner.setInteger( "maxY", maxY );
		planner.setInteger( "maxZ", maxZ );
		
		NBTTagList blocks = new NBTTagList();
		for( Map.Entry<BlockPos, List<IBlockState>> block : this.blocks.entrySet() ) {
			NBTTagCompound blockTag = new NBTTagCompound();
			blockTag.setTag( "pos", NBTUtil.createPosTag( block.getKey() ) );
			
			NBTTagList reg = new NBTTagList();
			for( IBlockState state : block.getValue() ) {
				NBTTagCompound regTag = new NBTTagCompound();
				regTag.setString( "registry", state.getBlock().getRegistryName().toString() );
				regTag.setInteger( "metadata", state.getBlock().getMetaFromState( state ) );
				reg.appendTag( regTag );
			}
			blockTag.setTag( "registries", reg );
			
			blocks.appendTag( blockTag );
		}
		planner.setTag( "blocks", blocks );
		
		compound.setTag( "planner", planner );
		return compound;
	}
	
	@Override
	@SuppressWarnings( "deprecation" )
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		NBTTagCompound planner = compound.getCompoundTag( "planner" );
		overlay = EnumColorOverlay.values()[ planner.getInteger( "overlay" ) ];
		prevMinX = planner.getInteger( "prevMinX" );
		prevMinY = planner.getInteger( "prevMinY" );
		prevMinZ = planner.getInteger( "prevMinZ" );
		prevMaxX = planner.getInteger( "prevMaxX" );
		prevMaxY = planner.getInteger( "prevMaxY" );
		prevMaxZ = planner.getInteger( "prevMaxZ" );
		minX = planner.getInteger( "minX" );
		minY = planner.getInteger( "minY" );
		minZ = planner.getInteger( "minZ" );
		maxX = planner.getInteger( "maxX" );
		maxY = planner.getInteger( "maxY" );
		maxZ = planner.getInteger( "maxZ" );
		
		NBTTagList blocks = compound.getTagList( "blocks", Constants.NBT.TAG_COMPOUND );
		for( int i = 0; i < blocks.tagCount(); i++ ) {
			NBTTagCompound blockTag = blocks.getCompoundTagAt( i );
			BlockPos pos = NBTUtil.getPosFromTag( blockTag.getCompoundTag( "pos" ) );
			
			this.blocks.put( pos, Lists.newArrayList() );
			
			NBTTagList reg = blockTag.getTagList( "registries", Constants.NBT.TAG_COMPOUND );
			for( int j = 0; j < reg.tagCount(); j++ ) {
				NBTTagCompound regTag = reg.getCompoundTagAt( j );
				String registry = regTag.getString( "registry" );
				int metadata = regTag.getInteger( "metadata" );
				
				this.blocks.get( pos ).add( ForgeRegistries.BLOCKS.getValue( new ResourceLocation( registry ) ).getStateFromMeta( metadata ) );
			}
		}
		
		if( world != null )
			onLoad();
	}
	
	public void toggleColorOverlay() {
		overlay = overlay.next();
		syncClient();
	}
	
	public void amendSize( EnumFacing facing, boolean shrink ) {
		switch( facing ) {
		case NORTH:
			minZ += shrink ? 1 : -1;
			if( minZ > -1 )
				minZ = -1;
			break;
		case SOUTH:
			maxZ += shrink ? -1 : 1;
			if( maxZ < 1 )
				maxZ = 1;
			break;
			
		case EAST:
			maxX += shrink ? -1 : 1;
			if( maxX < 1 )
				maxX = 1;
			break;
		case WEST:
			minX += shrink ? 1 : -1;
			if( minX > -1 )
				minX = -1;
			break;
			
		case UP:
			maxY += shrink ? -1 : 1;
			if( maxY < 1 )
				maxY = 1;
			break;
		case DOWN:
			minY += shrink ? 1 : -1;
			if( minY > -1 )
				minY = -1;
			break;
		}
		
		syncClient();
	}
	
	public Map<BlockPos, List<IBlockState>> getBlocks() {
		return blocks;
	}
	
	public EnumColorOverlay getColorOverlay() {
		return overlay;
	}
	
	public void setPrevMinX( int x ) { prevMinX = x; }
	public int getPrevMinX() { return prevMinX; }
	public void setPrevMinY( int y ) { prevMinY = y; }
	public int getPrevMinY() { return prevMinY; }
	public void setPrevMinZ( int z ) { prevMinZ = z; }
	public int getPrevMinZ() { return prevMinZ; }
	public void setPrevMaxX( int x ) { prevMaxX = x; }
	public int getPrevMaxX() { return prevMaxX; }
	public void setPrevMaxY( int y ) { prevMaxY = y; }
	public int getPrevMaxY() { return prevMaxY; }
	public void setPrevMaxZ( int z ) { prevMaxZ = z; }
	public int getPrevMaxZ() { return prevMaxZ; }
	
	public int getMinX() { return minX; }
	public int getMinY() { return minY; }
	public int getMinZ() { return minZ; }
	public int getMaxX() { return maxX; }
	public int getMaxY() { return maxY; }
	public int getMaxZ() { return maxZ; }
	
	private void amendStructure() {
		blocks.clear();
		
		EnumFacing facing = world.getBlockState( pos ).getValue( BlockDirectional.FACING );
		
		for( int x = minX; x <= maxX; x++ )
			for( int z = minZ; z <= maxZ; z++ )
				for( int y = minY; y <= maxY; y++ ) {
					if( x > minX && x < maxX && y > minY && y < maxY && z > minZ && z < maxZ )
						continue;
					
					BlockPos p = pos.add( x, y, z );
					
					blocks.put( p, Lists.newArrayList( Blocks.REACTOR_CASING.getDefaultState() ) );
					
					if( y == maxY ) {
						if( ( x == minX && z == minZ ) || ( x == minX && z == maxZ ) || ( x == maxX && z == minZ )|| ( x == maxX && z == maxZ ) )
							addBlocks( p, facing, Reactor.ROOF_CORNERS );
						else if( x == minX || x == maxX || z == minZ || z == maxZ )
							addBlocks( p, facing, Reactor.ROOF_WALLS );
						else
							addBlocks( p, facing, Reactor.ROOF_INTERIORS );
					}
					else if( y == minY ) {
						if( ( x == minX && z == minZ ) || ( x == minX && z == maxZ ) || ( x == maxX && z == minZ )|| ( x == maxX && z == maxZ ) )
							addBlocks( p, facing, Reactor.BASE_CORNERS );
						else if( x == minX || x == maxX || z == minZ || z == maxZ )
							addBlocks( p, facing, Reactor.BASE_WALLS );
						else
							addBlocks( p, facing, Reactor.BASE_INTERIORS );
					}
					else {
						if( ( x == minX && z == minZ ) || ( x == minX && z == maxZ ) || ( x == maxX && z == minZ )|| ( x == maxX && z == maxZ ) )
							addBlocks( p, facing, Reactor.CENTRAL_CORNERS );
						else if( x == minX || x == maxX || z == minZ || z == maxZ )
							addBlocks( p, facing, Reactor.CENTRAL_WALLS );
					}
				}
	}
	
	private void addBlocks( BlockPos pos, EnumFacing facing, Block[] blockArr ) {
		if( pos.getZ() == this.pos.getZ() + minZ )
			facing = EnumFacing.NORTH;
		if( pos.getZ() == this.pos.getZ() + maxZ )
			facing = EnumFacing.SOUTH;
		
		if( pos.getX() == this.pos.getX() + maxX )
			facing = EnumFacing.EAST;
		if( pos.getX() == this.pos.getX() + minX )
			facing = EnumFacing.WEST;
		
		for( int i = 0; i < blockArr.length; i++ ) {
			if( blockArr[ i ] == Blocks.REACTOR_CASING )
				continue;
			
			try {
				blocks.get( pos ).add( blockArr[ i ].getDefaultState().withProperty( BlockDirectional.FACING, facing ) );
			}
			catch( Exception e ) {
				blocks.get( pos ).add( blockArr[ i ].getDefaultState() );
			}
		}
	}
	
	public enum EnumColorOverlay {
		
		ALL,
		INVALID_ONLY,
		OFF {
			@Override
			public EnumColorOverlay next() {
				return ALL;
			}
		};
		
		public EnumColorOverlay next() {
			return EnumColorOverlay.values()[ ordinal() + 1 ];
		}
		
	}
	
}
