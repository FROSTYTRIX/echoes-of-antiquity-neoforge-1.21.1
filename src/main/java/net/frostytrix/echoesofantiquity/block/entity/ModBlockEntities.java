package net.frostytrix.echoesofantiquity.block.entity;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.frostytrix.echoesofantiquity.block.entity.custom.GravityAnchorBlockEntity;
import net.frostytrix.echoesofantiquity.block.entity.custom.SieveBlockEntity;
import net.frostytrix.echoesofantiquity.block.entity.custom.UncrafterBlockEntity;
import net.frostytrix.echoesofantiquity.block.entity.custom.VoidPedestalBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EchoesOfAntiquity.MOD_ID);

    public static final Supplier<BlockEntityType<VoidPedestalBlockEntity>> VOID_PEDESTAL_BE =
            BLOCK_ENTITIES.register("void_pedestal_be", () ->
                    BlockEntityType.Builder.of(VoidPedestalBlockEntity::new, ModBlocks.VOID_PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<UncrafterBlockEntity>> UNCRAFTER_BE =
            BLOCK_ENTITIES.register("uncrafter_be", () ->
                    BlockEntityType.Builder.of(UncrafterBlockEntity::new, ModBlocks.UNCRAFTER.get()).build(null));

    //public static final Supplier<BlockEntityType<WaystoneBlockEntity>> WAYSTONE_BE =
     //       BLOCK_ENTITIES.register("waystone_be", () ->
     //               BlockEntityType.Builder.of(WaystoneBlockEntity::new, ModBlocks.WAYSTONE.get()).build(null));

    public static final Supplier<BlockEntityType<GravityAnchorBlockEntity>> GRAVITY_ANCHOR_BE =
            BLOCK_ENTITIES.register("gravity_anchor_be", () ->
                    BlockEntityType.Builder.of(GravityAnchorBlockEntity::new, ModBlocks.GRAVITY_ANCHOR.get()).build(null));

    public static final Supplier<BlockEntityType<SieveBlockEntity>> SIEVE_BE =
            BLOCK_ENTITIES.register("sieve_be", () ->
                    BlockEntityType.Builder.of(SieveBlockEntity::new, ModBlocks.SIEVE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}