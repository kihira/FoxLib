/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.toast;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Iterator;

public class ToastManager {

    public static final ToastManager INSTANCE = new ToastManager();

    private final ArrayList<Toast> toasts = new ArrayList<Toast>();

    private ToastManager() {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void createToast(int x, int y, String text) {
        toasts.add(new Toast(x, y, text, Minecraft.getMinecraft().fontRenderer.getStringWidth(text) * 4));
    }

    public void createCenteredToast(int x, int y, String text) {
        int stringWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        toasts.add(new Toast(x - (stringWidth / 2) - 5, y, text, stringWidth * 4));
    }

    @SubscribeEvent
    public void onClientTickPost(TickEvent.ClientTickEvent event) {
        Iterator<Toast> toasts = this.toasts.iterator();
        while (toasts.hasNext()) {
            Toast toast = toasts.next();
            toast.time--;
            if (toast.time <= 0) toasts.remove();
        }
    }

    @SubscribeEvent
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        Profiler profiler = Minecraft.getMinecraft().mcProfiler;
        profiler.startSection("toastNotification");
        for (Toast toast : toasts) {
            toast.drawToast(event.mouseX, event.mouseY);
        }
        profiler.endSection();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Profiler profiler = Minecraft.getMinecraft().mcProfiler;
            profiler.startSection("toastNotification");
            for (Toast toast : toasts) {
                toast.drawToast(event.mouseX, event.mouseY);
            }
            profiler.endSection();
        }
    }
}
