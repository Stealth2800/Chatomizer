package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatterManager {

    private ChatomizerPlugin plugin;

    private File chatterDir;

    private Chatter consoleChatter;
    private Map<String, Chatter> loadedChatters = new HashMap<>();

    public ChatterManager(ChatomizerPlugin plugin) {
        this.plugin = plugin;

        chatterDir = new File(plugin.getDataFolder() + File.separator + "chatters");
        if (chatterDir.mkdir()) {
            Log.info("Unable to find 'chatters' directory, creating it for you now.");
        }

        loadChatter("console");
    }

    public void loadChatter(String name) {
        Validate.notNull(name, "Name must not be null.");

        if (!name.equalsIgnoreCase("console")) {
            UUID uuid = plugin.getPlayerManager().getUuid(name);
            if (uuid == null) {
                Log.warning("Unable to load player chatter '" + name + "' -> missing UUID mapping.");
                return;
            }
            loadChatter(uuid);
            return;
        }

        File file = new File(chatterDir + File.separator + name + ".yml");
        if (!file.exists()) {
            Log.warning("Unable to load chatter '" + name + "' -> no chatter file found.");
            return;
        }

        consoleChatter = new ChatterConsole(new YamlFileManager(file));
        Log.info("Loaded CONSOLE chatter.");
    }

    public void loadChatter(UUID playerUuid) {
        Validate.notNull(playerUuid, "Player UUID must not be null.");

        String name = plugin.getPlayerManager().getName(playerUuid);
        if (name == null) {
            Log.warning("Unable to load player chatter with UUID '" + playerUuid + "' -> missing name mapping.");
            return;
        }

        File file = new File(chatterDir + File.separator + playerUuid.toString() + ".yml");
        if (!file.exists()) {
            Log.warning("Unable to load player chatter with UUID '" + playerUuid + "' -> file not found");
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(name);

        //Chatter chatter = (player.isOnline()) ? new ChatterPlayer(new YamlFileManager(file), player.getPlayer()) : new ChatterOfflinePlayer(file);
    }

    public Chatter getChatter(String name) {
        return (name.equalsIgnoreCase("console")) ? consoleChatter : loadedChatters.get(name.toLowerCase());
    }

}