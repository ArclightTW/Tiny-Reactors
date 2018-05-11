package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.api.manual.pages.ManualPage;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageRequirement;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageTextDetails;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageTextStructure;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageRequirement.Requirement;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorGlass;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReactorGlass extends BlockTinyTile<TileEntityReactorGlass> {

	private static final PropertyBool STRUCTURE = PropertyBool.create( "structure" );
	
	public BlockReactorGlass() {
		super( Material.IRON, TileEntityReactorGlass.class );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
		
		setDefaultState( blockState.getBaseState().withProperty( STRUCTURE, false ) );
	}
	
	@Override
	public IProperty<?>[] getListedProperties() {
		return new IProperty<?>[] {
			STRUCTURE
		};
	}
	
	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess world, BlockPos pos ) {
		return state.withProperty( STRUCTURE, getTileEntity( world, pos ).getController() != null );
	}
	
	@Override
	public int getMetaFromState( IBlockState state ) {
		return 0;
	}
	
	@Override
	public void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed ) {
		TileEntityReactorGlass glass = getTileEntity( world, pos );
		if( glass == null || glass.getController() == null )
			return;
		
		BlockPos p = glass.getController().getPos();
		if( removed != null && removed.getX() == p.getX() && removed.getY() == p.getY() && removed.getZ() == p.getZ() )
			glass.onStructureValidated( null );
	}
	
	@Override
	public boolean isOpaqueCube( IBlockState state ) {
		return false;
	}
	
	@Override
	public boolean isFullCube( IBlockState state ) {
		return false;
	}
	
	@Override
	public String getManualKey() {
		return "reactor_glass";
	}
	
	@Override
	public ManualPage[] getManualPages() {
		return new ManualPage[] {
				new ManualPageRequirement( Requirement.OPTIONAL ),
				new ManualPageTextDetails( this ),
				new ManualPageTextStructure( this )
		};
	}
	
}
