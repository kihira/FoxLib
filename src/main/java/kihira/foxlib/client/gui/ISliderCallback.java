/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.gui;

public interface ISliderCallback {

    boolean onValueChange(GuiSlider slider, float oldValue, float newValue);
}
