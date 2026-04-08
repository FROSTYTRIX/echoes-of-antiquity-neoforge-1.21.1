package net.frostytrix.echoesofantiquity.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class PhasingEffect extends MobEffect {
    public PhasingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        Direction dir = entity.getDirection(); // Equivalent to getHorizontalFacing()

        // 1. Manually check for a wall by expanding the player's collision check area slightly where they are looking
        AABB collisionCheckArea = entity.getBoundingBox().expandTowards(
                dir.getStepX() * 0.2,
                0,
                dir.getStepZ() * 0.2
        );

        boolean hasWall = !level.getEntities(entity, collisionCheckArea).isEmpty() || !level.noCollision(entity, collisionCheckArea);

        if (hasWall) {
            BlockPos targetPos = null;

            // 2. Scan forward (up to 5 blocks) to find an empty space to safely phase into
            for (int i = 1; i <= 5; i++) {
                BlockPos checkPos = entity.blockPosition().relative(dir, i);

                // Ensure both the foot level and head level are clear of collision
                if (level.getBlockState(checkPos).getCollisionShape(level, checkPos).isEmpty() &&
                        level.getBlockState(checkPos.above()).getCollisionShape(level, checkPos.above()).isEmpty()) {
                    targetPos = checkPos;
                    break;
                }
            }

            if (targetPos != null) {
                if (level instanceof ServerLevel serverLevel) {
                    // Visuals
                    serverLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);

                    // Teleport
                    entity.teleportTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);

                    // Sound
                    level.playSound(null, targetPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);

                    // Visuals n.2
                    serverLevel.sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY() + 1, entity.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}