package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.apache.commons.lang.Validate;

public abstract class Chatter {

    private YamlFileManager file;

    public Chatter(YamlFileManager file) {
        Validate.notNull(file, "File must not be null.");

        this.file = file;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!this.getClass().isInstance(obj)) {
             return false;
        } else {
            return ((Chatter) obj).getName().equals(getName());
        }
    }

    public YamlFileManager getFile() {
        return file;
    }

    public abstract String getName();

    public abstract String getDisplayName();

    public abstract String getWorldName();

    public abstract ChatFormat getChatFormat();

    public abstract void sendMessage(String message);

    public abstract boolean hasPermission(String permission);

}