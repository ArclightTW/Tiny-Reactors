package com.immersiveworks.tinyreactors.common.items;

import java.util.List;

import com.immersiveworks.tinyreactors.api.wrench.IWrenchable;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyRelay;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemTinyWrench extends ItemTiny {

	public ItemTinyWrench() {
		setMaxStackSize( 1 );
		setContainerItem( this );
	}
	
	@Override
	public void addInformation( ItemStack itemstack, World world, List<String> tooltip, ITooltipFlag flag ) {
		if( itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey( "linkedPosition" ) ) {
			BlockPos pos = NBTUtil.getPosFromTag( itemstack.getTagCompound().getCompoundTag( "linkedPosition" ) );
			tooltip.add( String.format( "Linked: [%s%d,%d,%d%s]", TextFormatting.LIGHT_PURPLE, pos.getX(), pos.getY(), pos.getZ(), TextFormatting.GRAY ) );
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick( World world, EntityPlayer player, EnumHand hand ) {
		ItemStack itemstack = player.getHeldItem( hand );
		
		if( player.isSneaking() && itemstack.hasTagCompound() )
			itemstack.setTagCompound( new NBTTagCompound() );
		
		return ActionResult.newResult( EnumActionResult.SUCCESS, itemstack );
	}
	
	@Override
	public EnumActionResult onItemUseFirst( EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand ) {
		ItemStack itemstack = player.getHeldItem( hand );
		if( !itemstack.hasTagCompound() )
			itemstack.setTagCompound( new NBTTagCompound() );
		
		IBlockState state = world.getBlockState( pos );
		if( state.getBlock() instanceof IWrenchable )
			if( ( ( IWrenchable )state.getBlock() ).onWrenched( world, pos, facing, player, itemstack, hitX, hitY, hitZ ) ) 
				return EnumActionResult.SUCCESS;
		
		if( !world.isRemote )
			if( itemstack.getTagCompound().hasKey( "linkedPosition" ) && attemptRelayConnection( itemstack, world, pos, player, facing ) )
				return EnumActionResult.SUCCESS;
		
		return EnumActionResult.PASS;
	}
	
	private boolean attemptRelayConnection( ItemStack itemstack, World world, BlockPos pos, EntityPlayer player, EnumFacing facing ) {
		BlockPos relayPos = NBTUtil.getPosFromTag( itemstack.getTagCompound().getCompoundTag( "linkedPosition" ) );

		if( relayPos.equals( pos ) )
			return false;
		
		IBlockState relayState = world.getBlockState( relayPos );
		if( relayState.getBlock() != Blocks.ENERGY_RELAY ) {
			player.sendMessage( new TextComponentString( "Error: The linked Energy Relay has been moved or destroyed!" ) );
			itemstack.getTagCompound().removeTag( "linkedPosition" );
			return false;
		}
		
		TileEntity tile = world.getTileEntity( pos );
		if( tile == null || !tile.hasCapability( CapabilityEnergy.ENERGY, facing ) )
			return false;
		
		( ( TileEntityEnergyRelay )world.getTileEntity( relayPos ) ).addDestination( pos );
		
		player.sendMessage( new TextComponentString( "Success: Energy Relay linked to Energy-compatible block!" ) );
		itemstack.getTagCompound().removeTag( "linkedPosition" );
		return true;
	}
	
}
