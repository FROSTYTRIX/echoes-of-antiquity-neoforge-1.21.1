package net.frostytrix.echoesofantiquity.recipe;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, EchoesOfAntiquity.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, EchoesOfAntiquity.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SieveRecipe>> SIEVE_SERIALIZER =
            SERIALIZERS.register("sieve", SieveRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<SieveRecipe>> SIEVE_TYPE =
            TYPES.register("sieve", () -> new RecipeType<SieveRecipe>() {
                @Override
                public String toString() {
                    return "sieve";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}