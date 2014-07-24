package kihira.foxlib.proxy

import kihira.foxlib.client.ClientEventHandler
import net.minecraftforge.common.MinecraftForge

class ClientProxy extends CommonProxy {

    override def registerEventHandlers() = {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler)
    }
}
