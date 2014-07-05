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

package kihira.foxcore.common.gson

import java.lang.reflect.Type

import com.google.gson._
import kihira.foxcore.FoxCore

/**
 * A sub-class deserializer for use in Googles Gson. This is generic code that can work with most sub-classes so long as
 * the super class has the field <i>type</i> specified as the current class name
 */
class SubClassDeserializer[T] extends JsonDeserializer[T] {
    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T = {
        val jsonObject: JsonObject = json.getAsJsonObject
        val className: String = jsonObject.get("type").getAsString
        var clazz: Class[_] = null
        try {
            clazz = Class.forName(className)
            return new Gson().fromJson(jsonObject.toString, clazz).asInstanceOf[T]
        }
        catch {
            case e: ClassNotFoundException =>
                FoxCore.logger.error("Failed to deserialize " + jsonObject.toString, new JsonParseException(e.getMessage))
            case e: ClassCastException =>
                FoxCore.logger.error("Failed to deserialize, trying to deserialise into the wrong class " + jsonObject.toString, e)
        }
        null.asInstanceOf[T]
    }
}

