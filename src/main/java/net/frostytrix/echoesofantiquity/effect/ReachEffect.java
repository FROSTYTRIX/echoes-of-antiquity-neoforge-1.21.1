package net.frostytrix.echoesofantiquity.effect;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ReachEffect extends MobEffect {

    public ReachEffect(MobEffectCategory category, int color) {
        super(category, color);

        // In 1.21 Mojang Mappings, we do not need to manually handle onApplied/onRemoved!
        // The MobEffect class natively supports binding attributes directly in the constructor:
        this.addAttributeModifier(
                Attributes.ENTITY_INTERACTION_RANGE,
                ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, "reach_extension"),
                2.0,
                AttributeModifier.Operation.ADD_VALUE
        );

        this.addAttributeModifier(
                Attributes.BLOCK_INTERACTION_RANGE,
                ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, "reach_block_extension"),
                2.0,
                AttributeModifier.Operation.ADD_VALUE
        );
    }
}