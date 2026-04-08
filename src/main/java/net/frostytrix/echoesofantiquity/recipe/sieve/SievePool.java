package net.frostytrix.echoesofantiquity.recipe.sieve;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SievePool(float chance, List<ItemStack> items) {

    public static final Codec<SievePool> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.fieldOf("chance").forGetter(SievePool::chance),
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(SievePool::items)
    ).apply(inst, SievePool::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SievePool> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SievePool::chance,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), SievePool::items,
            SievePool::new
    );
}