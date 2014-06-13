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

import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;
import com.stealthyone.mcb.stbukkitlib.storage.YamlFileManager;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class FileBasedChatter extends Chatter {

    private YamlFileManager file;

    public FileBasedChatter(YamlFileManager file) {
        Validate.notNull(file, "File cannot be null.");

        this.file = file;
        reload();
    }

    public void reload() {
        FileConfiguration config = file.getConfig();

        setChatFormat(config.getString("chatFormat", "default"));
        setMuted(config.getBoolean("muted", false));
    }

    @Override
    public void save() {
        FileConfiguration config = file.getConfig();

        config.set("muted", isMuted());
        config.set("format", getChatFormat());

        file.saveFile();
    }

}