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
package com.stealthyone.mcb.chatomizer.backend;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class PlayerManager {

    private ChatomizerPlugin plugin;

    private YamlFileManager playerIdFile;
    private Map<UUID, String> uuidToNames = new HashMap<>();
    private Map<String, UUID> nameToUuids = new HashMap<>();

    private YamlFileManager playerFile;
    private Map<String, String> playerFormats = new HashMap<>(); //UUID, format

    public PlayerManager(ChatomizerPlugin plugin) {
        this.plugin = plugin;
        playerIdFile = new YamlFileManager(plugin.getDataFolder() + File.separator + "data" + File.separator + "playerUuids.yml");

        playerFile = new YamlFileManager(plugin.getDataFolder() + File.separator + "data" + File.separator + "playerFormats.yml");
        Log.info("Loaded " + reloadPlayers() + " players.");
    }

    public void saveAll() {
        FileConfiguration playerConfig = playerFile.getConfig();
        for (Entry<String, String> entry : playerFormats.entrySet()) {
            playerConfig.set(entry.getKey(), entry.getValue());
        }
        playerFile.saveFile();
    }

    public int reloadUuids() {
        playerIdFile.reloadConfig();
        uuidToNames.clear();
        nameToUuids.clear();

        FileConfiguration config = playerIdFile.getConfig();
        for (String rawUuid : config.getKeys(false)) {
            UUID uuid = UUID.fromString(rawUuid);
            String name = config.getString(rawUuid);
            uuidToNames.put(uuid, name);
            nameToUuids.put(name.toLowerCase(), uuid);
        }
        return uuidToNames.size();
    }

    public String getName(UUID uuid) {
        return uuidToNames.get(uuid);
    }

    public UUID getUuid(String name) {
        return nameToUuids.get(name.toLowerCase());
    }

    public int reloadPlayers() {
        playerFile.reloadConfig();
        playerFormats.clear();
        FileConfiguration config = playerFile.getConfig();
        for (String uuid : config.getKeys(false)) {
            loadPlayer(UUID.fromString(uuid));
        }
        return playerFormats.size();
    }

    public void loadPlayer(UUID uuid) {
        playerFormats.put(uuid.toString(), playerFile.getConfig().getString(uuid.toString(), "<default>"));
    }

    public ChatFormat getFormat(Player player) {
        Validate.notNull(player);

        return getFormat(player.getName());
    }

    public ChatFormat getFormat(String playerName) {
        Validate.notNull(playerName);

        UUID id = getUuid(playerName);
        if (id == null) {
            return plugin.getFormatManager().getDefaultFormat();
        }
        String name = playerFormats.get(id.toString());
        return name.equalsIgnoreCase("<default>") ? plugin.getFormatManager().getDefaultFormat() : plugin.getFormatManager().getFormat(name);
    }

    public boolean setFormat(Player player, ChatFormat format) {
        Validate.notNull(player);

        return setFormat(player.getName(), format);
    }

    public boolean setFormat(String playerName, ChatFormat format) {
        Validate.notNull(playerName);
        Validate.notNull(format);

        if (getFormat(playerName).equals(format)) {
            return false;
        } else {
            String formatName = format.equals(plugin.getFormatManager().getDefaultFormat()) ? "<default>" : format.getName();
            UUID id = getUuid(playerName);
            playerFormats.put(id.toString(), formatName);
            return true;
        }
    }

}