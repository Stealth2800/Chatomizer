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
import com.stealthyone.mcb.chatomizer.api.formats.GroupFormat;
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
    private String defaultGroup = null;
    private Map<String, GroupFormat> groupFormats = new HashMap<>();

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
                groupFormats.put(groupName.toLowerCase(), new GroupFormat(groupName, format));
            }
        } catch (NullPointerException ex) {
            //No groups section, just add default
        }

        String defaultName = Chatomizer.getInstance().getFormatManager().getDefaultGroup();
        if (!groupFormats.containsKey(defaultName)) {
            groupFormats.put(defaultName.toLowerCase(), new GroupFormat(defaultName, "<{SNAME}> {MESSAGE}"));
        }
    }

    @Override
    public boolean equals(Object object) {
        return !(object == null || !(object instanceof ChatFormat)) && ((ChatFormat) object).getName().equalsIgnoreCase(getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public boolean isHidden() {
        return isHidden;
    }

    @Override
    public boolean checkPermission() {
        return checkPermission;
    }

    @Override
    public GroupFormat getDefaultGroupFormat() {
        String defaultGroup = Chatomizer.getInstance().getFormatManager().getDefaultGroup();
        GroupFormat format = this.defaultGroup != null ? groupFormats.get(this.defaultGroup.toLowerCase()) : groupFormats.get(defaultGroup);
        return format != null ? format : Chatomizer.getInstance().getFormatManager().getDefaultGroupFormat();
    }

    @Override
    public GroupFormat getGroupFormat(String groupName) {
        GroupFormat format = groupFormats.get(groupName.toLowerCase());
        return format != null ? format : getDefaultGroupFormat();
    }

    @Override
    public Map<String, GroupFormat> getAllGroupFormats() {
        return groupFormats;
    }

}