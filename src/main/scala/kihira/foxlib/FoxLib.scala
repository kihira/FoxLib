/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib

import java.io.File

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.{Mod, SidedProxy}
import kihira.foxlib.proxy.CommonProxy
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = FoxLib.MOD_ID, name = "FoxLib", version = "0.3.0", modLanguage = "scala")
object FoxLib {

    final val MOD_ID = "foxlib"
    final val logger: Logger = LogManager.getLogger(MOD_ID)

    var showCollisionBoxes: Boolean = false
    var fancySkulls: Boolean = true

    @SidedProxy(clientSide = "kihira.foxlib.proxy.ClientProxy", serverSide = "kihira.foxlib.proxy.CommonProxy")
    var proxy: CommonProxy = null

    @EventHandler
    def onPreInit(e: FMLPreInitializationEvent) {
        loadConfig(e.getSuggestedConfigurationFile)
        proxy.registerEventHandlers()
        proxy.registerRenderers()
    }

    private def loadConfig(file: File) = {
        val config: Configuration = new Configuration(file)
        showCollisionBoxes = config.getBoolean("Show collision boxes", "client", false, "Shows the collision boxes for blocks")
        fancySkulls = config.getBoolean("Fancy skull rendering", "client", true, "Renders player heads with the hair layer and 3D in inventory")

        if (config.hasChanged) config.save()
    }
}
