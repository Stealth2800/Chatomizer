/*
 * Chatomizer - Advanced chat plugin with endless possibilities
 * Copyright (C) 2014 Stealth2800 <stealth2800@stealthyone.com>
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
package com.stealthyone.mcb.chatomizer.backend;

import com.stealthyone.mcb.chatomizer.Chatomizer;
import com.stealthyone.mcb.chatomizer.api.*;
import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.api.chatters.ChatterIdentifier;
import com.stealthyone.mcb.chatomizer.backend.chatters.*;
import com.stealthyone.mcb.chatomizer.api.modifiers.ChatModifier;
import com.stealthyone.mcb.stbukkitlib.logging.LogHelper;
import com.stealthyone.mcb.stbukkitlib.storage.YamlFileManager;
import com.stealthyone.mcb.stbukkitlib.utils.FileUtils;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ChatomizerChatManager implements ChatManager {

    private Chatomizer plugin;

    private File chatterDir;
    private Map<ChatterIdentifier, Chatter> loadedChatters = new HashMap<>();

    public ChatomizerChatManager(Chatomizer plugin) {
        this.plugin = plugin;
    }

    public void load() {
        chatterDir = new File(plugin.getDataFolder() + File.separator + "chatters");
        chatterDir.mkdir();
        reload();
    }

    public void reload() {
        if (!new File(chatterDir + File.separator + "console.yml").exists()) {
            try {
                FileUtils.copyFileFromJar(plugin, "defaultChatterFile.yml", new File(chatterDir + File.separator + "console.yml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (!loadedChatters.containsKey(new ConsoleChatterIdentifier())) {
            loadChatter(new ConsoleChatter(new YamlFileManager(chatterDir + File.separator + "console.yml")));
        } else {
            ((FileBasedChatter) loadedChatters.get(new ConsoleChatterIdentifier())).reload();
        }
    }

    public void save() {
        for (Chatter chatter : loadedChatters.values()) {
            saveChatter(chatter);
        }
    }

    public boolean loadPlayerChatter(Player player) {
        LogHelper.debug(plugin, "Load player chatter...");
        YamlFileManager file = new YamlFileManager(chatterDir + File.separator + player.getUniqueId().toString() + ".yml");
        if (file.isEmpty()) {
            try {
                FileUtils.copyFileFromJar(plugin, "defaultChatterFile.yml", file.getFile());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (loadedChatters.containsKey(new PlayerChatterIdentifier(player))) {
            return false;
        }

        return loadChatter(new PlayerChatter(file, player));
    }

    public boolean unloadPlayerChatter(Player player) {
        Chatter chatter = getPlayerChatter(player);
        saveChatter(chatter);
        loadedChatters.remove(chatter.getIdentifier());
        return true;
    }

    @Override
    public boolean loadChatter(Chatter chatter) {
        Validate.notNull(chatter, "Chatter cannot be null.");
        Validate.notNull(chatter.getIdentifier(), "Chatter's identifier cannot be null.");

        if (loadedChatters.containsKey(chatter.getIdentifier())) {
            return false;
        }
        loadedChatters.put(chatter.getIdentifier(), chatter);
        LogHelper.debug(plugin, "Loaded chatter '" + chatter.getIdentifier() + "'");
        return true;
    }

    @Override
    public void saveChatter(Chatter chatter) {
        Validate.notNull(chatter, "Chatter cannot be null.");

        chatter.save();
    }

    @Override
    public boolean unloadChatter(Chatter chatter) {
        Validate.notNull(chatter, "Chatter cannot be null.");

        if (!loadedChatters.containsKey(chatter.getIdentifier())) {
            return false;
        }
        saveChatter(chatter);
        loadedChatters.remove(chatter.getIdentifier());
        return true;
    }

    @Override
    public MultiChatEvent createConsoleChatEvent(String message) {
        Validate.notNull(message, "Message cannot be null.");

        MultiChatEvent event = new MultiChatEvent(getConsoleChatter(), message);
        handleMultiChatEvent(event);
        return event;
    }

    @Override
    public MultiChatEvent createChatEvent(Chatter sender, String message) {
        Validate.notNull(sender, "Sender cannot be null.");
        Validate.notNull(message, "Message cannot be null.");

        MultiChatEvent event = new MultiChatEvent(sender, message);
        handleMultiChatEvent(event);
        return event;
    }

    @Override
    public Chatter getConsoleChatter() {
        return loadedChatters.get(new ConsoleChatterIdentifier());
    }

    @Override
    public Chatter getPlayerChatter(Player player) {
        Validate.notNull(player, "Player cannot be null.");

        return loadedChatters.get(new PlayerChatterIdentifier(player));
    }

    @Override
    public Chatter getChatter(ChatterIdentifier identifier) {
        Validate.notNull(identifier, "Identifier cannot be null.");

        return loadedChatters.get(identifier);
    }

    public boolean isChatterRegistered(Chatter chatter) {
        Validate.notNull(chatter, "Chatter cannot be null.");
        return chatter.getIdentifier() != null && loadedChatters.containsKey(chatter.getIdentifier());
    }

    public void handleChatEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        handleMultiChatEvent(new MultiChatEvent(getPlayerChatter(e.getPlayer()), e.getMessage()));
    }

    private void handleMultiChatEvent(MultiChatEvent e) {
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return;
        }

        Set<Chatter> recipients = e.getRecipients();
        if (recipients.isEmpty()) return;

        Map<Chatter, String> finalMessages = new HashMap<>();
        Chatter sender = e.getSender();

        Map<String, String> genericModifications = new HashMap<>();
        Map<String, ChatModifier> specificModifications = new HashMap<>();
        for (Entry<String, ChatModifier> curModifier : plugin.getModifierManager().getRegisteredModifiers().entrySet()) {
            if (!curModifier.getValue().isRecipientSpecific()) {
                String replacement = curModifier.getValue().getReplacement(sender, null);
                if (replacement != null) {
                    genericModifications.put(curModifier.getKey(), replacement);
                }
            } else {
                specificModifications.put(curModifier.getKey(), curModifier.getValue());
            }
        }

        for (Chatter recipient : e.getRecipients()) {
            String finalMessage = e.getFormat(recipient).getGroupFormat(getChatterGroup(sender)).getFormat().replace("{MESSAGE}", e.getMessage(recipient));

            // Replace generic modifiers.
            for (Entry<String, String> genericMod : genericModifications.entrySet()) {
                if (finalMessage.contains(genericMod.getKey())) {
                    finalMessage = finalMessage.replace(genericMod.getKey(), genericMod.getValue());
                }
            }

            // Replace specific modifiers.
            for (Entry<String, ChatModifier> specificMod : specificModifications.entrySet()) {
                if (finalMessage.contains(specificMod.getKey())) {
                    String replacement = specificMod.getValue().getReplacement(sender, recipient);
                    if (replacement != null) {
                        finalMessage = finalMessage.replace(specificMod.getKey(), replacement);
                    }
                }
            }

            // Set final message.
            finalMessages.put(recipient, finalMessage);
        }

        for (Entry<Chatter, String> entry : finalMessages.entrySet()) {
            entry.getKey().sendMessage(entry.getValue());
        }
    }

    public String getChatterGroup(Chatter chatter) {
        Permission permission = plugin.getHookVault().getPermission();
        if (chatter instanceof PlayerChatter) {
            return permission == null ? plugin.getFormatManager().getDefaultGroup() : permission.getPrimaryGroup(((PlayerChatter) chatter).getPlayer());
        } else {
            return plugin.getFormatManager().getDefaultGroup();
        }
    }

}