/*
 * Copyright (C) 2014  Kihira
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package kihira.foxcore.client.gui

import kihira.foxcore.client.RenderHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiListExtended
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry

import scala.collection.mutable.ListBuffer

class GuiList(width: Int, height: Int, top: Int, bottom: Int, leftM: Int, entryList: ListBuffer[IGuiListEntry]) extends GuiListExtended(Minecraft.getMinecraft, width, height, top, bottom, 20) {
    this.left = leftM
    this.right = this.left + width

    var listWidth: Int = 1000
    var currentIndex: Int = 0

    //Use a scissor helper here so it renders within its bounds
    //Also change list length to allow selecting the entries correctly
    override def drawScreen(p_148128_1_ : Int, p_148128_2_ : Int, p_148128_3_ : Float) {
        RenderHelper.startGlScissor(this.left, this.top, this.width, this.height)
        this.listWidth = 2000
        super.drawScreen(p_148128_1_, p_148128_2_, p_148128_3_)
        this.listWidth = this.width
        RenderHelper.endGlScissor()
    }

    /**
     * Called when an element is clicked
     * @param index: [[Int]] The index for the element
     * @param doubleClick: [[Boolean]] Whether it was a double click
     * @param mouseX: [[Int]] The mouse x position
     * @param mouseY: [[Int]] The mouse y position
     */
    override def elementClicked(index: Int, doubleClick: Boolean, mouseX : Int, mouseY : Int) {
        this.currentIndex = index
    }

    override def isSelected(index : Int): Boolean = index == this.currentIndex
    override def getSize: Int = this.entryList.size
    override def getListEntry(index: Int): IGuiListEntry = this.entryList(index)
    override def getScrollBarX: Int = this.right - 6
    override def getListWidth: Int = this.listWidth
    def getEntries: ListBuffer[_ <: IGuiListEntry] = this.entryList
}