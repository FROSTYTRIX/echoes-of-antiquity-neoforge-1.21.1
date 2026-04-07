package net.frostytrix.echoesofantiquity.item;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquityMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EchoesOfAntiquityMod.MOD_ID);
    
    
    // THE FALLEN HUMANS

    public static final DeferredItem<Item> ANCIENT_SCRIP = ITEMS.register("ancient_scrip",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> END_STEEL_INGOT = ITEMS.register("end_steel_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VOID_TREATED_LEATHER = ITEMS.register("void_treated_leather", () -> new Item(new Item.Properties()));


    // THE ARCHITECTS/BUILDERS

    public static final DeferredItem<Item> CLIMBING_SPIDER_LEG = ITEMS.register("climbing_spider_leg",() -> new Item(new Item.Properties().stacksTo(1)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
