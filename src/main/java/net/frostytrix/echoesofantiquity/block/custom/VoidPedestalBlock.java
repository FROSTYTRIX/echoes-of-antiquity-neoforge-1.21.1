package net.frostytrix.echoesofantiquity.block.custom;

import com.mojang.serialization.MapCodec;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class VoidPedestalBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BlockStateProperties.TRIGGERED; // Reusing triggered as 'active'

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(3, 2, 3, 13, 13, 13),
            Block.box(1, 13, 1, 15, 16.25, 15),
            Block.box(1, 16.25, 1, 4, 17, 15),
            Block.box(4, 14.7, 3.5, 12, 16.7, 5.5),
            Block.box(12, 16.25, 1, 15, 17, 15),
            Block.box(4, 16.25, 1, 12, 17, 4),
            Block.box(4, 16.25, 12, 12, 17, 15),
            Block.box(3.5, 14.7, 3.5, 5.5, 16.7, 12.5),
            Block.box(10.5, 14.7, 3.5, 12.5, 16.7, 12.5),
            Block.box(4, 14.7, 10.45, 12, 16.7, 12.45)
    );

    public static final MapCodec<VoidPedestalBlock> CODEC = simpleCodec(VoidPedestalBlock::new);

    public VoidPedestalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VoidPedestalBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof VoidPedestalBlockEntity pedestal) {
                // Containers.dropContents is the NeoForge equivalent of ItemScatterer
                for (int i = 0; i < pedestal.getInventory().getSlots(); i++) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), pedestal.getInventory().getStackInSlot(i));
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof VoidPedestalBlockEntity pedestal) {
            ItemStack stackOnPedestal = pedestal.getInventory().getStackInSlot(0);

            // Case 1: Pedestal is empty, player holding item
            if (stackOnPedestal.isEmpty() && !stack.isEmpty()) {
                pedestal.getInventory().setStackInSlot(0, stack.copyWithCount(1));
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                pedestal.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
                return ItemInteractionResult.SUCCESS;
            }
            // Case 2: Pedestal has item, player hand is empty (or not sneaking)
            else if (!stackOnPedestal.isEmpty() && !player.isShiftKeyDown()) {
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, stackOnPedestal.copy());
                    level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                    pedestal.getInventory().setStackInSlot(0, ItemStack.EMPTY);
                    pedestal.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.VOID_PEDESTAL_BE.get(), VoidPedestalBlockEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}