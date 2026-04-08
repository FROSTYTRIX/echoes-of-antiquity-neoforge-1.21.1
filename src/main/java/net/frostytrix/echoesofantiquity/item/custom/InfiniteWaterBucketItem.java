package net.frostytrix.echoesofantiquity.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class InfiniteWaterBucketItem extends BucketItem {

    public InfiniteWaterBucketItem(Properties properties) {
        super(Fluids.WATER, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Keep a copy of the stack so we can return it and avoid consuming the bucket
        ItemStack stackToKeep = player.getItemInHand(hand).copy();

        if (player.isCrouching()) {
            // 1. Shoot a raycast looking ONLY for fluids
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState state = level.getBlockState(pos);

                // 2. Check if the block is a fluid that can be drained
                // Fabric's "FluidDrainable" is "BucketPickup" in Mojang mappings
                if (state.getBlock() instanceof BucketPickup drainable) {

                    // 3. Drain it!
                    ItemStack drained = drainable.pickupBlock(player, level, pos, state);

                    if (!drained.isEmpty()) {
                        // Play the slurp sound manually
                        level.playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                        return InteractionResultHolder.success(stackToKeep);
                    }
                }
            }
            return InteractionResultHolder.pass(stackToKeep);
        }

        // Normal behavior (Not sneaking) - place water using the vanilla BucketItem logic
        InteractionResultHolder<ItemStack> placeResult = super.use(level, player, hand);

        // If the vanilla logic successfully placed the water, return our copy instead of the empty bucket
        if (placeResult.getResult().consumesAction()) {
            return InteractionResultHolder.success(stackToKeep);
        }

        return placeResult;
    }
}