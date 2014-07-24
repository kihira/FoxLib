/*
 * Copyright (C) 2014  Kihira
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package kihira.foxlib

import java.io.File

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.{Mod, SidedProxy}
import kihira.foxlib.proxy.CommonProxy
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = FoxLib.MOD_ID, name = "FoxLib", version = "0.1.0", useMetadata = true, modLanguage = "scala")
object FoxLib {

    final val MOD_ID = "foxlib"
    final val logger: Logger = LogManager.getLogger(MOD_ID)

    var showCollisionBoxes: Boolean = false

    @SidedProxy(clientSide = "kihira.foxlib.proxy.ClientProxy", serverSide = "kihira.foxlib.proxy.CommonProxy")
    var proxy: CommonProxy = null

    @EventHandler
    def onPreInit(e: FMLPreInitializationEvent) {
        loadConfig(e.getSuggestedConfigurationFile)
        proxy.registerEventHandlers()
    }

    private def loadConfig(file: File) = {
        val config: Configuration = new Configuration(file)
        showCollisionBoxes = config.getBoolean("Show collision boxes", "client", false, "Shows the collision boxes for blocks")

        if (config.hasChanged) config.save()
    }
}
