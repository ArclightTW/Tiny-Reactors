package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork.Priority;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorController;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

// TODO: Render text on screen (like a log)
public class BlockReactorController extends BlockTinyTile<TileEntityReactorController> implements IEnergyNetworkBlockRenderer {

	public BlockReactorController() {
		super( Material.IRON, TileEntityReactorController.class );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
		
		setDefaultState( blockState.getBaseState().withProperty( BlockDirectional.FACING, EnumFacing.NORTH ) );
	}
	
	@Override
	public IProperty<?>[] getListedProperties() {
		return new IProperty<?>[] {
			BlockDirectional.FACING
		};
	}
	
	@Override
	public IBlockState getStateForPlacement( World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand ) {
		return getDefaultState().withProperty( BlockDirectional.FACING, placer.getHorizontalFacing().getOpposite() );
	}
	
	@Override
	public IBlockState getStateFromMeta( int meta ) {
		return getDefaultState().withProperty( BlockDirectional.FACING, EnumFacing.getFront( meta ) );
	}
	
	@Override
	public int getMetaFromState( IBlockState state ) {
		return state.getValue( BlockDirectional.FACING ).getIndex();
	}
	
	@Override
	public IBlockState withRotation( IBlockState state, Rotation rot ) {
		return state.withProperty( BlockDirectional.FACING, rot.rotate( state.getValue( BlockDirectional.FACING ) ) );
	}
	
	@Override
	public IBlockState withMirror( IBlockState state, Mirror mirror ) {
		return withRotation( state, mirror.toRotation( state.getValue( BlockDirectional.FACING ) ) );
	}
		
	@Override
	public Priority getEnergyNetworkPriority() {
		return Priority.HIGH;
	}
	
	@Override
	public void onEnergyNetworkRefreshed( World world, BlockPos pos, BlockPos removed ) {
		TileEntityReactorController controller = getTileEntity( world, pos );
		if( controller == null )
			return;
		
		controller.getStructure().validateStructure( world, pos, removed );
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		TileEntityReactorController controller = getTileEntity( world, pos );
		StorageReactor structure = controller.getStructure();
		
		if( !structure.isValid() )
			return new String[] { "Operational: false" };
		
		if( !Configs.REACTOR_TEMPERATURE )
			return new String[] { "Operational: true" };
		
		float change = structure.getTemperatureGain() - structure.getTemperatureCooldown();
		String changeString = String.format( "%s%,.2f C/t%s", change > 0 ? TextFormatting.RED + "+" : TextFormatting.GREEN, change, TextFormatting.WHITE );
		if( change > -0.001F && change < 0.001F )
			changeString = "0.00 C/t";
		
		String tempString = String.format( "Temperature: %,.0f C / %,.0f C (%s)", structure.getTemperature().getCurrentTemperature(), structure.getTemperature().getMaximumTemperature(), changeString );
		if( structure.getTemperature().getCurrentTemperature() > structure.getTemperature().getMaximumTemperature() )
			tempString = String.format( "%sOverheated", TextFormatting.RED );
		
		return new String[] {
				"Operational: true",
				tempString,
				String.format( "Energy: %,d RF/t", structure.getEnergyGain() )
		};
	}
	
}
