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

package kihira.foxlib.common

import net.minecraft.entity.Entity
import net.minecraft.util.MathHelper

object EntityHelper {

    def getPitchYawToPosition(sourceX:Double, sourceY:Double, sourceZ:Double, targetX:Double, targetY:Double, targetZ:Double): Array[Float] = {
        val xDiff: Double = targetX - sourceX
        val yDiff: Double = targetY - sourceY
        val zDiff: Double = targetZ - sourceZ
        val d3: Double = MathHelper.sqrt_double((xDiff * xDiff) + (zDiff * zDiff)).asInstanceOf[Double]
        val pitch: Float = (-(Math.atan2(yDiff, d3) * 180.0D / Math.PI)).asInstanceOf[Float]
        val yaw: Float = (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI).asInstanceOf[Float] - 90.0F

        Array[Float](pitch, yaw)
    }

    def getPitchYawToEntity(sourceX:Double, sourceY:Double, sourceZ:Double, targetEntity:Entity): Array[Float] = {
        getPitchYawToPosition(sourceX, sourceY, sourceZ, targetEntity.posX - targetEntity.width, targetEntity.posY - targetEntity.height +
            targetEntity.getEyeHeight.asInstanceOf[Double] + (if (targetEntity.worldObj.isRemote) 0.1F else 0.22F), targetEntity.posZ - targetEntity.width)
    }

    def getPitchYawToEntity(sourceEntity:Entity, targetEntity:Entity): Array[Float] = {
        getPitchYawToEntity(sourceEntity.posX, sourceEntity.posY, sourceEntity.posZ, targetEntity)
    }

    def updateRotation(currRot: Float, intendedRot: Float, maxInc: Float): Float = {
        var f3: Float = MathHelper.wrapAngleTo180_float(intendedRot - currRot)
        if (f3 > maxInc) f3 = maxInc
        if (f3 < -maxInc) f3 = -maxInc
        currRot + f3
    }
}
