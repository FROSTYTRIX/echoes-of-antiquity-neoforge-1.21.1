package net.frostytrix.echoesofantiquity.block.custom;

import com.mojang.serialization.MapCodec;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.custom.GravityAnchorBlockEntity;
import net.frostytrix.echoesofantiquity.util.GravityAnchorManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class GravityAnchorBlock extends Block implements EntityBlock {
    public static final MapCodec<GravityAnchorBlock> CODEC = simpleCodec(GravityAnchorBlock::new);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final int RANGE = 10;

    public GravityAnchorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean isPowered = level.hasNeighborSignal(pos);

            if (state.getValue(ACTIVE) != isPowered) {
                level.setBlock(pos, state.setValue(ACTIVE, isPowered), Block.UPDATE_ALL);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide && !oldState.is(state.getBlock()) && state.getValue(ACTIVE)) {
            GravityAnchorManager.addAnchor(level, pos);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) {
            boolean wasActive = state.getValue(ACTIVE);
            if (!state.is(newState.getBlock()) || state.getValue(ACTIVE) != newState.getValue(ACTIVE)) {
                if (wasActive) {
                    GravityAnchorManager.removeAnchor(level, pos);
                    forceUpdateFallingBlocks(level, pos);
                } else if (newState.is(this) && newState.getValue(ACTIVE)) {
                    GravityAnchorManager.addAnchor(level, pos);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void forceUpdateFallingBlocks(Level level, BlockPos center) {
        BlockPos.betweenClosedStream(center.offset(-RANGE, -RANGE, -RANGE), center.offset(RANGE, RANGE, RANGE)).forEach(pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof FallingBlock) {
                level.scheduleTick(pos.immutable(), state.getBlock(), 2);
            }
        });
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GravityAnchorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Make sure to use .get() if ModBlockEntities.GRAVITY_ANCHOR_BE is a DeferredHolder/RegistryObject!
        return type == ModBlockEntities.GRAVITY_ANCHOR_BE.get() ?
                (level1, pos, state1, blockEntity) -> ((GravityAnchorBlockEntity) blockEntity).tick(level1, pos, state1) : null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE)) {
            for (int i = 0; i < 2; i++) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
                double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0, 0.02, 0);
            }

            if (random.nextInt(5) == 0) {
                double rx = pos.getX() + (random.nextDouble() - 0.5) * 20;
                double rz = pos.getZ() + (random.nextDouble() - 0.5) * 20;
                double ry = pos.getY() + (random.nextDouble() * 5);
                level.addParticle(ParticleTypes.WAX_OFF, rx, ry, rz, 0, 0.1, 0);
            }
        }
    }
}