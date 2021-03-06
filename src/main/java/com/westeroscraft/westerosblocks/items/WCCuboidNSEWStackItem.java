package com.westeroscraft.westerosblocks.items;

import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWBlock;
import com.westeroscraft.westerosblocks.blocks.WCCuboidNSEWStackBlock;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WCCuboidNSEWStackItem extends MultiBlockItem {

    public WCCuboidNSEWStackItem(Block par1) {
        super(par1);
    }
    
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing != EnumFacing.UP)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (!block.isReplaceable(worldIn, pos))
            {
                pos = pos.offset(facing);
            }

            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(pos, facing, itemstack) && this.block.canPlaceBlockAt(worldIn, pos))
            {
                EnumFacing enumfacing = EnumFacing.fromAngle((double)player.rotationYaw);
                placeCuboidBlock(worldIn, pos, enumfacing, (WCCuboidNSEWStackBlock) this.block, (itemstack.getItemDamage() & 0x2) >> 1);
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
    }
    
    public static void placeCuboidBlock(World worldIn, BlockPos pos, EnumFacing facing, WCCuboidNSEWStackBlock cblock, int var)
    {
        BlockPos blockpos2 = pos.up();
        IBlockState state = cblock.getDefaultState().withProperty(cblock.variant, var);
        state = state.withProperty(WCCuboidNSEWBlock.FACING, facing);
        worldIn.setBlockState(pos, state.withProperty(WCCuboidNSEWStackBlock.TOP, false), 2);
        worldIn.setBlockState(blockpos2, state.withProperty(WCCuboidNSEWStackBlock.TOP, true), 2);
        worldIn.notifyNeighborsOfStateChange(pos, cblock, false);
        worldIn.notifyNeighborsOfStateChange(blockpos2, cblock, false);
    }
}
