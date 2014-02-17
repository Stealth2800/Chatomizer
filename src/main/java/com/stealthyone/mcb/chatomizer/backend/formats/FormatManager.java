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
package com.stealthyone.mcb.chatomizer.backend.formats;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.chatomizer.utils.FileUtils;
import com.stealthyone.mcb.chatomizer.utils.YamlFileManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FormatManager {

    public final static String DEFAULT_FORMAT = "<{SENDER}> {MESSAGE}";

    private ChatomizerPlugin plugin;

    private YamlFileManager formatFile;
    private Map<String, ChatFormat> loadedFormats = new LinkedHashMap<String, ChatFormat>();

    public FormatManager(ChatomizerPlugin plugin) {
        this.plugin = plugin;
        formatFile = new YamlFileManager(plugin.getDataFolder() + File.separator + "chatFormats.yml");
        if (formatFile.isEmpty()) {
            try {
                FileUtils.copyFileFromJar(plugin, "chatFormats.yml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Log.info("Loaded " + reloadFormats() + " chat formats.");
    }

    public int reloadFormats() {
        formatFile.reloadConfig();
        loadedFormats.clear();
        FileConfiguration config = formatFile.getConfig();
        for (String name : config.getKeys(false)) {
            loadFormat(name);
        }

        if (!loadedFormats.containsKey("default")) {
            Log.info("Default chat format not found, creating now.");
            config.set("default.format", DEFAULT_FORMAT);
            loadFormat("default");
        }

        return loadedFormats.size();
    }

    public ChatFormat loadFormat(String name) {
        ConfigurationSection config = formatFile.getConfig().getConfigurationSection(name);
        if (config != null && config.getKeys(false).size() != 0) {
            ChatFormat format = new ChatFormat(config);
            loadedFormats.put(format.getName().toLowerCase(), format);
            return format;
        }
        return null;
    }

    public ChatFormat getFormat(String name) {
        ChatFormat format = name.equalsIgnoreCase("<default>") ? getDefaultFormat() : loadedFormats.get(name.toLowerCase());
        if (format == null) {
            if (name.equalsIgnoreCase(getDefaultFormatName())) {
                formatFile.getConfig().set(name.toLowerCase() + ".format", DEFAULT_FORMAT);
                loadFormat(name);
            } else {
                format = getDefaultFormat();
            }
        }
        return format;
    }

    public String getDefaultFormatName() {
        return formatFile.getConfig().getString("Default format", "default");
    }

    public ChatFormat getDefaultFormat() {
        return getFormat(getDefaultFormatName());
    }

    public boolean setDefaultFormat(ChatFormat format) {
        String formatName = format.getName();
        if (!formatName.equalsIgnoreCase(getDefaultFormat().getName())) {
            formatFile.getConfig().set("Default format", formatName);
            return true;
        }
        return false;
    }

    public String getDefaultGroup() {
        return formatFile.getConfig().getString("Default group", "default");
    }

    public boolean doesFormatExist(String name) {
        return name.equalsIgnoreCase("<default>") || loadedFormats.containsKey(name.toLowerCase());
    }

    public Map<String, ChatFormat> getLoadedFormats() {
        return loadedFormats;
    }

    public List<ChatFormat> getAllowedFormats(CommandSender sender) {
        List<ChatFormat> returnList = new ArrayList<ChatFormat>();
        for (ChatFormat format : loadedFormats.values()) {
            if (PermissionNode.STYLES.isAllowed(sender, format.getName().toLowerCase()))
                returnList.add(format);
        }
        return returnList;
    }

}