/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.gui;

import java.util.List;

/**
 * Implemented by GuiButton's that has a tooltip
 */
public interface ITooltip {
    List<String> getTooltip(int mouseX, int mouseY);
}
