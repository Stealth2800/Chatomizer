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
package com.stealthyone.mcb.chatomizer.backend;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.api.Chatomizer;
import com.stealthyone.mcb.chatomizer.backend.formats.ChatFormat;
import com.stealthyone.mcb.stbukkitlib.api.Stbl;
import com.stealthyone.mcb.stbukkitlib.lib.storage.YamlFileManager;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

public class PlayerManager {

    private ChatomizerPlugin plugin;

    private YamlFileManager playerFile;
    private Map<String, String> playerFormats = new HashMap<String, String>(); //Player, format

    public PlayerManager(ChatomizerPlugin plugin) {
        this.plugin = plugin;
        playerFile = new YamlFileManager(plugin.getDataFolder() + File.separator + "data" + File.separator + "playerFormats.yml");
        Log.info("Loaded " + reloadPlayers() + " players.");
    }

    public void saveAll() {
        for (Entry<String, String> entry : playerFormats.entrySet()) {
            playerFile.getConfig().set(Stbl.playerIds.getUuid(entry.getKey()).toString(), entry.getValue());
        }
        playerFile.saveFile();
    }

    public int reloadPlayers() {
        playerFormats.clear();
        FileConfiguration config = playerFile.getConfig();
        List<String> invalid = new ArrayList<String>();
        for (String uuid : config.getKeys(false)) {
            if (!loadPlayer(UUID.fromString(uuid)))
                invalid.add(uuid);
        }
        if (invalid.size() > 0)
            Log.warning("Unable to load some UUIDs from playerFormats.yml - names not found. (" + invalid.toString().replace("[", "").replace("]", ")"));
        return playerFormats.size();
    }

    public boolean loadPlayer(UUID uuid) {
        String name = Stbl.playerIds.getName(uuid);
        if (name != null) {
            playerFormats.put(name.toLowerCase(), playerFile.getConfig().getString(name.toLowerCase(), "default"));
            return true;
        }
        return false;
    }

    public ChatFormat getFormat(Player player) {
        Validate.notNull(player);
        return getFormat(player.getName());
    }

    public ChatFormat getFormat(String playerName) {
        Validate.notNull(playerName);

        UUID id = Stbl.playerIds.getUuid(playerName);
        if (id == null) {
            Log.debug("getFormat, id is null");
            return Chatomizer.formats.getDefaultFormat();
        }
        Log.debug("getFormat, id: " + id.toString());
        return Chatomizer.formats.getFormat(playerFormats.get(playerName.toLowerCase()));
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
            UUID id = Stbl.playerIds.getUuid(playerName);
            playerFile.getConfig().set(id.toString(), format.getName());
            playerFormats.put(playerName.toLowerCase(), format.getName());
            return true;
        }
    }

}