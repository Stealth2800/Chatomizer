package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

            String playerName = plugin.getPlayerManager().getName(uuid);
            if (playerName == null) {
                Log.warning("Unable to load player chatter with UUID '" + uuid + "' -> missing name mapping.");
                return;
            }
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null) {
                Log.warning("Unable to load player chatter with UUID '" + uuid + "' -> player not online.");
                return;
            }

            loadChatter(player);
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

    public void loadChatter(Player player) {
        Validate.notNull(player, "Player must not be null.");

        UUID playerUuid = player.getUniqueId();
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

        Chatter chatter = new ChatterPlayer(new YamlFileManager(file), player.getPlayer());
        loadedChatters.put(name, chatter);
    }

    public Chatter getChatter(String name) {
        return (name.equalsIgnoreCase("console")) ? consoleChatter : loadedChatters.get(name.toLowerCase());
    }

}