package net.frostytrix.echoesofantiquity.potion;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.effect.ModEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    // 1. Create the DeferredRegister for Potions
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, EchoesOfAntiquity.MOD_ID);

    // 2. Register the potions! Note we use DeferredHolder instead of RegistryEntry
    public static final DeferredHolder<Potion, Potion> PHASING_POTION = POTIONS.register("phasing_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.PHASING, 600, 0)));

    public static final DeferredHolder<Potion, Potion> REACH_POTION = POTIONS.register("reach_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.REACH, 900, 0)));
}