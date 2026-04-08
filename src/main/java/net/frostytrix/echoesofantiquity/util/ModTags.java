package net.frostytrix.echoesofantiquity.util;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_END_STEEL_TOOL = createTag("needs_end_steel_tool");
        public static final TagKey<Block> INCORRECT_FOR_END_STEEL_TOOL = createTag("incorrect_for_end_steel_tool");
        public static final TagKey<Block> NATURAL_BLOCKS_LEVEL = createTag("natural_blocks_level");

        private static TagKey<Block> createTag(String name) {
            // RegistryKeys is now Registries, and Identifier is ResourceLocation
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, name));
        }

    }

    public static class Items {
        public static final TagKey<Item> GLASS_PANES = createTag("glass_panes");
        public static final TagKey<Item> ALL_STONES = createTag("all_stones");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, name));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> SOULLESS = createTag("soulless");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, name));
        }
    }
}