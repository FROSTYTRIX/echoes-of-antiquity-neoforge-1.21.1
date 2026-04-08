package net.frostytrix.echoesofantiquity.sound;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, EchoesOfAntiquity.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GRAVITY_ANCHOR_ACTIVE = registerSoundEvent("gravity_anchor_active");
    public static final DeferredHolder<SoundEvent, SoundEvent> SIFTING = registerSoundEvent("sifting");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}