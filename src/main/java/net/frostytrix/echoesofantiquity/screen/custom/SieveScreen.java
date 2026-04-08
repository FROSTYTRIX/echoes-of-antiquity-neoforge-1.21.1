package net.frostytrix.echoesofantiquity.screen.custom;

import net.frostytrix.echoesofantiquity.EchoesOfAntiquity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SieveScreen extends AbstractContainerScreen<SieveScreenHandler> {
    public static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, "textures/gui/sieve/sieve_gui.png");
    public static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EchoesOfAntiquity.MOD_ID, "textures/gui/sieve/arrow_progress.png");

    public SieveScreen(SieveScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        // Mojang mapping: 'handler' is 'menu'
        if (this.menu.isSifting()) {
            guiGraphics.blit(ARROW_TEXTURE, x + 58, y + 35, 0, 0,
                    this.menu.getScaledArrowProgress(), 16, 24, 16);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}