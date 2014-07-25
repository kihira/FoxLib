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

package kihira.foxlib.common;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public enum EnumHeadType {

    NONE(-1),
    SKELETON(0),
    WITHERSKELETON(1),
    ZOMBIE(2),
    PLAYER(3),
    CREEPER(4);

    private int id;
    private EnumHeadType(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public static ItemStack getHead(EnumHeadType headType, GameProfile owner) {
        return getHead(headType.getID(), owner);
    }

    public static ItemStack getHead(int id, GameProfile owner) {
        ItemStack itemStack = new ItemStack(Items.skull, 1, id);
        if (owner != null) {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound gameProfileTag = new NBTTagCompound();

            NBTUtil.func_152460_a(gameProfileTag, refreshGameProfile(owner));
            tag.setTag("SkullOwner", gameProfileTag);
            itemStack.setTagCompound(tag);
        }
        return itemStack;
    }

    public static EnumHeadType fromId(int id) {
        switch (id) {
            case 0: return EnumHeadType.SKELETON;
            case 1: return EnumHeadType.WITHERSKELETON;
            case 2: return EnumHeadType.ZOMBIE;
            case 3: return EnumHeadType.PLAYER;
            case 4: return EnumHeadType.CREEPER;
            default: return EnumHeadType.NONE;
        }
    }

    private static GameProfile refreshGameProfile(GameProfile profile) {
        if (profile != null && !StringUtils.isNullOrEmpty(profile.getName())) {
            if (!profile.isComplete() || !profile.getProperties().containsKey("textures")) {
                //This would always need to get textures as textures aren't saved client side
                GameProfile gameprofile = MinecraftServer.getServer().func_152358_ax().func_152655_a(profile.getName());
                if (gameprofile != null) {
                    Property property = (Property) Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object) null);
                    if (property == null) gameprofile = MinecraftServer.getServer().func_147130_as().fillProfileProperties(gameprofile, true);

                    profile = gameprofile;
                }
            }
        }
        return profile;
    }
}
