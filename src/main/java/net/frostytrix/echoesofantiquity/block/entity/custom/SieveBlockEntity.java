package net.frostytrix.echoesofantiquity.block.entity.custom;

import net.frostytrix.echoesofantiquity.block.entity.ModBlockEntities;
import net.frostytrix.echoesofantiquity.item.ModItems;
import net.frostytrix.echoesofantiquity.recipe.ModRecipes;
import net.frostytrix.echoesofantiquity.recipe.sieve.SievePool;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipe;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveRecipeInput;
import net.frostytrix.echoesofantiquity.recipe.sieve.SieveResult;
import net.frostytrix.echoesofantiquity.screen.custom.SieveScreenHandler;
import net.frostytrix.echoesofantiquity.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SieveBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private static final int INPUT_SLOT = 0;
    private static final int SOUL_FRAGMENT_SLOT = 1;

    private int progress = 0;
    private int maxProgress = 72;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> SieveBlockEntity.this.progress;
                case 1 -> SieveBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> SieveBlockEntity.this.progress = value;
                case 1 -> SieveBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private Item cachedRenderItem = null;
    private boolean cachedRecipeResult = false;

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SIEVE_BE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public boolean hasValidRecipeForRender() {
        if (this.level == null) return false;
        ItemStack currentStack = this.inventory.getStackInSlot(INPUT_SLOT);

        if (currentStack.isEmpty()) {
            this.cachedRenderItem = null;
            return false;
        }

        if (currentStack.getItem() != this.cachedRenderItem) {
            this.cachedRenderItem = currentStack.getItem();
            this.cachedRecipeResult = this.level.getRecipeManager()
                    .getRecipeFor(ModRecipes.SIEVE_TYPE.get(), new SieveRecipeInput(currentStack), this.level)
                    .isPresent();
        }

        return this.cachedRecipeResult;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.echoesofantiquity.sieve");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new SieveScreenHandler(id, playerInventory, this, this.data);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        // Safely wipe the client memory before reading so the 3D block doesn't get stuck
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }

        this.progress = tag.getInt("sieve.progress");
        this.maxProgress = tag.getInt("sieve.maxProgress");
        if (tag.contains("Inventory")) {
            this.inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", this.inventory.serializeNBT(registries));
        tag.putInt("sieve.progress", this.progress);
        tag.putInt("sieve.maxProgress", this.maxProgress);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            if (this.progress > 0 && this.progress < this.maxProgress) {
                this.progress++;
            }
            return;
        }

        if (hasRecipe() && hasOutputSpace() && hasFuel()) {
            this.progress++;
            setChanged();

            if (this.progress == 1) {
                level.playSound(null, pos, ModSounds.SIFTING.get(), SoundSource.BLOCKS, 1.0f, 0.8f);
                level.sendBlockUpdated(pos, state, state, 3);
            }

            if (this.progress >= this.maxProgress) {
                craftItem(level);
                this.progress = 0;
                level.sendBlockUpdated(pos, state, state, 3);
            }
        } else {
            if (this.progress > 0) {
                this.progress = 0;
                level.sendBlockUpdated(pos, state, state, 3);
            }
            setChanged();
        }
    }

    private boolean hasFuel() {
        return this.inventory.getStackInSlot(SOUL_FRAGMENT_SLOT).is(ModItems.SOUL_FRAGMENT.get());
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<SieveRecipe>> recipe = getCurrentRecipe();
        return recipe.isPresent() && hasOutputSpace();
    }

    private boolean hasOutputSpace() {
        for (int i = 2; i <= 7; i++) {
            ItemStack slotStack = this.inventory.getStackInSlot(i);
            if (slotStack.isEmpty() || slotStack.getCount() < slotStack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private Optional<RecipeHolder<SieveRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.SIEVE_TYPE.get(), new SieveRecipeInput(inventory.getStackInSlot(INPUT_SLOT)), this.level);
    }

    private void craftItem(Level level) {
        Optional<RecipeHolder<SieveRecipe>> recipeEntry = getCurrentRecipe();
        if (recipeEntry.isEmpty()) return;

        SieveRecipe recipe = recipeEntry.get().value();

        this.inventory.getStackInSlot(SOUL_FRAGMENT_SLOT).shrink(1);
        this.inventory.getStackInSlot(INPUT_SLOT).shrink(1);

        for (SieveResult result : recipe.results()) {
            if (level.random.nextFloat() <= result.chance()) {
                insertOutput(result.stack().copy());
            }
        }

        for (SievePool pool : recipe.pools()) {
            if (!pool.items().isEmpty() && level.random.nextFloat() <= pool.chance()) {
                int randomIndex = level.random.nextInt(pool.items().size());
                insertOutput(pool.items().get(randomIndex).copy());
            }
        }
    }

    private void insertOutput(ItemStack stackToInsert) {
        for (int i = 2; i <= 7; i++) {
            ItemStack slotStack = this.inventory.getStackInSlot(i);

            if (slotStack.isEmpty()) {
                this.inventory.setStackInSlot(i, stackToInsert);
                return;
            } else if (ItemStack.isSameItemSameComponents(slotStack, stackToInsert) && slotStack.getCount() + stackToInsert.getCount() <= slotStack.getMaxStackSize()) {
                slotStack.grow(stackToInsert.getCount());
                return;
            }
        }
    }

    public ItemStack getInputStack() {
        return this.inventory.getStackInSlot(INPUT_SLOT);
    }

    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }
}