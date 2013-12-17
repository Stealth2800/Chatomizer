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
package com.stealthyone.mcb.chatomizer.backend.formats;

import com.stealthyone.mcb.chatomizer.api.Chatomizer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ChatFormat {

    private ConfigurationSection config;

    private Map<String, String> groupFormats = new HashMap<String, String>();

    public ChatFormat(ConfigurationSection config) {
        this.config = config;
        reloadGroupFormats();
    }

    public void reloadGroupFormats() {
        groupFormats.clear();
        ConfigurationSection formats = config.getConfigurationSection("formats");
        if (formats == null) return;
        for (String key : formats.getKeys(false)) {
            if (formats.getString(key) != null)
                groupFormats.put(key.toLowerCase(), formats.getString(key));
        }
    }

    @Override
    public boolean equals(Object object) {
        return !(object == null || !(object instanceof ChatFormat)) && ((ChatFormat) object).getName().equals(getName());
    }

    public String getName() {
        return config.getName();
    }

    public String getCreator() {
        return getCreator(false);
    }

    public String getCreator(boolean raw) {
        return config.getString("creator", raw ? null : ChatColor.DARK_RED + "<none>");
    }

    public String getDefaultFormat() {
        String defaultGroup = Chatomizer.formats.getDefaultGroup();
        String format = defaultGroup != null ? groupFormats.get(defaultGroup.toLowerCase()) : null;
        return format != null ? format : FormatManager.DEFAULT_FORMAT;
    }

    public String getFormat(String groupName) {
        String format = groupFormats.get(groupName.toLowerCase());
        return format != null ? format : getDefaultFormat();
    }

    public Map<String, String> getAllFormats() {
        return groupFormats;
    }

}