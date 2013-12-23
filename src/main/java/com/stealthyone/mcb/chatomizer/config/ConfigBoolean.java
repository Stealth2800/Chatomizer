package com.stealthyone.mcb.chatomizer.config;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;

public enum ConfigBoolean {

    DEBUG("Debug"),

    LOG_CHAT("Log chat to console");

    private String path;

    private ConfigBoolean(String path) {
        this.path = path;
    }

    public boolean get() {
        return ChatomizerPlugin.getInstance().getConfig().getBoolean(path);
    }

    public boolean get(boolean defaultValue) {
        return ChatomizerPlugin.getInstance().getConfig().getBoolean(path, defaultValue);
    }

}