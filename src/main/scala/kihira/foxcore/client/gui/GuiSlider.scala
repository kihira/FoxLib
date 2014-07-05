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

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11

import scala.math.BigDecimal.RoundingMode

/**
 * A basic slider implementation. Can define an upper and lower limit as well as maximum amount of decimal places. Can
 * also define a parent which implements [[TSliderCallback]] so when this value is changed, the parent is notified.
 * The decimal is calculated using [[RoundingMode.HALF_EVEN]]
 * @param parent: [[TSliderCallback]] The parent
 * @param id: [[Int]] The button ID
 * @param xPos: [[Int]] The x position
 * @param yPos: [[Int]] The y position
 * @param width: [[Int]] The width
 * @param height: [[Int]] The height
 * @param text: [[String]] The display text before the value
 * @param minValue: [[Float]] The minimum value
 * @param maxValue: [[Float]] The maximum value
 * @param currentValue: [[Float]] The current/default value
 * @param decimal: [[Int]] Maximum amount of decimal places
 */
class GuiSlider(parent: TSliderCallback, id: Int, xPos: Int, yPos: Int, width: Int, height: Int, text: String, minValue: Float, maxValue: Float, var currentValue: Float, decimal: Int) extends GuiButton(id, xPos, yPos, width, height, text) {
    def this(id: Int, xPos: Int, yPos: Int, text: String, minValue: Float, maxValue: Float, currentValue: Float, decimal: Int) = this(null, id, xPos, yPos, 200, 20, text, minValue, maxValue, currentValue, decimal)
    this.displayString = text + ":" + String.valueOf(this.currentValue)

    var sliderValue: Float = MathHelper.clamp_float((this.currentValue - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F)
    var dragging: Boolean = false

    /**
     * Called when the mouse button moved
     * @param minecraft: [[Minecraft]] The Minecraft instance
     * @param xPos: [[Int]] The mouse x co-ord
     * @param yPos: [[Int]] The mouse y co-ord
     */
    override def mouseDragged(minecraft: Minecraft, xPos: Int, yPos: Int) {
        if (this.visible) {
            if (this.dragging) {
                this.updateValues(xPos, yPos)
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
            this.drawTexturedModalRect(this.xPosition + (this.sliderValue * (this.width - 8).asInstanceOf[Float]).asInstanceOf[Int], this.yPosition, 0, 66, 4, 20)
            this.drawTexturedModalRect(this.xPosition + (this.sliderValue * (this.width - 8).asInstanceOf[Float]).asInstanceOf[Int] + 4, this.yPosition, 196, 66, 4, 20)
        }
    }

    /**
     * Called when the left mouse button is clicked
     * @param minecraft: [[Minecraft]] The Minecraft instance
     * @param xPos: [[Int]] The mouse x co-ord
     * @param yPos: [[Int]] The mouse y co-ord
     */
    override def mousePressed(minecraft : Minecraft, xPos : Int, yPos : Int): Boolean = {
        if (super.mousePressed(minecraft, xPos, yPos)) {
            this.updateValues(xPos, yPos)
            this.dragging = true
            true
        }
        else {
            false
        }
    }

    protected override def getHoverState(p_146114_1_ : Boolean): Int = 0

    override def mouseReleased(p_146118_1_ : Int, p_146118_2_ : Int) {
        this.dragging = false
    }

    /**
     * Updates the display, current value and internal slider position based on mouse co-ords
     * @param xPos: [[Int]] The mouse x co-ord
     * @param yPos: [[Int]] The mouse y co-ord
     */
    private def updateValues(xPos: Int, yPos: Int) {
        this.sliderValue = MathHelper.clamp_float((xPos - (this.xPosition + 4)) / (this.width - 8).asInstanceOf[Float], 0F, 1F)
        val prevValue: Float = this.currentValue
        this.currentValue = BigDecimal(this.sliderValue * (maxValue - minValue) + minValue).setScale(this.decimal, RoundingMode.HALF_EVEN).toFloat

        if (this.parent != null) {
            //If the parent doesn't want this value set, revert
            if (!this.parent.onSliderChangeValue(this.id, prevValue, this.currentValue)) {
                this.currentValue = prevValue
                this.sliderValue = MathHelper.clamp_float((this.currentValue - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F)
            }
        }
        this.displayString = text + ":" + String.valueOf(this.currentValue)
    }
}
