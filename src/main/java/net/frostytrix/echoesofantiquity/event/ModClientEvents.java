package net.frostytrix.echoesofantiquity.event;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.block.entity.renderer.SieveBERenderer;
import net.frostytrix.echoesofantiquity.block.entity.renderer.VoidPedestalBERenderer;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.screen.ModScreenHandlers;
import net.frostytrix.echoesofantiquity.screen.custom.UncrafterScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = EchoesOfAntiquity.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getUseItem();

        if (player.isUsingItem() && itemStack.is(ModItems.DRAGON_BOW.get())) {
            int i = player.getTicksUsingItem();
            float g = (float)i / 20.0f;
            g = g > 1.0f ? 1.0f : g * g;

            // This replaces the complex math in your Mixin
            event.setNewFovModifier(event.getFovModifier() * (1.0f - g * 0.15f));
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 1. Register Block Entity Renderers (The Pedestal)
        // This links the BlockEntityType to its custom Renderer class
        event.registerBlockEntityRenderer(ModBlockEntities.VOID_PEDESTAL_BE.get(), VoidPedestalBERenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SIEVE_BE.get(), SieveBERenderer::new);

        // 2. Register Entity Renderers (The Chorus Husk)
        // This links the EntityType to its custom Renderer class
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModScreenHandlers.UNCRAFTER_SCREEN_HANDLER.get(), UncrafterScreen::new);
    }
}