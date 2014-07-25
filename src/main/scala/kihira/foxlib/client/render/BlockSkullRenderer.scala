package kihira.foxlib.client.render

import java.util
import java.util.Calendar

import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftProfileTexture
import kihira.foxlib.common.EntityHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer
import net.minecraft.tileentity.TileEntitySkull
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.{GL11, GL12}

object BlockSkullRenderer extends TileEntitySkullRenderer {
    override def renderTileEntityAt(p_147500_1_ : TileEntitySkull, p_147500_2_ : Double, p_147500_4_ : Double, p_147500_6_ : Double, p_147500_8_ : Float) {
        this.render(p_147500_1_, p_147500_2_.asInstanceOf[Float], p_147500_4_.asInstanceOf[Float], p_147500_6_.asInstanceOf[Float], p_147500_1_.getBlockMetadata & 7, (p_147500_1_.func_145906_b * 360).asInstanceOf[Float] / 16.0F, p_147500_1_.func_145904_a, p_147500_1_.func_152108_a)
    }

    def render(skull: TileEntitySkull, par1: Float, par2: Float, par3: Float, par4: Int, par5: Float, par6: Int, gameProfile: GameProfile) {
        var rot: Float = par5
        if (par6 == 3) {
            var resourcelocation: ResourceLocation = AbstractClientPlayer.locationStevePng
            if (gameProfile != null) {
                val minecraft: Minecraft = Minecraft.getMinecraft
                val map: util.Map[_, _] = minecraft.func_152342_ad.func_152788_a(gameProfile)
                if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                    resourcelocation = minecraft.func_152342_ad.func_152792_a(map.get(MinecraftProfileTexture.Type.SKIN).asInstanceOf[MinecraftProfileTexture], MinecraftProfileTexture.Type.SKIN)
                }
            }
            this.bindTexture(resourcelocation)
            GL11.glPushMatrix()
            GL11.glDisable(GL11.GL_CULL_FACE)
            if (par4 != 1) {
                par4 match {
                    case 2 =>
                        GL11.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.74F)
                    case 3 =>
                        GL11.glTranslatef(par1 + 0.5F, par2 + 0.25F, par3 + 0.26F)
                        rot = 180.0F
                    case 4 =>
                        GL11.glTranslatef(par1 + 0.74F, par2 + 0.25F, par3 + 0.5F)
                        rot = 270.0F
                    case _ =>
                        GL11.glTranslatef(par1 + 0.26F, par2 + 0.25F, par3 + 0.5F)
                        rot = 90.0F
                }
            }
            else GL11.glTranslatef(par1 + 0.5F, par2, par3 + 0.5F)

            GL11.glEnable(GL12.GL_RESCALE_NORMAL)
            GL11.glScalef(-1.0F, -1.0F, 1.0F)
            GL11.glEnable(GL11.GL_ALPHA_TEST)
            if (skull != null) {
                val hour: Int = Calendar.getInstance.get(Calendar.HOUR_OF_DAY)
                if (hour >= 19 && hour <= 21) {
                    val floats: Array[Float] = EntityHelper.getPitchYawToEntity(skull.xCoord, skull.yCoord, skull.zCoord, Minecraft.getMinecraft.thePlayer)
                    GL11.glRotatef(floats(1) + 180F, 0F, 1F, 0F)
                    GL11.glRotatef(floats(0), 1F, 0F, 0F)
                    rot = 0F
                }
            }
            ItemSkullRenderer.modelSkull.render(null, 0F, 0F, 0F, rot, 0F, 0.0625F)
            GL11.glPopMatrix()
        }
        else super.func_152674_a(par1, par2, par3, par4, rot, par6, gameProfile)
    }
}