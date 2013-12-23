package com.stealthyone.mcb.chatomizer.config;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;

public enum ConfigString {

    CONSOLE_CHAT_FORMAT("Console chat format");

    private String path;

    private ConfigString(String path) {
        this.path = path;
    }

    public String get() {
        return ChatomizerPlugin.getInstance().getConfig().getString(path);
    }

    public String get(String defaultValue) {
        return ChatomizerPlugin.getInstance().getConfig().getString(path, defaultValue);
    }

}