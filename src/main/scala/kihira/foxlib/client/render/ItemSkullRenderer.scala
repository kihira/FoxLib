/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client.render

import java.util

import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftProfileTexture
import kihira.foxlib.client.model.ModelSkull
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTUtil}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.{ItemRenderType, ItemRendererHelper}
import org.lwjgl.opengl.{GL11, GL12}

object ItemSkullRenderer extends IItemRenderer {
    private final val skeletonTex: ResourceLocation = new ResourceLocation("textures/entity/skeleton/skeleton.png")
    private final val wSkeletonTex: ResourceLocation = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png")
    private final val zombieTex: ResourceLocation = new ResourceLocation("textures/entity/zombie/zombie.png")
    private final val creeperTex: ResourceLocation = new ResourceLocation("textures/entity/creeper/creeper.png")
    val modelSkull: ModelSkull = new ModelSkull()
    val modelSkullLarge: ModelSkull = new ModelSkull(texHeight = 64)

    override def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true

    override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true

    override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*) = {
        `type` match {
            case ItemRenderType.EQUIPPED_FIRST_PERSON =>
                renderSkull(item, 0.5F, 0, 0.5F, 2F, 0F)
            case ItemRenderType.EQUIPPED =>
                renderSkull(item, 0.5F, 0, 0.5F, 2F, -90F)
            case ItemRenderType.INVENTORY =>
                renderSkull(item, 0, -0.5F, 0, 1.8F, 90F)
            case _ =>
                renderSkull(item, 0, 0, 0, 1F, 0)
        }
    }

    private def renderSkull(itemStack: ItemStack, x: Float, y: Float, z: Float, scale: Float, rot: Float) {
        val tagCompound: NBTTagCompound = itemStack.getTagCompound
        var gameProfile: GameProfile = null
        if (tagCompound != null && tagCompound.hasKey("SkullOwner", 10)) {
            gameProfile = NBTUtil.func_152459_a(tagCompound.getCompoundTag("SkullOwner"))
        }

        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glEnable(GL12.GL_RESCALE_NORMAL)
        GL11.glTranslatef(x, y, z)
        GL11.glScalef(-scale, -scale, scale)
        GL11.glRotatef(rot, 0F, 1F, 0F)
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        //Bind the correct texture
        itemStack.getItemDamage match {
            case 1 =>
                Minecraft.getMinecraft.renderEngine.bindTexture(wSkeletonTex)
            case 2 =>
                Minecraft.getMinecraft.renderEngine.bindTexture(zombieTex)
                modelSkullLarge.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F)
                GL11.glPopMatrix()
                return
            case 3 =>
                var resourcelocation: ResourceLocation = AbstractClientPlayer.locationStevePng
                if (gameProfile != null) {
                    val minecraft: Minecraft = Minecraft.getMinecraft
                    val map: util.Map[_, _] = minecraft.func_152342_ad.func_152788_a(gameProfile)
                    if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                        resourcelocation = minecraft.func_152342_ad.func_152792_a(map.get(MinecraftProfileTexture.Type.SKIN).asInstanceOf[MinecraftProfileTexture], MinecraftProfileTexture.Type.SKIN)
                    }
                }
                Minecraft.getMinecraft.renderEngine.bindTexture(resourcelocation)
            case 4 =>
                Minecraft.getMinecraft.renderEngine.bindTexture(creeperTex)
            case _ =>
                Minecraft.getMinecraft.renderEngine.bindTexture(skeletonTex)
        }
        modelSkull.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F)
        GL11.glPopMatrix()
    }
}
