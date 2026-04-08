package net.frostytrix.echoesofantiquity.recipe.sieve;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.frostytrix.echoesofantiquity.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record SieveRecipe(Ingredient inputItem, List<SieveResult> results, List<SievePool> pools) implements Recipe<SieveRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(SieveRecipeInput input, Level level) {
        return this.inputItem.test(input.getItem(0));
    }

    // This is called by the standard crafting system, but since we have a custom
    // probability system handled in the BlockEntity, we just return an empty stack here.
    @Override
    public ItemStack assemble(SieveRecipeInput input, HolderLookup.Provider lookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider lookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SIEVE_SERIALIZER.get(); // Linked to your registry
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SIEVE_TYPE.get(); // Linked to your registry
    }

    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        public static final MapCodec<SieveRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SieveRecipe::inputItem),
                SieveResult.CODEC.listOf().fieldOf("results").forGetter(SieveRecipe::results),
                // We make pools optional so old recipes don't break!
                SievePool.CODEC.listOf().optionalFieldOf("pools", List.of()).forGetter(SieveRecipe::pools)
        ).apply(inst, SieveRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SieveRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, SieveRecipe::inputItem,
                        SieveResult.STREAM_CODEC.apply(ByteBufCodecs.list()), SieveRecipe::results,
                        SievePool.STREAM_CODEC.apply(ByteBufCodecs.list()), SieveRecipe::pools,
                        SieveRecipe::new);

        @Override
        public MapCodec<SieveRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SieveRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}