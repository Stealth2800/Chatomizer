/*
 * StBukkitLib - Set of useful Bukkit-related classes
 * Copyright (C) 2013 Stealth2800 <stealth2800@stealthyone.com>
 * Website: <http://google.com/>
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
package com.stealthyone.mcb.chatomizer.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class FileUtils {

    public static File copyFileFromJar(JavaPlugin plugin, String fileName) throws IOException {
        return copyFileFromJar(plugin, fileName, plugin.getDataFolder());
    }

    public static File copyFileFromJar(JavaPlugin plugin, String fileName, File destination) throws IOException {
        Validate.notNull(plugin);
        Validate.notNull(fileName);
        Validate.notNull(destination);
        File file = new File(destination + File.separator + fileName);
        InputStream in = plugin.getResource(fileName);
        if (in == null)
            throw new FileNotFoundException("Unable to find file '" + fileName + "' in jar for plugin: '" + plugin.getName() + "'");

        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
        return file;
    }

}