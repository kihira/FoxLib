/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client

import java.awt.image.BufferedImage
import java.io.{IOException, InputStream}
import java.util
import javax.imageio.ImageIO

import com.mojang.authlib.minecraft.MinecraftProfileTexture
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import kihira.foxlib.FoxLib
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.client.renderer.texture.{DynamicTexture, ITextureObject, TextureUtil}
import net.minecraft.util.ResourceLocation
import org.apache.commons.io.IOUtils

object TextureHelper {
    
    def getPlayerSkinAsBufferedImage(player:AbstractClientPlayer): BufferedImage = {
        var bufferedImage: BufferedImage = null
        var inputStream: InputStream = null
        val mc: Minecraft = Minecraft.getMinecraft
        val map: util.Map[_, _] = mc.getSkinManager.loadSkinFromCache(player.getGameProfile)
        var skintex: ITextureObject = null
        val playerName: String = player.getName

        try {
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                skintex = mc.getTextureManager.getTexture(mc.getSkinManager.loadSkin(map.get(MinecraftProfileTexture.Type.SKIN).asInstanceOf[MinecraftProfileTexture], MinecraftProfileTexture.Type.SKIN))
            }
            else {
                skintex = mc.getTextureManager.getTexture(player.getLocationSkin)
            }

            skintex match {
                case imagedata: ThreadDownloadImageData =>
                    FoxLib.logger.debug(s"Loading $playerName skin")
                    bufferedImage = ObfuscationReflectionHelper.getPrivateValue(classOf[ThreadDownloadImageData], imagedata, "field_110560_d", "bufferedImage")
                case imagedata: DynamicTexture =>
                    FoxLib.logger.warn(s"$playerName skin is a DynamicTexture! Attempting to load anyway")
                    val width: Int = ObfuscationReflectionHelper.getPrivateValue(classOf[DynamicTexture], imagedata, "field_94233_j", "width")
                    val height: Int = ObfuscationReflectionHelper.getPrivateValue(classOf[DynamicTexture], imagedata, "field_94234_k", "height")
                    bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
                    bufferedImage.setRGB(0, 0, width, height, imagedata.getTextureData, 0, width)
                case _=>
                    FoxLib.logger.warn(s"Could not fetch $playerName skin, loading default skin")
                    inputStream = Minecraft.getMinecraft.getResourceManager.getResource(DefaultPlayerSkin.getDefaultSkinLegacy).getInputStream
                    bufferedImage = ImageIO.read(inputStream)
            }
        }
        catch {
            case e: IOException =>
                FoxLib.logger.error(s"Failed to read $playerName skin texture", e)
            }
        finally {
            IOUtils.closeQuietly(inputStream)
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
