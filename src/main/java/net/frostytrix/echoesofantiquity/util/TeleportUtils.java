package net.frostytrix.echoesofantiquity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class TeleportUtils {

    /**
     * Scans the 8 blocks around the center position to find a safe 2-block high space.
     */
    // Vec3d is now Vec3, World is now Level
    public static Optional<Vec3> findSafeTeleportSpot(Level level, BlockPos centerPos) {
        // Look at the 8 adjacent blocks around the Waystone on the X/Z plane
        BlockPos[] searchOffsets = new BlockPos[]{
                centerPos.north(), centerPos.south(), centerPos.east(), centerPos.west(),
                centerPos.north().east(), centerPos.north().west(),
                centerPos.south().east(), centerPos.south().west()
        };

        for (BlockPos pos : searchOffsets) {
            // Check the current level, one block down, and one block up
            // pos.up() is now pos.above()
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                BlockPos checkPos = pos.above(yOffset);

                if (isSafeToStandAt(level, checkPos)) {
                    // Return the exact center of the block for teleporting (adding 0.5 to X and Z)
                    return Optional.of(new Vec3(checkPos.getX() + 0.5, checkPos.getY(), checkPos.getZ() + 0.5));
                }
            }
        }

        return Optional.empty(); // No safe spot found anywhere around it!
    }

    /**
     * A safe spot requires a solid block underneath, and no collision boxes at foot and head level.
     */
    private static boolean isSafeToStandAt(Level level, BlockPos pos) {
        // pos.down() is now pos.below()
        BlockState floor = level.getBlockState(pos.below());
        BlockState footLevel = level.getBlockState(pos);
        BlockState headLevel = level.getBlockState(pos.above());

        // isSideSolidFullSquare is now isFaceSturdy
        boolean isFloorSolid = floor.isFaceSturdy(level, pos.below(), Direction.UP);

        // Are the foot and head spaces passable? (Collision shape is empty = air, tall grass, signs, etc.)
        boolean isFootPassable = footLevel.getCollisionShape(level, pos).isEmpty();
        boolean isHeadPassable = headLevel.getCollisionShape(level, pos.above()).isEmpty();

        // blocksMovement is now blocksMotion
        boolean isNotHazard = !footLevel.blocksMotion() && footLevel.getFluidState().isEmpty();

        return isFloorSolid && isFootPassable && isHeadPassable && isNotHazard;
    }
}