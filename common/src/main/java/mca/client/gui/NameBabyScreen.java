package mca.client.gui;

import mca.cobalt.network.NetworkHandler;
import mca.item.BabyItem;
import mca.network.s2c.BabyNameRequest;
import mca.network.s2c.BabyNamingVillagerMessage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import java.util.Objects;

public class NameBabyScreen extends Screen {
    private final ItemStack baby;
    private final PlayerEntity player;
    private TextFieldWidget babyNameTextField;

    public NameBabyScreen(PlayerEntity player, ItemStack baby) {
        super(new TranslatableText("gui.nameBaby.title"));
        this.baby = baby;
        this.player = player;
    }

    @Override
    public void tick() {
        super.tick();

        babyNameTextField.tick();
    }

    @Override
    public void init() {
        addDrawableChild(new ButtonWidget(width / 2 - 40, height / 2 + 20, 80, 20, new TranslatableText("gui.button.done"), (b) -> {
            NetworkHandler.sendToServer(new BabyNamingVillagerMessage(player.getInventory().selectedSlot, babyNameTextField.getText().trim()));
            Objects.requireNonNull(this.client).setScreen(null);
        }));
        addDrawableChild(new ButtonWidget(width / 2 + 105, height / 2 - 20, 60, 20, new TranslatableText("gui.button.random"), (b) -> {
            NetworkHandler.sendToServer(new BabyNameRequest(((BabyItem)baby.getItem()).getGender()));
        }));

        babyNameTextField = new TextFieldWidget(this.textRenderer, width / 2 - 100, height / 2 - 20, 200, 20, new TranslatableText("structure_block.structure_name"));
        babyNameTextField.setMaxLength(32);

        setInitialFocus(babyNameTextField);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack transform, int w, int h, float scale) {
        renderBackground(transform);

        setFocused(babyNameTextField);

        drawCenteredText(transform, this.textRenderer, this.title, this.width / 2, 70, 16777215);

        babyNameTextField.render(transform, width / 2 - 100, height / 2 - 20, scale);

        super.render(transform, w, h, scale);
    }

    public void setBabyName(String name) {
        babyNameTextField.setText(name);
    }
}
