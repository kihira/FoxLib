package kihira.foxlib.client.model

import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.entity.Entity

class ModelSkull(texWidth: Int = 64, texHeight: Int = 32) extends ModelBase {
    textureHeight = texHeight
    textureWidth = texWidth

    private final val bipedHead: ModelRenderer = new ModelRenderer(this, 0, 0)
    private final val bipedHeadwear: ModelRenderer = new ModelRenderer(this, 32, 0)

    this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0F)
    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F)
    this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F)
    this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F)

    def renderWithoutRotation(par1: Float) {
        this.bipedHead.render(par1)
        this.bipedHeadwear.render(par1)
    }

    override def render(par1Entity: Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, par7: Float) {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity)
        this.bipedHead.render(par7)
        this.bipedHeadwear.render(par7)
    }

    override def setRotationAngles(par1: Float, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, par7Entity: Entity) {
        this.bipedHead.rotateAngleY = {
            this.bipedHeadwear.rotateAngleY = par4 / (180F / Math.PI.asInstanceOf[Float])
            this.bipedHeadwear.rotateAngleY
        }
        this.bipedHead.rotateAngleX = {
            this.bipedHeadwear.rotateAngleX = par5 / (180F / Math.PI.asInstanceOf[Float])
            this.bipedHeadwear.rotateAngleX
        }
    }
}