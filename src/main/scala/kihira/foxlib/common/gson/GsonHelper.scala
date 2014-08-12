/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.common.gson

import com.google.gson.{Gson, GsonBuilder}

object GsonHelper {

    /**
     * Creates a [[Gson]] instance with a [[com.google.gson.TypeAdapter]] registered
     * @param classes: [[Class]] The classes to register a [[SubClassDeserializer]] for
     * @tparam T; [[Any]] Not used
     * @return A [[Gson]] instance
     */
    def createGson[T](classes: Class[T]*): Gson = {
        val gsonBuilder: GsonBuilder = new GsonBuilder
        if (classes != null && classes.length > 0) {
            for (value <- classes) {
                if (value != null) gsonBuilder.registerTypeAdapter(value, new SubClassDeserializer[value.type])
            }
        }
        gsonBuilder.create
    }
    /**
     * Creates a [[Gson]] instance
     * * @return A [[Gson]] instance
     */
    def createGson(): Gson = {
        this.createGson(null)
    }

    /**
     * Creates a [[Gson]] instance with a [[com.google.gson.TypeAdapter]] registered. This is mostly for Java compat
     * @param clazz: [[Class]] The class to register a [[SubClassDeserializer]] for
     * @tparam T; [[Any]] Not used
     * @return A [[Gson]] instance
     */
    def createGson[T](clazz: Class[T]): Gson = {
        this.createGson(clazz, null) //Lets specify null so it doesn't call itself!
    }

    def toJson(obj: Any): String = {
        this.createGson(obj.getClass).toJson(obj)
    }

    def fromJson[T](json: String, clazz: Class[T]): T ={
        this.createGson(clazz).fromJson(json, clazz)
    }
}
