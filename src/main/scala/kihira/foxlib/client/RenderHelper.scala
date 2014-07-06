/*
 * Copyright (C) 2014  Kihira/iChun
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

package kihira.foxlib.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{FontRenderer, ScaledResolution}
import net.minecraft.client.renderer.{OpenGlHelper, Tessellator}
import net.minecraft.client.renderer.entity.RenderManager
import org.lwjgl.opengl.GL11

object RenderHelper {

    //Following methods are copied from iChunUtil with permissions
    def startGlScissor(x: Int, y: Int, width: Int, height: Int) {
        val mc: Minecraft = Minecraft.getMinecraft
        val reso: ScaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
        val scaleW: Double = mc.displayWidth.asInstanceOf[Double] / reso.getScaledWidth_double
        val scaleH: Double = mc.displayHeight.asInstanceOf[Double] / reso.getScaledHeight_double
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(Math.floor(x.asInstanceOf[Double] * scaleW).asInstanceOf[Int], Math.floor(mc.displayHeight.asInstanceOf[Double] - ((y + height).asInstanceOf[Double] * scaleH)).asInstanceOf[Int], Math.floor((x + width).asInstanceOf[Double] * scaleW).asInstanceOf[Int] - Math.floor(x.asInstanceOf[Double] * scaleW).asInstanceOf[Int], Math.floor(mc.displayHeight.asInstanceOf[Double] - (y.asInstanceOf[Double] * scaleH)).asInstanceOf[Int] - Math.floor(mc.displayHeight.asInstanceOf[Double] - ((y + height).asInstanceOf[Double] * scaleH)).asInstanceOf[Int])
    }

    def endGlScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }

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
        val tessellator: Tessellator = Tessellator.instance
        val renderManager: RenderManager = RenderManager.instance
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

        tessellator.startDrawingQuads()
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F)
        tessellator.addVertex(xOffset - 1, -1, 0.0D)
        tessellator.addVertex(xOffset - 1, height, 0.0D)
        tessellator.addVertex(xOffset + maxWidth + 1, height, 0.0D)
        tessellator.addVertex(xOffset + maxWidth + 1, -1, 0.0D)
        tessellator.draw

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
        val fontrenderer: FontRenderer = Minecraft.getMinecraft.fontRenderer
        val tessellator: Tessellator = Tessellator.instance
        val renderManager: RenderManager = RenderManager.instance
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
            tessellator.startDrawingQuads()
            tessellator.setColorRGBA_F(0F, 0F, 0F, 0.25F)
            if (center) {
                //We add the offset of the font height to move it to the correct position
                tessellator.addVertex(-(width / 2) - 1, fontrenderer.FONT_HEIGHT, 0.0D)
                tessellator.addVertex(-(width / 2) - 1, -height + fontrenderer.FONT_HEIGHT - 1, 0.0D)
                tessellator.addVertex((width / 2) + 1, -height + fontrenderer.FONT_HEIGHT - 1, 0.0D)
                tessellator.addVertex((width / 2) + 1, fontrenderer.FONT_HEIGHT, 0.0D)
            }
            else {
                tessellator.addVertex(-1, height / 2, 0.0D)
                tessellator.addVertex(-1, -(height / 2) - 1, 0.0D)
                tessellator.addVertex(width + 1, -(height / 2) - 1, 0.0D)
                tessellator.addVertex(width + 1, height / 2, 0.0D)
            }
            tessellator.draw
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glTranslatef(0F, 0F, -0.1F) //Move render forward a little to prevent flickering
        for (line <- text) {
            if (center) fontrenderer.drawString(line, -fontrenderer.getStringWidth(line) / 2, 0, colour)
            else fontrenderer.drawString(line, 0, 0, colour)
            GL11.glTranslatef(0F, -fontrenderer.FONT_HEIGHT, 0F)
        }

        GL11.glEnable(GL11.GL_LIGHTING)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glColor4f(1F, 1F, 1F, 1F)
        GL11.glPopMatrix()
    }
}
