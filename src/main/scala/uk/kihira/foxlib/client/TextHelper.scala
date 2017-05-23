/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package uk.kihira.foxlib.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.{GlStateManager, OpenGlHelper, Tessellator, VertexBuffer}

object TextHelper {
    /**
     * Renders a message wrapped to a certain length, expanding downwards
     * @param x:[[Double]] The x position
     * @param y:[[Double]] The y position
     * @param z:[[Double]] The z position
     * @param scale:[[Float]] The scale of the text
     * @param maxWidth:[[Int]] The maximum width of the string per line
     * @param xOffset:[[Double]] How far offset the chat should be from the position defined
     * @param text:[[Array[String]] An array of the string that is rendered in order
     * @param colour:[[Int]] The Minecraft version of colour
     */
    def drawWrappedMessageFacingPlayer(x:Double, y:Double, z:Double, scale:Float = 0.016666668F, maxWidth:Int, xOffset:Double, text:String, colour:Int = -1) {
        val fontrenderer: FontRenderer = Minecraft.getMinecraft.fontRenderer
        val renderer: VertexBuffer = Tessellator.getInstance().getBuffer
        val renderManager: RenderManager = Minecraft.getMinecraft.getRenderManager
        val height: Double = fontrenderer.listFormattedStringToWidth(text, maxWidth).size * fontrenderer.FONT_HEIGHT

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F)
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
        GlStateManager.scale(-scale, -scale, scale)
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        GlStateManager.disableTexture2D()

        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        renderer.pos(xOffset - 1, -1, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
        renderer.pos(xOffset - 1, height, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
        renderer.pos(xOffset + maxWidth + 1, height, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
        renderer.pos(xOffset + maxWidth + 1, -1, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
        renderer.finishDrawing()

        GlStateManager.enableTexture2D()
        GlStateManager.translate(0F, 0F, -0.1F) //Move render forward a little to prevent flickering
        fontrenderer.drawSplitString(text, xOffset.asInstanceOf[Int], 0, maxWidth, colour)

        GlStateManager.enableLighting()
        GlStateManager.disableBlend()
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F)
        GlStateManager.popMatrix()
    }

    /**
     * Draws a multi line string from the y position and expanding up.
     * @param x:[[Double]] The x position
     * @param y:[[Double]] The y position
     * @param z:[[Double]] The z position
     * @param scale:[[Float]] The scale of the text
     * @param text:[[Array[String]] An array of the string that is rendered in order
     * @param colour:[[Int]] The Minecraft version of colour
     * @param center:[[Boolean]] Whether to draw the string centered or left aligned
     * @param background:[[Boolean]] Whether to render the faint black background
     */
    def drawMultiLineMessageFacingPlayer(x:Double, y:Double, z:Double, scale:Float = 0.016666668F, text:Array[String], colour:Int = -1, center:Boolean = true, background:Boolean = true) {
        val fontrenderer: FontRenderer = Minecraft.getMinecraft.fontRenderer
        val renderer: VertexBuffer = Tessellator.getInstance().getBuffer
        val renderManager: RenderManager = Minecraft.getMinecraft.getRenderManager
        val height: Double = text.size * fontrenderer.FONT_HEIGHT
        val width: Int = {
            var wid, w = 0
            for (line <- text) {
                w = fontrenderer.getStringWidth(line)
                if (w > wid) wid = w
            }
            wid
        }

        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, z)
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F)
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
        GlStateManager.scale(-scale, -scale, scale)
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.disableCull()
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        GlStateManager.disableTexture2D()

        if (background) {
            renderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
            if (center) {
                //We add the offset of the font height to move it to the correct position
                renderer.pos(-(width / 2) - 1, fontrenderer.FONT_HEIGHT, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
                renderer.pos(-(width / 2) - 1, -height + fontrenderer.FONT_HEIGHT - 1, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
                renderer.pos((width / 2) + 1, -height + fontrenderer.FONT_HEIGHT - 1, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
                renderer.pos((width / 2) + 1, fontrenderer.FONT_HEIGHT, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
            }
            else {
                renderer.pos(-1, height / 2, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
                renderer.pos(-1, -(height / 2) - 1, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
                renderer.pos(width + 1, -(height / 2) - 1, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
                renderer.pos(width + 1, height / 2, 0.0D).color(0f, 0f, 0f, 0.25f).endVertex()
            }
            renderer.finishDrawing()
        }

        GlStateManager.enableTexture2D()
        GlStateManager.translate(0F, 0F, -0.1F) //Move render forward a little to prevent flickering
        for (line <- text) {
            if (center) fontrenderer.drawString(line, -fontrenderer.getStringWidth(line) / 2, 0, colour)
            else fontrenderer.drawString(line, 0, 0, colour)
            GlStateManager.translate(0F, -fontrenderer.FONT_HEIGHT, 0F)
        }

        GlStateManager.enableCull()
        GlStateManager.disableBlend()
        GlStateManager.color(1F, 1F, 1F, 1F)
        GlStateManager.popMatrix()
    }
}
