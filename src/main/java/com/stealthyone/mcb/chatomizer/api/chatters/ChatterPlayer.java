package com.stealthyone.mcb.chatomizer.api.chatters;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import org.bukkit.entity.Player;

public class ChatterPlayer extends Chatter {

    private Player player;

    public ChatterPlayer(Player player) {
        super(player.getName(), player.getDisplayName(), player.getWorld());
        this.player = player;
    }

    @Override
    public ChatFormat getChatFormat() {
        return ChatomizerPlugin.getInstance().getPlayerManager().getFormat(player);
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