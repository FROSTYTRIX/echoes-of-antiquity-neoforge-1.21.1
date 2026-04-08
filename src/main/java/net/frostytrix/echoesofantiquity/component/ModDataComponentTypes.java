package net.frostytrix.echoesofantiquity.component;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponentTypes {
    // 1. Create a DeferredRegister for Data Component Types
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, EchoesOfAntiquity.MOD_ID);

    // 2. Register your custom 'coordinates' component (Replacing the Fabric Registry.register method)
    public static final Supplier<DataComponentType<BlockPos>> COORDINATES =
            DATA_COMPONENT_TYPES.register("coordinates", () -> DataComponentType.<BlockPos>builder()
                    .persistent(BlockPos.CODEC)
                    .networkSynchronized(BlockPos.STREAM_CODEC)
                    .build());
}
