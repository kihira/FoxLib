/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.{OpenGlHelper, Tessellator, WorldRenderer}
import org.lwjgl.opengl.GL11

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
        val fontrenderer: FontRenderer = Minecraft.getMinecraft.fontRendererObj
        val renderer: WorldRenderer = Tessellator.getInstance().getWorldRenderer
        val renderManager: RenderManager = Minecraft.getMinecraft.getRenderManager
        val height: Double = fontrenderer.listFormattedStringToWidth(text, maxWidth).size * fontrenderer.FONT_HEIGHT

        GL11.glPushMatrix()
        GL11.glTranslated(x, y, z)
        GL11.glNormal3f(0.0F, 1.0F, 0.0F)
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
        GL11.glScalef(-scale, -scale, scale)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glEnable(GL11.GL_BLEND)
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        GL11.glDisable(GL11.GL_TEXTURE_2D)

        renderer.func_181668_a(7, DefaultVertexFormats.field_181706_f)
        renderer.func_181662_b(xOffset - 1, -1, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
        renderer.func_181662_b(xOffset - 1, height, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
        renderer.func_181662_b(xOffset + maxWidth + 1, height, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
        renderer.func_181662_b(xOffset + maxWidth + 1, -1, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
        renderer.finishDrawing()

        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glTranslatef(0F, 0F, -0.1F) //Move render forward a little to prevent flickering
        fontrenderer.drawSplitString(text, xOffset.asInstanceOf[Int], 0, maxWidth, colour)

        GL11.glEnable(GL11.GL_LIGHTING)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
        GL11.glPopMatrix()
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
        val fontrenderer: FontRenderer = Minecraft.getMinecraft.fontRendererObj
        val renderer: WorldRenderer = Tessellator.getInstance().getWorldRenderer
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

        GL11.glPushMatrix()
        GL11.glTranslated(x, y, z)
        GL11.glNormal3f(0.0F, 1.0F, 0.0F)
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F)
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F)
        GL11.glScalef(-scale, -scale, scale)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_CULL_FACE)
        OpenGlHelper.glBlendFunc(770, 771, 1, 0)
        GL11.glDisable(GL11.GL_TEXTURE_2D)

        if (background) {
            renderer.func_181668_a(7, DefaultVertexFormats.field_181706_f)
            if (center) {
                //We add the offset of the font height to move it to the correct position
                renderer.func_181662_b(-(width / 2) - 1, fontrenderer.FONT_HEIGHT, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
                renderer.func_181662_b(-(width / 2) - 1, -height + fontrenderer.FONT_HEIGHT - 1, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
                renderer.func_181662_b((width / 2) + 1, -height + fontrenderer.FONT_HEIGHT - 1, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
                renderer.func_181662_b((width / 2) + 1, fontrenderer.FONT_HEIGHT, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
            }
            else {
                renderer.func_181662_b(-1, height / 2, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
                renderer.func_181662_b(-1, -(height / 2) - 1, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
                renderer.func_181662_b(width + 1, -(height / 2) - 1, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
                renderer.func_181662_b(width + 1, height / 2, 0.0D).func_181666_a(0f, 0f, 0f, 0.25f).func_181675_d()
            }
            renderer.finishDrawing()
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glTranslatef(0F, 0F, -0.1F) //Move render forward a little to prevent flickering
        for (line <- text) {
            if (center) fontrenderer.drawString(line, -fontrenderer.getStringWidth(line) / 2, 0, colour)
            else fontrenderer.drawString(line, 0, 0, colour)
            GL11.glTranslatef(0F, -fontrenderer.FONT_HEIGHT, 0F)
        }

        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glColor4f(1F, 1F, 1F, 1F)
        GL11.glPopMatrix()
    }
}
