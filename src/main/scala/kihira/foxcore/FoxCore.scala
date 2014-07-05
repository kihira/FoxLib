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

package kihira.foxcore

import cpw.mods.fml.common.Mod
import org.apache.logging.log4j.{LogManager, Logger}

@Mod(modid = FoxCore.MOD_ID, name = "FoxCore", version = "0.1.0", modLanguage = "scala")
object FoxCore {

    final val MOD_ID = "foxcore"
    final val logger: Logger = LogManager.getLogger(MOD_ID)
}
