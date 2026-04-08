package net.frostytrix.echoesofantiquity.item;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.frostytrix.echoesofantiquity.item.custom.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EchoesOfAntiquity.MOD_ID);
    
    
    // THE FALLEN HUMANS

    public static final DeferredItem<Item> ANCIENT_SCRIP = ITEMS.register("ancient_scrip",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> END_STEEL_INGOT = ITEMS.register("end_steel_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VOID_TREATED_LEATHER = ITEMS.register("void_treated_leather", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STATIC_PEARL = ITEMS.register("static_pearl", () -> new StaticPearlItem(new Item.Properties().stacksTo(1).durability(20)));
    public static final DeferredItem<Item> DRAGON_BOW = ITEMS.register("dragon_bow", () -> new DragonBowItem(new Item.Properties().stacksTo(1).durability(3000)));

    public static final DeferredItem<ArmorItem> ENDER_BOOTS = ITEMS.register("ender_boots",
            () -> new ArmorItem(ModArmorMaterials.ENDER, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(7))));

    public static final DeferredItem<ArmorItem> OBSIDIAN_GOGGLES = ITEMS.register("obsidian_goggles",
            () -> new ArmorItem(ModArmorMaterials.ENDER, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(7))));

    // Note: I swapped 'ArmorItem' to 'ModArmorItem' here so your set bonus logic from Canvas actually runs!
    public static final DeferredItem<ModArmorItem> VOID_CHAINMAIL_HELMET = ITEMS.register("void_chainmail_helmet",
            () -> new ModArmorItem(ModArmorMaterials.VOID_CHAINMAIL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(21))));

    public static final DeferredItem<VoidChainmailChestplateItem> VOID_CHAINMAIL_CHESTPLATE = ITEMS.register("void_chainmail_chestplate",
            () -> new VoidChainmailChestplateItem(ModArmorMaterials.VOID_CHAINMAIL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(21))));

    public static final DeferredItem<ModArmorItem> VOID_CHAINMAIL_LEGGINGS = ITEMS.register("void_chainmail_leggings",
            () -> new ModArmorItem(ModArmorMaterials.VOID_CHAINMAIL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(21))));

    public static final DeferredItem<ModArmorItem> VOID_CHAINMAIL_BOOTS = ITEMS.register("void_chainmail_boots",
            () -> new ModArmorItem(ModArmorMaterials.VOID_CHAINMAIL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(21))));

    // THE ARCHITECTS/BUILDERS

    public static final DeferredItem<Item> CLIMBING_SPIDER_LEG = ITEMS.register("climbing_spider_leg",() -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> INFINITE_WATER_BUCKET = ITEMS.register("infinite_water_bucket",() -> new InfiniteWaterBucketItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MEASURING_TAPE = ITEMS.register("measuring_tape",() -> new MeasuringTapeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> LEVEL = ITEMS.register("level",() -> new ArchitectsLevelItem(new Item.Properties().stacksTo(1).durability(2000)));
    public static final DeferredItem<Item> MAGNET_RING = ITEMS.register("magnet_ring",() -> new MagnetRingItem(new Item.Properties().stacksTo(1)));

    // THE FAILED CLONES

    public static final DeferredItem<Item> SOUL_FRAGMENT = ITEMS.register("soul_fragment", () -> new Item(new Item.Properties()));
    public static final DeferredItem<SoulSiphonItem> SOUL_SIPHON = ITEMS.register("soul_siphon",
            () -> new SoulSiphonItem(Tiers.IRON, new Item.Properties().attributes(
                    SwordItem.createAttributes(Tiers.IRON, 2, -1.4f))));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
