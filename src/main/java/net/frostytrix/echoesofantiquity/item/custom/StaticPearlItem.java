package net.frostytrix.echoesofantiquity.item.custom;

import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.component.ModDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class StaticPearlItem extends Item {
    public StaticPearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(pos).getBlock();
        BlockPos hasCoords = context.getItemInHand().get(ModDataComponentTypes.COORDINATES.get());

        if (!level.isClientSide()) {
            if (clickedBlock == ModBlocks.VOID_ANCHOR.get()) {
                if (hasCoords == null || (player != null && player.isCrouching())) {
                    level.playSound(null, pos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.BLOCKS, 1.0F, 1.0F);
                    context.getItemInHand().set(ModDataComponentTypes.COORDINATES.get(), pos);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }

            ServerLevel worldServer = (ServerLevel) level;

            if (hasCoords != null && player != null) {
                if (level.getBlockState(hasCoords).is(ModBlocks.VOID_ANCHOR.get()) && level.getBlockState(hasCoords.above()).isAir() && level.getBlockState(hasCoords.above(2)).isAir()) {

                    player.teleportTo(hasCoords.getX() + 0.5, hasCoords.above().getY(), hasCoords.getZ() + 0.5);

                    level.playSound(null, hasCoords.above(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
                    worldServer.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);

                    context.getItemInHand().hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player,
                            item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    player.getCooldowns().addCooldown(this, 20);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos hasCoords = stack.get(ModDataComponentTypes.COORDINATES.get());

        if (!level.isClientSide()) {
            ServerLevel worldServer = (ServerLevel) level;

            if (hasCoords != null) {
                if (level.getBlockState(hasCoords).is(ModBlocks.VOID_ANCHOR.get()) && level.getBlockState(hasCoords.above()).isAir() && level.getBlockState(hasCoords.above(2)).isAir()) {

                    player.teleportTo(hasCoords.getX() + 0.5, hasCoords.above().getY(), hasCoords.getZ() + 0.5);

                    level.playSound(null, hasCoords.above(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
                    worldServer.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 20, 0.5, 0.5, 0.5, 0.1);

                    stack.hurtAndBreak(1, worldServer, (ServerPlayer) player,
                            item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    player.getCooldowns().addCooldown(this, 20);
                    return InteractionResultHolder.success(stack);
                }
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.get(ModDataComponentTypes.COORDINATES.get()) != null || super.isFoil(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        BlockPos hasCoords = stack.get(ModDataComponentTypes.COORDINATES.get());
        if (!level.isClientSide() && hasCoords != null) {
            if (!level.getBlockState(hasCoords).is(ModBlocks.VOID_ANCHOR.get())) {
                stack.remove(ModDataComponentTypes.COORDINATES.get());
            }
        }
    }
}
