package net.frostytrix.echoesofantiquity.block.custom;

import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class VoidAnchorBlock extends Block {
    public static final BooleanProperty PURPLE = BooleanProperty.create("purple");

    public VoidAnchorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PURPLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PURPLE);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        // Use Magenta Dye to turn it purple
        if (stack.is(Items.MAGENTA_DYE) && !state.getValue(PURPLE)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(PURPLE, true), 3);
                level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        // Use Water Bucket to clean it
        if (stack.is(Items.WATER_BUCKET) && state.getValue(PURPLE)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(PURPLE, false), 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        // Use Infinite Water Bucket (Custom Item)
        if (stack.is(ModItems.INFINITE_WATER_BUCKET.get()) && state.getValue(PURPLE)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(PURPLE, false), 3);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}