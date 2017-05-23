/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package uk.kihira.foxlib.client

import java.util

import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.{GlStateManager, OpenGlHelper, RenderGlobal}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.{AxisAlignedBB, RayTraceResult}
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import uk.kihira.foxlib.FoxLib

object ClientEventHandler {

  @SubscribeEvent
  def onBlockHighlight(e: DrawBlockHighlightEvent) = {
    if (FoxLib.showCollisionBoxes && e.getTarget.typeOfHit == RayTraceResult.Type.BLOCK) {
      val player: EntityPlayer = Minecraft.getMinecraft.player
      val state: IBlockState = Minecraft.getMinecraft.world.getBlockState(e.getTarget.getBlockPos)
      val xOffset: Double = player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks
      val yOffset: Double = player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks
      val zOffset: Double = player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks
      val collisionBoxes: util.List[AxisAlignedBB] = new util.ArrayList[AxisAlignedBB]()

      state.addCollisionBoxToList(Minecraft.getMinecraft.world, e.getTarget.getBlockPos,
        TileEntity.INFINITE_EXTENT_AABB, collisionBoxes, null, false)

      //Render the box(es)
      GlStateManager.pushMatrix()
      GlStateManager.enableBlend()
      OpenGlHelper.glBlendFunc(770, 771, 1, 0)
      GlStateManager.color(0F, 0F, 0F, 0.4F)
      GlStateManager.glLineWidth(2F)
      GlStateManager.disableTexture2D()
      GlStateManager.depthMask(false)
      import scala.collection.JavaConversions._
      for (collisionBox <- collisionBoxes) {
        RenderGlobal.drawSelectionBoundingBox(collisionBox.offset(-xOffset, -yOffset, -zOffset), 0.0F, 0.0F, 0.0F, 0.4F) //drawSelectionBoundingBox
      }
      GlStateManager.depthMask(true)
      GlStateManager.enableTexture2D()
      GlStateManager.disableBlend()
      GlStateManager.popMatrix()

      e.setCanceled(true)
    }
  }
}
