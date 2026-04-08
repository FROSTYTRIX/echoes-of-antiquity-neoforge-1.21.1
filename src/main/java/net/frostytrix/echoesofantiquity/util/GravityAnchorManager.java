package net.frostytrix.echoesofantiquity.util;

import net.frostytrix.echoesofantiquity.block.custom.GravityAnchorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GravityAnchorManager {
    // Stocke les positions des ancres actives par dimension (Overworld, Nether, etc.)
    // Yarn's RegistryKey<World> is Mojang's ResourceKey<Level>
    private static final Map<ResourceKey<Level>, Set<BlockPos>> ANCHORS = new HashMap<>();

    // Appelé quand le bloc est posé ou allumé
    public static void addAnchor(Level level, BlockPos pos) {
        // level.dimension() is the equivalent of world.getRegistryKey()
        ANCHORS.computeIfAbsent(level.dimension(), k -> new HashSet<>()).add(pos);
    }

    // Appelé quand le bloc est cassé ou éteint
    public static void removeAnchor(Level level, BlockPos pos) {
        Set<BlockPos> set = ANCHORS.get(level.dimension());
        if (set != null) {
            set.remove(pos);
        }
    }

    // Vérifie si un bloc est dans le rayon d'une ancre
    public static boolean isProtected(Level level, BlockPos targetPos) {
        Set<BlockPos> set = ANCHORS.get(level.dimension());
        if (set == null || set.isEmpty()) return false;

        for (BlockPos anchorPos : set) {
            // closerThan in Mojang mappings is the equivalent of isWithinDistance in Yarn
            if (anchorPos.closerThan(targetPos, GravityAnchorBlock.RANGE)) {
                return true;
            }
        }
        return false;
    }
}