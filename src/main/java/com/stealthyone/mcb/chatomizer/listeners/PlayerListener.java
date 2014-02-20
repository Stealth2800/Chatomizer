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
import com.stealthyone.mcb.chatomizer.api.ChatModifier;
import com.stealthyone.mcb.chatomizer.api.ChatomizerAPI;
import com.stealthyone.mcb.chatomizer.api.events.AsyncPlayerMultiChatEvent;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PlayerListener implements Listener {

    private ChatomizerPlugin plugin;

    public PlayerListener(ChatomizerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getPlayerManager().updateUuid(e.getPlayer());
        plugin.getPlayerManager().loadPlayer(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) {
            return;
        } else {
            e.setCancelled(true);
        }

        ChatomizerAPI.createChatEvent(e.getPlayer(), e.getMessage(), e.getRecipients());
    }

    @EventHandler
    public void onPlayerMultiChat(AsyncPlayerMultiChatEvent e) {
        Player sender = e.getPlayer();

        Map<String, String> genericModifications = new HashMap<>();
        Map<String, ChatModifier> specificModifications = new HashMap<>();
        for (Entry<String, ChatModifier> curModifier : plugin.getModifierManager().getRegisteredModifiers().entrySet()) {
            if (!curModifier.getValue().isRecipientSpecific()) {
                genericModifications.put(curModifier.getKey(), curModifier.getValue().getReplacement(sender, null));
            } else {
                specificModifications.put(curModifier.getKey(), curModifier.getValue());
            }
        }

        String senderGroup = plugin.getHookVault().getPermission().getPrimaryGroup(sender);

        for (Entry<Player, ChatFormat> eRecipient : e.getRecipients().entrySet()) {
            Player ePlayer = eRecipient.getKey();
            String eMessage = e.getPlayerMessage(ePlayer);
            if (eMessage != null && !eMessage.equals("")) {
                ChatFormat format = eRecipient.getValue();
                String finalMessage = format.getFormat(senderGroup).replace("{MESSAGE}", eMessage);

                // Replace generic modifiers.
                for (Entry<String, String> genericMod : genericModifications.entrySet()) {
                    if (finalMessage.contains(genericMod.getKey())) {
                        finalMessage = finalMessage.replace(genericMod.getKey(), genericMod.getValue());
                    }
                }

                // Replace specific modifiers.
                for (Entry<String, ChatModifier> specificMod : specificModifications.entrySet()) {
                    if (finalMessage.contains(specificMod.getKey())) {
                        finalMessage = finalMessage.replace(specificMod.getKey(), specificMod.getValue().getReplacement(sender, ePlayer));
                    }
                }

                // Send message.
                ePlayer.sendMessage(finalMessage);
            }
        }
    }

}