package net.frostytrix.echoesofantiquity.screen;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.screen.custom.SieveScreenHandler;
import net.frostytrix.echoesofantiquity.screen.custom.UncrafterScreenHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModScreenHandlers {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, EchoesOfAntiquity.MOD_ID);

    // We use IMenuTypeExtension to allow passing BlockPos through the network
    public static final DeferredHolder<MenuType<?>, MenuType<UncrafterScreenHandler>> UNCRAFTER_SCREEN_HANDLER =
            MENUS.register("uncrafter_screen_handler", () ->
                    IMenuTypeExtension.create(UncrafterScreenHandler::new));

    public static final DeferredHolder<MenuType<?>, MenuType<SieveScreenHandler>> SIEVE_SCREEN_HANDLER =
            MENUS.register("sieve_screen_handler", () ->
                    IMenuTypeExtension.create(SieveScreenHandler::new));
    // Placeholder for Sieve if needed later
    // public static final DeferredHolder<MenuType<?>, MenuType<SieveScreenHandler>> SIEVE_SCREEN_HANDLER = ...

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}