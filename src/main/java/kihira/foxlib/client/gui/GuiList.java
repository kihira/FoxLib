/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.gui;

import kihira.foxlib.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

import java.util.List;

public class GuiList extends GuiListExtended {

    private final IListCallback parent;
    private final List<? extends IGuiListEntry> entries;
    private int currrentIndex;

    public GuiList(IListCallback parent, int width, int height, int top, int bottom, int slotHeight, List<? extends IGuiListEntry> entries) {
        super(Minecraft.getMinecraft(), width, height, top, bottom, slotHeight);
        this.parent = parent;
        this.entries = entries;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_148128_3_) {
        RenderHelper.startGlScissor(this.left, this.top, this.width, this.height);
        super.drawScreen(mouseX, mouseY, p_148128_3_);
        RenderHelper.endGlScissor();
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {}

    @Override
    protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
        this.currrentIndex = index;
        if (this.parent != null) this.parent.onEntrySelected(this, index, this.getListEntry(index));
    }

    @Override
    protected int getScrollBarX() {
        return this.right - 6;
    }

    @Override
    protected boolean isSelected(int index) {
        return this.currrentIndex == index;
    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return this.entries.get(index);
    }

    @Override
    protected int getSize() {
        return this.entries.size();
    }

    public int getCurrrentIndex() {
        return this.currrentIndex;
    }

    public void setCurrrentIndex(int currrentIndex) {
        this.currrentIndex = currrentIndex;
    }

    public List<? extends IGuiListEntry> getEntries() {
        return this.entries;
    }
}
