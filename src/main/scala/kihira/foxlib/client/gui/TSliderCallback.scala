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

package kihira.foxlib.client.gui

trait TSliderCallback {

    /**
     * This called when a [[kihira.foxlib.client.gui.GuiSlider]] value has changed and this is the sliders parent.
     * @param id The button ID for the slider
     * @param prevValue The previous value
     * @param newValue The new value
     * @return If it should be changed to the new value
     */
    def onSliderChangeValue(id: Int, prevValue: Float, newValue: Float): Boolean

}
