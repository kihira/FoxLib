/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */

package kihira.foxlib.common;

import com.mojang.authlib.GameProfile;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public enum EnumHeadType {

    NONE(-1),
    SKELETON(0),
    WITHERSKELETON(1),
    ZOMBIE(2),
    PLAYER(3),
    CREEPER(4);

    private int id;
    EnumHeadType(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public static ItemStack getHead(EnumHeadType headType, GameProfile owner) {
        return getHead(headType.getID(), owner);
    }

    public static ItemStack getHead(int id, GameProfile owner) {
        ItemStack itemStack = new ItemStack(Items.SKULL, 1, id);
        if (owner != null) {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound gameProfileTag = new NBTTagCompound();

            NBTUtil.writeGameProfile(gameProfileTag, owner);
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
}
