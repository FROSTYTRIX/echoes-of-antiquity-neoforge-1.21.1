package net.frostytrix.echoesofantiquity.item;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EchoesOfAntiquity.MOD_ID);

    public static final Supplier<CreativeModeTab> THE_FALLEN_HUMANS = CREATIVE_MODE_TAB.register("the_fallen_humans",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.VOID_ANCHOR.get())).title(Component.translatable("itemgroup.echoesofantiquity.the_fallen_humans"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ANCIENT_SCRIP.get());
                        output.accept(ModItems.VOID_TREATED_LEATHER.get());
                        output.accept(ModItems.END_STEEL_INGOT.get());
                        output.accept(ModItems.DRAGON_BOW.get());
                        output.accept(ModItems.STATIC_PEARL.get());
                        output.accept(ModBlocks.VOID_ANCHOR.get());
                        output.accept(ModBlocks.VOID_PEDESTAL.get());
                        output.accept(ModItems.OBSIDIAN_GOGGLES.get());
                        output.accept(ModItems.ENDER_BOOTS.get());
                        output.accept(ModItems.VOID_CHAINMAIL_HELMET.get());
                        output.accept(ModItems.VOID_CHAINMAIL_CHESTPLATE.get());
                        output.accept(ModItems.VOID_CHAINMAIL_LEGGINGS.get());
                        output.accept(ModItems.VOID_CHAINMAIL_BOOTS.get());
                    }).build());

    public static final Supplier<CreativeModeTab> THE_ARCHITECTS_TOOLS = CREATIVE_MODE_TAB.register("the_architects_tools",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MEASURING_TAPE.get())).title(Component.translatable("itemgroup.echoesofantiquity.the_architects_tools"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, "the_fallen_humans"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CLIMBING_SPIDER_LEG.get());
                        output.accept(ModItems.INFINITE_WATER_BUCKET.get());
                        output.accept(ModItems.MEASURING_TAPE.get());
                        output.accept(ModItems.MAGNET_RING.get());
                        output.accept(ModItems.LEVEL.get());
                        output.accept(ModBlocks.GRAVITY_ANCHOR.get());
                        output.accept(ModBlocks.SIEVE.get());
                    }).build());

    public static final Supplier<CreativeModeTab> THE_FAILED_CLONES = CREATIVE_MODE_TAB.register("the_failed_clones",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SOUL_FRAGMENT.get())).title(Component.translatable("itemgroup.echoesofantiquity.the_failed_clones"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, "the_architects_tools"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.SOUL_FRAGMENT.get());
                        output.accept(ModItems.SOUL_SIPHON.get());
                    }).build());



    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
