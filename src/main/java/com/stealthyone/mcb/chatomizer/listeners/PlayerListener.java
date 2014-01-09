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
import com.stealthyone.mcb.chatomizer.backend.formats.ChatFormat;
import com.stealthyone.mcb.chatomizer.config.ConfigHelper;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.stbukkitlib.api.Stbl;
import com.stealthyone.mcb.stbukkitlib.lib.utils.ChatColorUtils;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    private ChatomizerPlugin plugin;

    public PlayerListener(ChatomizerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getPlayerManager().loadPlayer(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player sender = e.getPlayer();
        String senderName = e.getPlayer().getName();
        String displayName = e.getPlayer().getDisplayName();
        String message = e.getMessage();
        Chat chat = Stbl.hooks.getVault().getChat();
        String senderGroup = Stbl.hooks.getVault().getPermission().getPrimaryGroup(sender);
        String senderPrefix = chat.getPlayerPrefix(sender);
        String senderSuffix = chat.getPlayerSuffix(sender);
        boolean hasColorPerm = PermissionNode.CHAT_COLOR.isAllowed(sender);
        boolean hasFormatPerm = PermissionNode.CHAT_FORMATTING.isAllowed(sender);
        boolean hasMagicPerm = PermissionNode.CHAT_MAGIC.isAllowed(sender);

        e.setCancelled(true);

        List<CommandSender> recipients = new ArrayList<CommandSender>();
        recipients.addAll(e.getRecipients());
        if (ConfigHelper.LOG_CHAT.get()) {
            recipients.add(Bukkit.getConsoleSender());
        }

        for (CommandSender recipient : recipients) {
            sendMessage(recipient, recipient instanceof ConsoleCommandSender ? plugin.getFormatManager().getFormat(ConfigHelper.CONSOLE_CHAT_FORMAT.get()) : plugin.getPlayerManager().getFormat(recipient.getName()), senderName, displayName, message, senderGroup, senderPrefix, senderSuffix, hasColorPerm, hasFormatPerm, hasMagicPerm);
        }
    }

    private void sendMessage(CommandSender recipient, ChatFormat format, String senderName, String displayName, String message, String senderGroup, String senderPrefix, String senderSuffix, boolean hasColorPerm, boolean hasFormatPerm, boolean hasMagicPerm) {
        if (format == null) {
            recipient.sendMessage(ChatColor.RED + "An error occurred while receiving a chat message. Please contact an administrator to let them know about this.");
            return;
        }

        String finalMessage = format.getFormat(senderGroup)
                .replace("{SENDER}", senderName)
                .replace("{USERNAME}", senderName)
                .replace("{DISPLAYNAME}", displayName)
                .replace("{MESSAGE}", message)
                .replace("{GROUP}", senderGroup)
                .replace("{PREFIX}", senderPrefix)
                .replace("{SUFFIX}", senderSuffix);

        if (hasColorPerm)
            finalMessage = ChatColorUtils.colorizeMessage(finalMessage);
        if (hasFormatPerm)
            finalMessage = ChatColorUtils.formatMessage(finalMessage);
        if (hasMagicPerm)
            finalMessage = ChatColorUtils.magicfyMessage(finalMessage);

        recipient.sendMessage(finalMessage);
    }

}