package com.stealthyone.mcb.chatomizer.api.chatters;

import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import org.apache.commons.lang.Validate;
import org.bukkit.World;

public abstract class Chatter {

    private String name;
    private String displayName;
    private String worldName;

    public Chatter(String name, String displayName, World world) {
        Validate.notNull(name, "Name cannot be null.");

        this.name = name;
        this.displayName = (displayName == null) ? name : displayName;
        this.worldName = (world == null) ? null : world.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!this.getClass().isInstance(obj)) {
             return false;
        } else {
            return ((Chatter) obj).name.equals(name);
        }
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getWorldName() {
        return worldName;
    }

    public abstract ChatFormat getChatFormat();

    public abstract void sendMessage(String message);

}