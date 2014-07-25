package kihira.foxlib.proxy

import cpw.mods.fml.client.registry.ClientRegistry
import kihira.foxlib.FoxLib
import kihira.foxlib.client.ClientEventHandler
import kihira.foxlib.client.render.{BlockSkullRenderer, ItemSkullRenderer}
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.init.Items
import net.minecraft.tileentity.TileEntitySkull
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.MinecraftForge

class ClientProxy extends CommonProxy {

    override def registerEventHandlers() = {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler)
    }

    override def registerRenderers() = {
        if (FoxLib.fancySkulls) {
            TileEntityRendererDispatcher.instance.mapSpecialRenderers.remove(classOf[TileEntitySkull])
            ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntitySkull], BlockSkullRenderer)
            MinecraftForgeClient.registerItemRenderer(Items.skull, ItemSkullRenderer)
        }
    }
}
