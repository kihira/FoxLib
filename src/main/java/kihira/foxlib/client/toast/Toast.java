/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.toast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class Toast {

    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private final String message;
    private boolean mouseOver;
    public int time;

    public Toast(int xPos, int yPos, String message, int time) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.message = message;
        this.time = time;

        width = Minecraft.getMinecraft().fontRenderer.getStringWidth(message) + 10;
        height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 7;
    }

    public void drawToast(int mouseX, int mouseY) {
        if (this.time > 0) {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            mouseOver = mouseX >= xPos && mouseY >= yPos && mouseX < xPos + width && mouseY < yPos + height;
            int opacity = mouseOver ? 255 : (int) (this.time * 256F / 10F);
            if (opacity > 255) opacity = 255;
            if (mouseOver) time = 20;

            if (opacity > 0) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_LIGHTING);
                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                drawBackdrop(xPos, yPos, width, height);
                fontRenderer.drawStringWithShadow(message, xPos + 5, yPos + (height / 4), 0xFFFFFF | (opacity << 24));
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    protected void drawBackdrop(int x, int y, int width, int height) {
        int opacity = mouseOver ? 255 : (int) (this.time * 256F / 25F);
        if (opacity > 255) opacity = 255;

        //Black back
        int colour = (opacity << 24);
        Gui.drawRect(x + 1, y, x + width - 1, y + height, colour);
        Gui.drawRect(x, y + 1, x + 1, y + height - 1, colour);
        Gui.drawRect(x + width - 1, y + 1, x + width, y + height - 1, colour);

        //Border
        colour = 0x28025c | (opacity << 24);
        Gui.drawRect(x + 1, y + 1, x + width - 1, y + 2, colour);
        Gui.drawRect(x + 1, y + height - 1, x + width - 1, y + height - 2, colour);
        Gui.drawRect(x + 1, y + 1, x + 2, y + height - 1, colour);
        Gui.drawRect(x + width - 1, y + 1, x + width - 2, y + height - 1, colour);
    }
}
