package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.bukkit.entity.Player;

public class ChatterPlayer extends Chatter {

    private Player player;

    public ChatterPlayer(YamlFileManager file, Player player) {
        super(file, player.getName(), player.getDisplayName());
        this.player = player;
    }

    @Override
    public String getWorldName() {
        return player.getWorld().getName();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public ChatFormat getChatFormat() {
        return ChatomizerPlugin.getInstance().getPlayerManager().getFormat(getName());
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

}