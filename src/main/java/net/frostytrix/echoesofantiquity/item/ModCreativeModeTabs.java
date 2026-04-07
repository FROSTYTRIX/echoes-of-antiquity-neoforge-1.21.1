package net.frostytrix.echoesofantiquity.item;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquityMod;
import net.frostytrix.echoesofantiquity.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EchoesOfAntiquityMod.MOD_ID);

    public static final Supplier<CreativeModeTab> THE_FALLEN_HUMANS = CREATIVE_MODE_TAB.register("the_fallen_humans",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.ENDER_EYE)).title(Component.translatable("itemgroup.echoesofantiquity.the_fallen_humans"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ANCIENT_SCRIP.get());
                        output.accept(ModItems.VOID_TREATED_LEATHER.get());
                        output.accept(ModItems.END_STEEL_INGOT.get());
                        output.accept(ModBlocks.VOID_ANCHOR.get());
                    }).build());

    public static final Supplier<CreativeModeTab> THE_ARCHITECTS_TOOLS = CREATIVE_MODE_TAB.register("the_architects_tools",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CLIMBING_SPIDER_LEG.get())).title(Component.translatable("itemgroup.echoesofantiquity.the_architects_tools"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquityMod.MOD_ID, "the_fallen_humans"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CLIMBING_SPIDER_LEG.get());
                    }).build());

    public static final Supplier<CreativeModeTab> THE_FAILED_CLONES = CREATIVE_MODE_TAB.register("the_failed_clones",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.LAPIS_LAZULI)).title(Component.translatable("itemgroup.echoesofantiquity.the_failed_clones"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquityMod.MOD_ID, "the_architects_tools"))
                    .displayItems((itemDisplayParameters, output) -> {
                    }).build());



    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
