package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ArchitectsLevelItem extends Item {
    public ArchitectsLevelItem(Properties properties) {
        super(properties);
    }

    private boolean hasRequiredBlock(Player player, Item item) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (player.getInventory().getItem(i).is(item)) {
                return true;
            }
        }
        return false;
    }

    private void consumeItem(Player player, Item item) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.is(item)) {
                stack.shrink(1);
                break;
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos centerPos = context.getClickedPos();
        Player player = context.getPlayer();

        if (!level.isClientSide() && player != null) {
            int radius = 1;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    boolean canPlace = player.isCreative() || hasRequiredBlock(player, Blocks.DIRT.asItem());

                    BlockPos targetFloor = centerPos.offset(x, 0, z);
                    BlockPos targetAbove = targetFloor.above();
                    BlockPos targetAbove2 = targetAbove.above();

                    if (level.getBlockState(targetAbove).is(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                            || level.getFluidState(targetAbove).is(FluidTags.WATER) || level.getFluidState(targetAbove).is(FluidTags.LAVA)) {
                        level.destroyBlock(targetAbove, true, player);
                        context.getItemInHand().hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player,
                                item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    }

                    if (level.getBlockState(targetAbove2).is(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                            || level.getFluidState(targetAbove2).is(FluidTags.WATER) || level.getFluidState(targetAbove2).is(FluidTags.LAVA)) {
                        level.destroyBlock(targetAbove2, true, player);
                        context.getItemInHand().hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player,
                                item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    }

                    if (level.getBlockState(targetFloor).is(ModTags.Blocks.NATURAL_BLOCKS_LEVEL)
                            || level.getFluidState(targetFloor).is(FluidTags.WATER) || level.getFluidState(targetFloor).is(FluidTags.LAVA)
                            || !level.getBlockState(targetFloor).is(Blocks.AIR) || !level.getBlockState(targetFloor).is(Blocks.CAVE_AIR)
                            || !level.getBlockState(targetFloor).is(Blocks.VOID_AIR)) {

                        if (!level.getBlockState(targetFloor).is(Blocks.DIRT) && canPlace) {
                            level.destroyBlock(targetFloor, true, player);
                            level.setBlock(targetFloor, Blocks.DIRT.defaultBlockState(), 3);
                            if (!player.isCreative()) {
                                consumeItem(player, Blocks.DIRT.asItem());
                            }
                            context.getItemInHand().hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player,
                                    item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                        }
                    }
                }
            }

            player.getCooldowns().addCooldown(this, 5);
        }

        return InteractionResult.SUCCESS;
    }
}