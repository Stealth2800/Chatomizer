package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.bukkit.OfflinePlayer;

public class ChatterOfflinePlayer extends Chatter {

    public ChatterOfflinePlayer(YamlFileManager file, String name, String displayName) {
        super(file, name, displayName);
    }

    @Override
    public String getWorldName() {
        OfflinePlayer player;
        return null;
    }

    @Override
    public ChatFormat getChatFormat() {
        return null;
    }

    @Override
    public void sendMessage(String message) { }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

}