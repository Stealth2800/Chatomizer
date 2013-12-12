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

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class ChatFormat {

    private ConfigurationSection config;

    public ChatFormat(ConfigurationSection config) {
        this.config = config;
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
        return config.getString("creator", raw ? null : ChatColor.RED + "<none>");
    }

    public String getFormat() {
        return getFormat(false);
    }

    public String getFormat(boolean raw) {
        return raw ? config.getString("format") : ChatColor.translateAlternateColorCodes('&', config.getString("format"));
    }

}