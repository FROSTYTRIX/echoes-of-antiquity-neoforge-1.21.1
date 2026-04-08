package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.screen.custom.UncrafterScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UncrafterBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private ItemStack cachedInputType = ItemStack.EMPTY;
    private List<RecipeHolder<?>> cachedValidRecipes = null;
    private ItemStack nextOutput = ItemStack.EMPTY;

    private int progress = 0;
    private int maxProgress = 72;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> UncrafterBlockEntity.this.progress;
                case 1 -> UncrafterBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> UncrafterBlockEntity.this.progress = value;
                case 1 -> UncrafterBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public UncrafterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UNCRAFTER_BE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.echoesofantiquity.uncrafter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new UncrafterScreenHandler(id, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("Progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        progress = tag.getInt("Progress");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        if (hasRecipe(level)) {
            progress++;
            setChanged();

            if (progress >= maxProgress) {
                craftItem(level);
                progress = 0;
            }
        } else {
            progress = 0;
        }
    }

    private boolean hasRecipe(Level level) {
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty()) {
            cachedInputType = ItemStack.EMPTY;
            cachedValidRecipes = null;
            nextOutput = ItemStack.EMPTY;
            return false;
        }

        if (!ItemStack.isSameItem(input, cachedInputType) || cachedValidRecipes == null) {
            cachedInputType = input.copy();
            cachedValidRecipes = findAllValidRecipes(level, input);
            nextOutput = ItemStack.EMPTY;
        }

        if (cachedValidRecipes.isEmpty()) return false;

        if (nextOutput.isEmpty()) {
            nextOutput = generateRandomOutput(level);
        }

        if (nextOutput.isEmpty()) return false;

        ItemStack output = inventory.getStackInSlot(1);
        return output.isEmpty() || (ItemStack.isSameItem(output, nextOutput) && output.getCount() + nextOutput.getCount() <= output.getMaxStackSize());
    }

    private void craftItem(Level level) {
        inventory.getStackInSlot(0).shrink(1);
        inventory.insertItem(1, nextOutput.copy(), false);
        nextOutput = ItemStack.EMPTY;
    }

    private List<RecipeHolder<?>> findAllValidRecipes(Level level, ItemStack input) {
        RecipeManager rm = level.getRecipeManager();
        return rm.getRecipes().stream()
                .filter(holder -> {
                    ItemStack result = holder.value().getResultItem(level.registryAccess());
                    if (!result.is(input.getItem())) return false;

                    // Basic exploit prevention: can't uncraft if recipe yields more than ingredients
                    return result.getCount() <= holder.value().getIngredients().size();
                })
                .toList();
    }

    private ItemStack generateRandomOutput(Level level) {
        if (cachedValidRecipes == null || cachedValidRecipes.isEmpty()) return ItemStack.EMPTY;

        RecipeHolder<?> recipe = cachedValidRecipes.get(level.random.nextInt(cachedValidRecipes.size()));
        List<Ingredient> ingredients = recipe.value().getIngredients().stream().filter(i -> !i.isEmpty()).toList();

        if (ingredients.isEmpty()) return ItemStack.EMPTY;

        Ingredient randomIng = ingredients.get(level.random.nextInt(ingredients.size()));
        ItemStack[] items = randomIng.getItems();

        if (items.length > 0) {
            ItemStack out = items[level.random.nextInt(items.length)].copy();
            out.setCount(1);
            return out;
        }
        return ItemStack.EMPTY;
    }
}