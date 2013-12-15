/*
 * Chatomizer - Basic chat plugin that allows players to choose what chat format they wish to use
 * Copyright (C) 2013 Stealth2800 <stealth2800@stealthyone.com>
 * Website: <http://stealthyone.com/bukkit>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.stealthyone.mcb.chatomizer.listeners;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.api.Chatomizer;
import com.stealthyone.mcb.chatomizer.backend.formats.ChatFormat;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.stbukkitlib.api.Stbl;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private ChatomizerPlugin plugin;

    public PlayerListener(ChatomizerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Chatomizer.players.loadPlayer(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Log.debug("Player chat");
        Player sender = e.getPlayer();
        String senderName = e.getPlayer().getName();
        boolean hasColorPerm = PermissionNode.CHAT_COLOR.isAllowed(sender);

        e.setCancelled(true);

        for (Player recipient : e.getRecipients()) {
            ChatFormat format = Chatomizer.players.getFormat(recipient);
            if (format == null) {
                recipient.sendMessage(ChatColor.RED + "An error occurred while receiving a chat message. Please contact an administrator to let them know about this.");
                return;
            }

            Log.debug("senderName is null: " + (senderName == null));
            Log.debug("message is null: " + (e.getMessage() == null));
            Log.debug("Vault is null: " + (Stbl.hooks.getVault() == null));
            Log.debug("Permission is null: " + (Stbl.hooks.getVault().getPermission() == null));
            Log.debug("group is null: " + (Stbl.hooks.getVault().getPermission().getPrimaryGroup(sender) == null));
            Log.debug("prefix is null: " + (Stbl.hooks.getVault().getChat().getPlayerPrefix(sender) == null));
            Log.debug("suffix is null: " + (Stbl.hooks.getVault().getChat().getPlayerSuffix(sender) == null));

            String finalMessage = format.getFormat()
                    .replace("{SENDER}", senderName)
                    .replace("{MESSAGE}", e.getMessage())
                    .replace("{GROUP}", Stbl.hooks.getVault().getPermission().getPrimaryGroup(sender))
                    .replace("{PREFIX}", Stbl.hooks.getVault().getChat().getPlayerPrefix(sender))
                    .replace("{SUFFIX}", Stbl.hooks.getVault().getChat().getPlayerSuffix(sender));

            recipient.sendMessage(hasColorPerm ? ChatColor.translateAlternateColorCodes('&', finalMessage) : finalMessage);
        }
    }

}