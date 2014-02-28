package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.config.ConfigHelper;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.bukkit.Bukkit;

public class ChatterConsole extends Chatter {

    public ChatterConsole(YamlFileManager file) {
        super(file, "CONSOLE", null);
    }

    @Override
    public String getWorldName() {
        return null;
    }

    @Override
    public ChatFormat getChatFormat() {
        return ChatomizerPlugin.getInstance().getFormatManager().getFormat(ConfigHelper.CONSOLE_CHAT_FORMAT.get());
    }

    @Override
    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return Bukkit.getConsoleSender().hasPermission(permission);
    }

}