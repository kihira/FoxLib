/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.client

import java.util

import kihira.foxlib.FoxLib
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.{OpenGlHelper, RenderGlobal}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

object ClientEventHandler {

    @SubscribeEvent
    def onBlockHighlight(e: DrawBlockHighlightEvent) = {
        if (FoxLib.showCollisionBoxes && e.target.typeOfHit == MovingObjectType.BLOCK) {
            val player: EntityPlayer = Minecraft.getMinecraft.thePlayer
            val block: IBlockState = Minecraft.getMinecraft.theWorld.getBlockState(e.target.getBlockPos)
            val xOffset: Double = player.prevPosX + (player.posX - player.prevPosX) * e.partialTicks
            val yOffset: Double = player.prevPosY + (player.posY - player.prevPosY) * e.partialTicks
            val zOffset: Double = player.prevPosZ + (player.posZ - player.prevPosZ) * e.partialTicks
            val collisionBoxes: util.List[AxisAlignedBB] = new util.ArrayList[AxisAlignedBB]()

            block.getBlock.addCollisionBoxesToList(Minecraft.getMinecraft.theWorld, e.target.getBlockPos, block, TileEntity.INFINITE_EXTENT_AABB, collisionBoxes, null)

            //Render the box(es)
            GL11.glPushMatrix()
            GL11.glEnable(GL11.GL_BLEND)
            OpenGlHelper.glBlendFunc(770, 771, 1, 0)
            GL11.glColor4f(0F, 0F, 0F, 0.4F)
            GL11.glLineWidth(2F)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDepthMask(false)
            import scala.collection.JavaConversions._
            for (collisionBox <- collisionBoxes) {
                RenderGlobal.drawSelectionBoundingBox(collisionBox.offset(-xOffset, -yOffset, -zOffset))
            }
            GL11.glDepthMask(true)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()

            e.setCanceled(true)
        }
    }
}
