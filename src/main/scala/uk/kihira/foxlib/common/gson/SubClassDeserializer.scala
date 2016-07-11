/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package uk.kihira.foxlib.common.gson

import java.lang.reflect.Type

import com.google.gson._
import uk.kihira.foxlib.FoxLib

/**
 * A sub-class deserializer for use in Googles Gson. This is generic code that can work with most sub-classes so long as
 * the super class has the field <i>type</i> specified as the current class name
 */
class SubClassDeserializer[T] extends JsonDeserializer[T] {
    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T = {
        val jsonObject: JsonObject = json.getAsJsonObject
        val className: String = jsonObject.get("clazz").getAsString
        var clazz: Class[_] = null
        try {
            clazz = Class.forName(className)
            return new Gson().fromJson(jsonObject.toString, clazz).asInstanceOf[T]
        }
        catch {
            case e: ClassNotFoundException =>
                FoxLib.logger.error("Failed to deserialize " + jsonObject.toString, new JsonParseException(e.getMessage))
            case e: ClassCastException =>
                FoxLib.logger.error("Failed to deserialize, trying to deserialise into the wrong class " + jsonObject.toString, e)
        }
        null.asInstanceOf[T]
    }
}

