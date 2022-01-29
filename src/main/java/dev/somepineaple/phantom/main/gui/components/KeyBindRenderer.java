package dev.somepineaple.phantom.main.gui.components;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class KeyBindRenderer {
    private static final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    private final Module module;
    //private int x;
    private int y;
    //private int width;
    private int height;
    private boolean isBinding;
    public KeyBindRenderer(Module module) {
        this.module = module;

        isBinding = false;
    }

    public void render(int x, int y, int width, int height) {
        //this.x = x;
        this.y = y;
        // this.width = width;
        this.height = height;
        String keyName = isBinding ? "..." : (module.getBind() == -1 ? "None" : Keyboard.getKeyName(module.getBind()));

        String fullText = "Bind <" + keyName + ">";

        int textColor = new Color(
                Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text R").intVal(),
                Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text G").intVal(),
                Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text B").intVal(),
                Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text A").intVal()
        ).getRGB();

        int nameXOffset = width / 2 - fr.getStringWidth(fullText) / 2;
        int nameYOffset = height / 2 - fr.FONT_HEIGHT / 2;

        fr.drawString(fullText, x + nameXOffset, y + nameYOffset, textColor);
    }

    public void processKey(int keyCode) {
        if (isBinding) {
            module.setBind(keyCode);
            if (keyCode == Keyboard.KEY_DELETE) {
                module.setBind(-1);
            }

            isBinding = false;
        }
    }

    public void processClick() {
        isBinding = !isBinding;
    }

    public int getHeight() {
        return height;
    }

    public int getY() {
        return y;
    }
}
