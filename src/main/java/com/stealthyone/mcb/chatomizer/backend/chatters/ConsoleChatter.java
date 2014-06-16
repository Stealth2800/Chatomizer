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
package com.stealthyone.mcb.chatomizer.backend.chatters;

import com.stealthyone.mcb.stbukkitlib.storage.YamlFileManager;
import org.bukkit.Bukkit;

public class ConsoleChatter extends FileBasedChatter {

    public ConsoleChatter(YamlFileManager file) {
        super(file);

        setIdentifier(new ConsoleChatterIdentifier());
    }

    @Override
    public boolean isMuted() {
        return false;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public String getDisplayName() {
        return "Console";
    }

    @Override
    public String getWorldName() {
        return null;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

}