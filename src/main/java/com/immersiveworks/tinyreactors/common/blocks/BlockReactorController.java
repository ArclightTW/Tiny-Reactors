package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.api.manual.IManualEntryBlock;
import com.immersiveworks.tinyreactors.api.temperature.ITemperatureStorage;
import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPage;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPageReactorBlock;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPageText;
import com.immersiveworks.tinyreactors.client.gui.manual.pages.ManualPageReactorBlock.Requirement;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork.Priority;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorController;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockReactorController extends BlockTinyTile<TileEntityReactorController> implements IEnergyNetworkBlockRenderer, IManualEntryBlock {

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
	public boolean onWrenched( World world, BlockPos pos, EnumFacing facing, EntityPlayer player, ItemStack itemstack ) {
		TileEntityReactorController controller = getTileEntity( world, pos );
		
		if( !world.isRemote && player.isSneaking() ) {
			controller.setActive( !controller.isManuallyActive(), true );
			return true;
		}
		
		return false;
	}
	
	@Override
	public String[] getWrenchOverlayInfo( World world, BlockPos pos, IBlockState state ) {
		TileEntityReactorController controller = getTileEntity( world, pos );
		StorageReactor structure = controller.getStructure();
		
		if( !structure.isValid() )
			return new String[] { "Operational: Invalid Structure" };
		
		if( !Configs.REACTOR_TEMPERATURE )
			return new String[] {
					String.format( "Status: %s", controller.isManuallyActive() ? controller.isActive() ? "Operational" : "Auto-Cooling" : "Deactivated" ),
					String.format( "Energy: %,d RF/t", structure.getEnergyGain() )
				};
		
		ITemperatureStorage temperature = structure.getTemperature();
		
		float change;
		if( controller.isActive() )
			change = structure.getTemperatureGain() - structure.getTemperatureCooldown();
		else {
			change = structure.getTemperatureGain() + structure.getTemperatureCooldown();
			if( change > 0 )
				change *= -1;
		}
		
		String changeString = String.format( "%s%,.2f C/t%s", change > 0 ? TextFormatting.RED + "+" : TextFormatting.GREEN, change, TextFormatting.WHITE );
		if( change > -0.001F && change < 0.001F )
			changeString = "0.00 C/t";
		
		TextFormatting tempColor = TextFormatting.AQUA;
		if( temperature.getCurrentTemperature() >= temperature.getMaximumTemperature() * 0.5F )
			tempColor = TextFormatting.LIGHT_PURPLE;
		if( temperature.getCurrentTemperature() >= temperature.getPeakEfficiencyTemperature() * 0.9F && temperature.getCurrentTemperature() <= temperature.getPeakEfficiencyTemperature() * 1.1F )
			tempColor = TextFormatting.GREEN;
		if( temperature.getCurrentTemperature() >= temperature.getCriticalTemperature() * 0.9F )
			tempColor = TextFormatting.YELLOW;
		if( temperature.getCurrentTemperature() >= temperature.getCriticalTemperature() )
			tempColor = TextFormatting.RED;
			
		String tempString = String.format( "Heat:%s %,.0f C %s/ %,.0f C (%s)", tempColor, temperature.getCurrentTemperature(), TextFormatting.WHITE, temperature.getMaximumTemperature(), changeString );
		if( temperature.getCurrentTemperature() > temperature.getMaximumTemperature() )
			tempString = String.format( "%sOverheated", TextFormatting.RED );
		
		return new String[] {
				String.format( "Status: %s", controller.isManuallyActive() ? controller.isActive() ? "Operational" : "Auto-Cooling" : "Deactivated" ),
				tempString,
				String.format( "Energy: %,d RF/t", structure.getEnergyGain() )
		};
	}
	
	@Override
	public String getManualKey() {
		return "reactor_controller";
	}
	
	@Override
	public String getManualHeader() {
		return getUnlocalizedName() + ".name";
	}
	
	@Override
	public float getManualHeaderScale() {
		return 0.9F;
	}
	
	@Override
	public ItemStack getManualIcon() {
		return new ItemStack( Blocks.REACTOR_CONTROLLER );
	}
	
	@Override
	public ManualPage[] getManualPages() {
		return new ManualPage[] {
				new ManualPageReactorBlock( Requirement.REQUIRED, "tiny_manual:page.required" ),
				new ManualPageText( "tiny_manual:header.details", "tiny_manual:page.reactor_controller" )
		};
	}
	
}
