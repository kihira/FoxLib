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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiBaseScreen extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_) {
        super.drawScreen(mouseX, mouseY, p_73863_3_);

        //Tooltips
        for (Object obj : this.buttonList) {
            //Has tooltip and has mouse over
            if (obj instanceof ITooltip && ((GuiButton) obj).isMouseOver()) {
                drawHoveringText(((ITooltip) obj).getTooltip(mouseX, mouseY), mouseX, mouseY);
            }
        }
    }

    public class GuiButtonTooltip extends GuiButtonExt implements ITooltip {
        private final int maxTextWidth;
        protected final ArrayList<String> tooltip = new ArrayList<String>();

        @SuppressWarnings("unchecked")
        public GuiButtonTooltip(int id, int x, int y, int width, int height, String text, int maxTextWidth, String... tooltips) {
            super(id, x, y, width, height, text);
            this.maxTextWidth = maxTextWidth;
            if (tooltips != null && tooltips.length > 0) {
                for (String s : tooltips) {
                    tooltip.addAll(fontRendererObj.listFormattedStringToWidth(s, this.maxTextWidth));
                }
            }
        }

        @Override
        public List<String> getTooltip(int mouseX, int mouseY) {
            return this.tooltip;
        }
    }

    public class GuiButtonToggle extends GuiButtonTooltip {

        public GuiButtonToggle(int id, int x, int y, int width, int height, String text, int maxTextWidth, String... tooltips) {
            super(id, x, y, width, height, text, maxTextWidth, tooltips);
        }

        @Override
        public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
            if (this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
                this.enabled = !this.enabled;
                return true;
            }
            return false;
        }

        @Override
        public void drawButtonForegroundLayer(int x, int y) {
            ArrayList<String> list = new ArrayList<String>();
            list.addAll(this.tooltip);
            list.add((!this.enabled ? TextFormatting.GREEN + TextFormatting.ITALIC.toString() + "Enabled" : TextFormatting.RED + TextFormatting.ITALIC.toString() + "Disabled"));
            drawHoveringText(list, x, y);
        }
    }
}
