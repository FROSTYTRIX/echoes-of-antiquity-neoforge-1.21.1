package net.frostytrix.echoesofantiquity.effect;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    // 1. Create the DeferredRegister for MobEffects (StatusEffects)
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, EchoesOfAntiquity.MOD_ID);

    // 2. Register each effect
    public static final DeferredHolder<MobEffect, MobEffect> PHASING = MOB_EFFECTS.register("phasing",
            () -> new PhasingEffect(MobEffectCategory.NEUTRAL, 0x93248e));

    public static final DeferredHolder<MobEffect, MobEffect> REACH = MOB_EFFECTS.register("reach",
            () -> new ReachEffect(MobEffectCategory.BENEFICIAL, 0x41e8c3));

    public static void registerEffects(IEventBus eventBus) {
        EchoesOfAntiquity.LOGGER.info("[EchoesOfAntiquity] Loading effects");
        MOB_EFFECTS.register(eventBus);
    }
}