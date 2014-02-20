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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static boolean isSenderPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public static boolean isSenderPlayer(CommandSender sender, String errorMessage) {
        boolean result = sender instanceof Player;
        if (!result && !errorMessage.equalsIgnoreCase(""))
            sender.sendMessage(errorMessage);
        return result;
    }

}