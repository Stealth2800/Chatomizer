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
import com.stealthyone.mcb.stbukkitlib.storage.YamlFileManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ChatFormatFile implements ChatFormat {

    private String name;
    private String creator = "" + ChatColor.RED + ChatColor.ITALIC + "<unknown>";
    private boolean isHidden = false;
    private boolean checkPermission = false;
    private Map<String, String> groupFormats = new HashMap<String, String>();

    public ChatFormatFile(YamlFileManager file) {
        FileConfiguration config = file.getConfig();

        this.name = config.getString("name");
        if (name == null) {
            throw new RuntimeException("No name is set in the configuration file!");
        }
        this.creator = config.getString("creator", creator);
        this.isHidden = config.getBoolean("hidden", isHidden);
        this.checkPermission = config.getBoolean("permission", checkPermission);
        try {
            for (String groupName : config.getConfigurationSection("groups").getKeys(false)) {
                String format = config.getString("groups." + groupName);
                if (format == null) {
                    //null, bypass
                    continue;
                }
                groupFormats.put(groupName.toLowerCase(), format);
            }
        } catch (NullPointerException ex) {
            //No groups section, just add default
        }

        String defaultName = Chatomizer.getInstance().getFormatManager().getDefaultGroup();
        if (!groupFormats.containsKey(defaultName)) {
            groupFormats.put(defaultName, "<{SNAME}> {MESSAGE}");
        }
    }

    @Override
    public boolean equals(Object object) {
        return !(object == null || !(object instanceof ChatFormat)) && ((ChatFormat) object).getName().equalsIgnoreCase(getName());
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean checkPermission() {
        return checkPermission;
    }

    public String getDefaultFormat() {
        String defaultGroup = Chatomizer.getInstance().getFormatManager().getDefaultGroup();
        String format = defaultGroup != null ? groupFormats.get(defaultGroup.toLowerCase()) : null;
        return format != null ? format : FormatManager.DEFAULT_FORMAT;
    }

    public String getGroupFormat(String groupName) {
        String format = groupFormats.get(groupName.toLowerCase());
        return ChatColor.translateAlternateColorCodes('&', format != null ? format : getDefaultFormat());
    }

    public Map<String, String> getAllFormats() {
        return groupFormats;
    }

}