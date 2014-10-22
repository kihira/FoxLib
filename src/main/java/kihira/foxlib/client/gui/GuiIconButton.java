/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class GuiIconButton extends GuiButton implements ITooltip {

    public static final ResourceLocation iconsTextures = new ResourceLocation("foxlib", "textures/gui/icons.png");

    private final Icons icon;
    private final List<String> tooltip;

    public GuiIconButton(int id, int x, int y, Icons icon, String ... tooltips) {
        super(id, x, y, 16 ,16, "");
        this.icon = icon;
        this.tooltip = Arrays.asList(tooltips);
    }

    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (visible) {
            minecraft.getTextureManager().bindTexture(iconsTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);

            //Check mouse over
            field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int textureOffset = getHoverState(field_146123_n);

            drawTexturedModalRect(xPosition, yPosition, icon.u, icon.v + (textureOffset * 16), 16, 16);
        }
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY) {
        return tooltip;
    }

    public enum Icons {
        UNDO(0, 0),
        QUESTION(16, 0),
        EYEDROPPER(32, 0);

        public final int u;
        public final int v;

        private Icons(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }
}
