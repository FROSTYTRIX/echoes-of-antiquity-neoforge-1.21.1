package net.frostytrix.echoesofantiquity.sound;

import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class GravityAnchorSoundInstance extends AbstractTickableSoundInstance {
    private final Level level;
    private final BlockPos pos;

    public GravityAnchorSoundInstance(SoundEvent sound, Level level, BlockPos pos) {
        // SoundInstance.createRandom() maps to RandomSource.create() in NeoForge
        super(sound, SoundSource.BLOCKS, RandomSource.create());
        this.level = level;
        this.pos = pos;

        // Pin the sound to the exact center of the block
        this.x = (double) pos.getX() + 0.5;
        this.y = (double) pos.getY() + 0.5;
        this.z = (double) pos.getZ() + 0.5;

        // This makes the hum loop infinitely
        this.looping = true;
        this.delay = 0;
        this.volume = 1.4f;
    }

    @Override
    public void tick() {
        // If the block is broken OR turned off, kill the audio loop
        // Note: Assumes ModBlocks.GRAVITY_ANCHOR is a DeferredHolder, hence the .get()
        if (!level.getBlockState(pos).is(ModBlocks.GRAVITY_ANCHOR.get()) || !level.getBlockState(pos).getValue(GravityAnchorBlock.ACTIVE)) {
            this.stop();
        }
    }
}