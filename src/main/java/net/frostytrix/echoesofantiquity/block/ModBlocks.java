package net.frostytrix.echoesofantiquity.block;


import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.custom.*;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EchoesOfAntiquity.MOD_ID);

    public static final DeferredBlock<Block> VOID_PEDESTAL = registerBlock("void_pedestal",
            () -> new VoidPedestalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .noOcclusion().strength(50.0F, 1200.0F).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> VOID_ANCHOR = registerBlock("void_anchor",
            () -> new VoidAnchorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)
                    .requiresCorrectToolForDrops().strength(3.0F, 9.0F)));

    public static final DeferredBlock<Block> UNCRAFTER = registerBlock("uncrafter",
            () -> new UncrafterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)
                    .strength(2.5F).sound(SoundType.WOOD)));

//    public static final DeferredBlock<Block> WAYSTONE = registerBlock("waystone",
//            () -> new WaystoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
//                    .noOcclusion().requiresCorrectToolForDrops().strength(1.5F, 6.0F).sound(SoundType.STONE)
//                    .lightLevel(state -> state.getValue(WaystoneBlock.ACTIVE) ? 9 : 0)));

    public static final DeferredBlock<Block> GRAVITY_ANCHOR = registerBlock("gravity_anchor",
            () -> new GravityAnchorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noOcclusion().strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(GravityAnchorBlock.ACTIVE) ? 6 : 0)));

    public static final DeferredBlock<Block> SIEVE = registerBlock("sieve",
            () -> new SieveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .noOcclusion().sound(SoundType.WOOD).requiresCorrectToolForDrops()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
