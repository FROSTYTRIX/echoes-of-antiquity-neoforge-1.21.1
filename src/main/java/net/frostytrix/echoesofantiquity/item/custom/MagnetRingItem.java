package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MagnetRingItem extends Item {
    public static final int RANGE = 4;
    public static final float SPEED = 0.02f;

    public MagnetRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();

        nbt.putBoolean("attracting", !nbt.getBoolean("attracting")); // Toggle boolean safely

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof Player player) {
            AABB area = player.getBoundingBox().inflate(RANGE);
            List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, area);

            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag nbt = customData.copyTag();

            for (ItemEntity itemEntity : entities) {
                double distanceSq = itemEntity.distanceToSqr(player);

                if (distanceSq > 0.5 && nbt.getBoolean("attracting")) {
                    Vec3 direction = player.position().add(0, 0.75, 0).subtract(itemEntity.position()).normalize();

                    double pullStrength = 0.05 + (RANGE / (distanceSq + 1)) * SPEED;

                    Vec3 newVelocity = itemEntity.getDeltaMovement().scale(0.95).add(direction.scale(pullStrength));
                    itemEntity.setDeltaMovement(newVelocity);

                    itemEntity.hasImpulse = true;
                } else if (!nbt.getBoolean("attracting")) {
                    Vec3 direction = player.position().add(0, 0.75, 0).subtract(itemEntity.position()).normalize();

                    double pullStrength = -0.05 - (RANGE / (distanceSq + 1)) * SPEED;

                    Vec3 newVelocity = itemEntity.getDeltaMovement().scale(0.95).add(direction.scale(pullStrength));
                    itemEntity.setDeltaMovement(newVelocity);

                    itemEntity.hasImpulse = true;
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();

        boolean attracting = nbt.getBoolean("attracting");

        if (attracting) {
            tooltip.add(Component.translatable("tooltip.echoesofantiquity.magnetic_ring").append(Component.translatable("tooltip.echoesofantiquity.magnetic_ring.attracting")));
        } else {
            tooltip.add(Component.translatable("tooltip.echoesofantiquity.magnetic_ring").append(Component.translatable("tooltip.echoesofantiquity.magnetic_ring.repulsing")));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}