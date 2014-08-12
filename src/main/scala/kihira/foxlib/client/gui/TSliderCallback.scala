/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
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
