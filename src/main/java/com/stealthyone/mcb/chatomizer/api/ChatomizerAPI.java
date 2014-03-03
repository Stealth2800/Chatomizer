/*
 * Chatomizer - Advanced chat plugin with endless possibilities
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
package com.stealthyone.mcb.chatomizer.api;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.backend.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.api.events.AsyncPlayerMultiChatEvent;
import com.stealthyone.mcb.chatomizer.backend.chatters.ChatterManager;
import com.stealthyone.mcb.chatomizer.backend.players.PlayerManager;
import com.stealthyone.mcb.chatomizer.backend.formats.FormatManager;
import com.stealthyone.mcb.chatomizer.config.ConfigHelper;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.chatomizer.utils.ChatColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public final class ChatomizerAPI {

    private ChatomizerAPI() { }

    /**
     * Register a chat modifier.
     *
     * @param chatModifier Chat modifier to register.
     * @return True if modifier registered successfully.
     *         False if modifier already registered.
     */
    public static boolean registerChatModifier(ChatModifier chatModifier) {
        return ChatomizerPlugin.getInstance().getModifierManager().registerModifier(chatModifier);
    }

    /**
     * Unregister a chat modifier.
     *
     * @param chatModifier Chat modifier to unregister.
     * @return True if modifier unregistered successfully.
     *         False if modifier not registered.
     */
    public static boolean unregisterChatModifier(ChatModifier chatModifier) {
        return ChatomizerPlugin.getInstance().getModifierManager().unregisterModifier(chatModifier);
    }

    public static AsyncPlayerMultiChatEvent createChatEvent(Chatter sender, String message) {
        ChatomizerPlugin plugin = ChatomizerPlugin.getInstance();

        FormatManager formatManager = plugin.getFormatManager();
        PlayerManager playerManager = plugin.getPlayerManager();

        Map<Chatter, ChatFormat> recipientFormats = new HashMap<>();
        recipientFormats.put(sender, sender.getChatFormat());

        boolean sendToConsole = ConfigHelper.LOG_CHAT.get();
        ChatFormat consoleFormat = formatManager.getFormat(ConfigHelper.CONSOLE_CHAT_FORMAT.get());

        ChatterManager chatterManager = plugin.getChatterManager();
        Set<Chatter> recipients = new HashSet<>();
        for (Player recipient : Bukkit.getOnlinePlayers()) {
            recipients.add(chatterManager.getChatter(recipient.getName()));
        }

        for (Chatter recipient : recipients) {
            recipientFormats.put(recipient, recipient.getChatFormat());
        }

        if (sender.hasPermission(PermissionNode.CHAT_COLOR.getPermission().getName()))
            message = ChatColorUtils.colorizeMessage(message);
        if (sender.hasPermission(PermissionNode.CHAT_FORMATTING.getPermission().getName()))
            message = ChatColorUtils.formatMessage(message);
        if (sender.hasPermission(PermissionNode.CHAT_MAGIC.getPermission().getName()))
            message = ChatColorUtils.magicfyMessage(message);

        AsyncPlayerMultiChatEvent multiChatEvent = new AsyncPlayerMultiChatEvent(sender, message, recipientFormats, sendToConsole, consoleFormat);

        Bukkit.getPluginManager().callEvent(multiChatEvent);

        return multiChatEvent;
    }

    public static AsyncPlayerMultiChatEvent createChatEvent(Chatter sender, String message, Set<Chatter> recipients) {
        ChatomizerPlugin plugin = ChatomizerPlugin.getInstance();

        FormatManager formatManager = plugin.getFormatManager();
        PlayerManager playerManager = plugin.getPlayerManager();

        Map<Chatter, ChatFormat> recipientFormats = new HashMap<>();
        recipientFormats.put(sender, sender.getChatFormat());

        boolean sendToConsole = ConfigHelper.LOG_CHAT.get();
        ChatFormat consoleFormat = formatManager.getFormat(ConfigHelper.CONSOLE_CHAT_FORMAT.get());

        for (Chatter recipient : recipients) {
            recipientFormats.put(recipient, recipient.getChatFormat());
        }

        if (sender.hasPermission(PermissionNode.CHAT_COLOR.getPermission().getName()))
            message = ChatColorUtils.colorizeMessage(message);
        if (sender.hasPermission(PermissionNode.CHAT_FORMATTING.getPermission().getName()))
            message = ChatColorUtils.formatMessage(message);
        if (sender.hasPermission(PermissionNode.CHAT_MAGIC.getPermission().getName()))
            message = ChatColorUtils.magicfyMessage(message);

        AsyncPlayerMultiChatEvent multiChatEvent = new AsyncPlayerMultiChatEvent(sender, message, recipientFormats, sendToConsole, consoleFormat);

        Bukkit.getPluginManager().callEvent(multiChatEvent);

        return multiChatEvent;
    }

    public static ChatFormat getChatFormat(String name) {
        return ChatomizerPlugin.getInstance().getFormatManager().getFormat(name);
    }

    public static UUID getPlayerUuid(String name) {
        return ChatomizerPlugin.getInstance().getPlayerManager().getUuid(name);
    }

    public static String getPlayerName(UUID uuid) {
        return ChatomizerPlugin.getInstance().getPlayerManager().getName(uuid);
    }

}