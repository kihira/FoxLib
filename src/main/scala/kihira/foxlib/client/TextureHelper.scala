/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client

import java.awt.image.{BufferedImage, ColorModel, WritableRaster}
import java.io.{IOException, InputStream}
import javax.imageio.ImageIO

import cpw.mods.fml.relauncher.ReflectionHelper
import kihira.foxlib.FoxLib
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.client.renderer.texture.{ITextureObject, TextureUtil}
import net.minecraft.util.ResourceLocation
import org.apache.commons.io.IOUtils

object TextureHelper {
    
    def getPlayerSkinAsBufferedImage(player:AbstractClientPlayer): BufferedImage = {
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
                    FoxLib.logger.warn("Failed to read players skin texture", e)
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

    def getPlayerSkinAsBufferedImage2(playerName: String): BufferedImage = {
        var skinResLoc: ResourceLocation = AbstractClientPlayer.getLocationSkin(playerName)
        var bufferedImage: BufferedImage = null

        if (skinResLoc == null) skinResLoc = AbstractClientPlayer.getLocationSkin("default")
        val imageData: ThreadDownloadImageData = AbstractClientPlayer.getDownloadImageSkin(skinResLoc, playerName)
        bufferedImage = ReflectionHelper.getPrivateValue(classOf[ThreadDownloadImageData], imageData, "bufferedImage") //TODO AT?

        if (bufferedImage != null) {
            //Clone the buffered image if we got one
            val cm: ColorModel = bufferedImage.getColorModel
            bufferedImage = new BufferedImage(cm, bufferedImage.copyData(null), cm.isAlphaPremultiplied, null)
        }
        bufferedImage
    }

    def restoreOriginalTexture(resourceLoc:ResourceLocation) {
/*        var textureObject:ITextureObject = Minecraft.getMinecraft.getTextureManager.getTexture(resourceLoc)

        if (textureObject == null) textureObject = new SimpleTexture(resourceLoc)
        else Minecraft.getMinecraft.getTextureManager.deleteTexture(resourceLoc)

        textureObject match {
            case data: ThreadDownloadImageData =>
                val bufferedImage:BufferedImage = ReflectionHelper.getPrivateValue(classOf[ThreadDownloadImageData], data, "bufferedImage")
                TextureUtil.uploadTextureImage(textureObject.getGlTextureId, bufferedImage)
            case _ =>
        }
        Minecraft.getMinecraft.getTextureManager.loadTexture(resourceLoc, textureObject)*/
    }

    def uploadTexture(resourceLoc: ResourceLocation, bufferedImage: BufferedImage) {
        val textureObject:ITextureObject = Minecraft.getMinecraft.getTextureManager.getTexture(resourceLoc)
        if (textureObject != null) uploadTexture(textureObject, bufferedImage)
    }

    def uploadTexture(textureObject: ITextureObject, bufferedImage: BufferedImage) {
        TextureUtil.uploadTextureImage(textureObject.getGlTextureId, bufferedImage)
    }
}
