package net.frostytrix.echoesofantiquity.recipe.sieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record SieveResult(ItemStack stack, float chance) {

    // Defines how to read/write this from the Recipe JSON
    public static final Codec<SieveResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(SieveResult::stack),
            Codec.FLOAT.fieldOf("chance").forGetter(SieveResult::chance)
    ).apply(inst, SieveResult::new));

    // Defines how to sync this from the server to the client
    public static final StreamCodec<RegistryFriendlyByteBuf, SieveResult> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, SieveResult::stack,
            ByteBufCodecs.FLOAT, SieveResult::chance,
            SieveResult::new
    );
}