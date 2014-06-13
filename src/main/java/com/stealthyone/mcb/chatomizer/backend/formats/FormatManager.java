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
package com.stealthyone.mcb.chatomizer.backend.formats;

import com.stealthyone.mcb.chatomizer.Chatomizer;
import com.stealthyone.mcb.chatomizer.api.formats.ChatFormat;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.stbukkitlib.logging.LogHelper;
import com.stealthyone.mcb.stbukkitlib.storage.YamlFileManager;
import com.stealthyone.mcb.stbukkitlib.utils.FileUtils;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatManager {

    public final static String DEFAULT_FORMAT = "<{SENDER}> {MESSAGE}";

    private Chatomizer plugin;

    private String defaultFormat;
    private String defaultGroup;

    private File formatDir;
    private Map<String, ChatFormat> loadedFormats = new HashMap<>();

    public FormatManager(Chatomizer plugin) {
        this.plugin = plugin;
    }

    public void load() {
        formatDir = new File(plugin.getDataFolder() + File.separator + "formats");
        formatDir.mkdir();
        reload();
    }

    public void reload() {
        if (formatDir.listFiles().length == 0) {
            try {
                FileUtils.copyFileFromJar(plugin, "defaultFormat.yml", new File(formatDir + File.separator + "default.yml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        defaultFormat = plugin.getConfig().getString("Default format", "default");
        defaultGroup = plugin.getConfig().getString("Default group", "default");

        for (File file : formatDir.listFiles()) {
            if (file.getName().matches("[a-z0-9]+.yml")) {
                loadFormat(new YamlFileManager(file));
            }
        }
    }

    public void save() {
        plugin.getConfig().set("Default format", defaultFormat);
        plugin.getConfig().set("Default group", defaultGroup);
    }

    public boolean loadFormat(YamlFileManager file) {
        ChatFormatFile format;
        try {
            format = new ChatFormatFile(file);
        } catch (Exception ex) {
            plugin.getLogger().warning("Unable to load format from file: '" + file.getName() + "' - " + ex.getMessage());
            return false;
        }
        loadedFormats.put(format.getName().toLowerCase(), format);
        LogHelper.debug(plugin, "Loaded chat format '" + format.getName() + "'");
        return true;
    }

    public ChatFormat getFormat(String name) {
        ChatFormat format = loadedFormats.get(name.toLowerCase());
        if (format == null) {
            format = getDefaultFormat();
        }
        return format;
    }

    public String getDefaultFormatName() {
        return defaultFormat;
    }

    public ChatFormat getDefaultFormat() {
        return getFormat(getDefaultFormatName());
    }

    public boolean setDefaultFormat(ChatFormat format) {
        String formatName = format.getName();
        if (!formatName.equalsIgnoreCase(getDefaultFormat().getName())) {
            defaultFormat = formatName;
            return true;
        }
        return false;
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public boolean doesFormatExist(String name) {
        return name.equalsIgnoreCase("default") || loadedFormats.containsKey(name.toLowerCase());
    }

    public Map<String, ChatFormat> getLoadedFormats() {
        return loadedFormats;
    }

    public List<ChatFormat> getAllowedFormats(CommandSender sender) {
        List<ChatFormat> returnList = new ArrayList<>();
        for (ChatFormat format : loadedFormats.values()) {
            if (!format.checkPermission() || (format.checkPermission() && PermissionNode.FORMATS.isAllowed(sender, format.getName().toLowerCase())))
                returnList.add(format);
        }
        return returnList;
    }

}