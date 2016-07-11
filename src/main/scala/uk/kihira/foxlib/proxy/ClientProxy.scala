/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package uk.kihira.foxlib.proxy

import uk.kihira.foxlib.client.ClientEventHandler
import net.minecraftforge.common.MinecraftForge

class ClientProxy extends CommonProxy {

    override def registerEventHandlers() = {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler)
    }
}
