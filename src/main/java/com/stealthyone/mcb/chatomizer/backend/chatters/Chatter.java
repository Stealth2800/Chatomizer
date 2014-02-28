package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.apache.commons.lang.Validate;

public abstract class Chatter {

    private YamlFileManager file;

    private String name;
    private String displayName;

    public Chatter(YamlFileManager file, String name, String displayName) {
        Validate.notNull(name, "Name cannot be null.");

        this.file = file;
        this.name = name;
        this.displayName = (displayName == null) ? name : displayName;
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

    public YamlFileManager getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract String getWorldName();

    public abstract ChatFormat getChatFormat();

    public abstract void sendMessage(String message);

    public abstract boolean hasPermission(String permission);

}