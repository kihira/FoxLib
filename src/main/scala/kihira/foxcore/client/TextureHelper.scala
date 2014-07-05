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

package kihira.foxcore.client

import java.awt.image.{WritableRaster, ColorModel, BufferedImage}
import java.io.{IOException, InputStream}
import javax.imageio.ImageIO

import cpw.mods.fml.relauncher.ReflectionHelper
import kihira.foxcore.FoxCore
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.{EntityPlayerSP, AbstractClientPlayer}
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.client.renderer.texture.{ITextureObject, SimpleTexture, TextureUtil}
import net.minecraft.util.ResourceLocation
import org.apache.commons.io.IOUtils

object TextureHelper {
    
    def getPlayerSkinAsBufferedImage(player:EntityPlayerSP): BufferedImage = {
        var bufferedImage: BufferedImage = null
        var inputStream: InputStream = null
        if (player.func_152123_o) {
            val imageData: ThreadDownloadImageData = AbstractClientPlayer.getDownloadImageSkin(player.getLocationSkin, player.getCommandSenderName)
            bufferedImage = ReflectionHelper.getPrivateValue(classOf[ThreadDownloadImageData], imageData, "bufferedImage")
        }
        else {
            try {
                inputStream = Minecraft.getMinecraft.getResourceManager.getResource(AbstractClientPlayer.locationStevePng).getInputStream
                bufferedImage = ImageIO.read(inputStream)
            }
            catch {
                case e: IOException => 
                    FoxCore.logger.warn("Failed to read players skin texture", e)
            }
            finally {
                IOUtils.closeQuietly(inputStream)
            }
        }
        if (bufferedImage != null) {
            val cm:ColorModel = bufferedImage.getColorModel
            val isAlphaPremultiplied = cm.isAlphaPremultiplied
            val raster:WritableRaster = bufferedImage.copyData(null)
            return new BufferedImage(cm, raster, isAlphaPremultiplied, null)
        }
        bufferedImage
    }

    def restoreOriginalTexture(resourceLoc:ResourceLocation) {
        var textureObject:ITextureObject = Minecraft.getMinecraft.getTextureManager.getTexture(resourceLoc)

        if (textureObject == null) textureObject = new SimpleTexture(resourceLoc)
        else Minecraft.getMinecraft.getTextureManager.deleteTexture(resourceLoc)

        textureObject match {
            case data: ThreadDownloadImageData =>
                val bufferedImage:BufferedImage = ReflectionHelper.getPrivateValue(classOf[ThreadDownloadImageData], data, "bufferedImage")
                TextureUtil.uploadTextureImage(textureObject.getGlTextureId, bufferedImage)
            case _ =>
        }
        Minecraft.getMinecraft.getTextureManager.loadTexture(resourceLoc, textureObject)
    }

    def uploadTexture(resourceLoc:ResourceLocation, bufferedImage:BufferedImage) {
        val textureObject:ITextureObject = Minecraft.getMinecraft.getTextureManager.getTexture(resourceLoc)
        if (textureObject != null) uploadTexture(textureObject, bufferedImage)
    }

    def uploadTexture(textureObject:ITextureObject, bufferedImage:BufferedImage) {
        TextureUtil.uploadTextureImage(textureObject.getGlTextureId, bufferedImage)
    }
}
