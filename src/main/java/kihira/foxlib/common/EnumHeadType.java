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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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

    public static ItemStack getHead(EnumHeadType headType, String owner) {
        return getHead(headType.getID(), owner);
    }

    public static ItemStack getHead(int id, String owner) {
        ItemStack itemStack = new ItemStack(Items.skull, 1, id);
        if (owner != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("SkullOwner", owner);
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
}
