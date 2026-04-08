package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.sound.GravityAnchorSoundInstance;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GravityAnchorBlockEntity extends BlockEntity {
    private boolean isPlayingSound = false;

    public GravityAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRAVITY_ANCHOR_BE.get(), pos, state);
    }

    public boolean isActive() {
        BlockState state = this.getBlockState();
        if (state.hasProperty(GravityAnchorBlock.ACTIVE)) {
            return state.getValue(GravityAnchorBlock.ACTIVE);
        }
        return false;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide && state.getValue(GravityAnchorBlock.ACTIVE)) {
            playAmbientSound(level, pos);
        } else if (level.isClientSide) {
            isPlayingSound = false;
        }
    }

    private void playAmbientSound(Level level, BlockPos pos) {
        if (!isPlayingSound) {
            // Ensure ModSounds.GRAVITY_ANCHOR_ACTIVE uses .get() if it's a DeferredHolder
            Minecraft.getInstance().getSoundManager().play(
                    new GravityAnchorSoundInstance(ModSounds.GRAVITY_ANCHOR_ACTIVE.get(), level, pos)
            );
            isPlayingSound = true;
        }
    }
}