/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.common

class Loc4(val dimID: Int, val posX: Double, val posY: Double, val posZ: Double) {
    import java.lang.Double

    override def equals(o: Any): Boolean = {
        if (o == null || getClass != o.getClass) return false
        val loc4: Loc4 = o.asInstanceOf[Loc4]
        this.dimID == loc4.dimID && Double.compare(loc4.posX, this.posX) == 0 && Double.compare(loc4.posY, this.posY) == 0 && Double.compare(loc4.posZ, this.posZ) == 0
    }

    override def hashCode: Int = {
        var result: Int = 0
        var temp: Long = 0L
        result = this.dimID
        temp = Double.doubleToLongBits(this.posX)
        result = 31 * result + (temp ^ (temp >>> 32)).asInstanceOf[Int]
        temp = Double.doubleToLongBits(this.posY)
        result = 31 * result + (temp ^ (temp >>> 32)).asInstanceOf[Int]
        temp = Double.doubleToLongBits(this.posZ)
        result = 31 * result + (temp ^ (temp >>> 32)).asInstanceOf[Int]
        result
    }
}
