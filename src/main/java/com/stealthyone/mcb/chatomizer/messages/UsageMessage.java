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
package com.stealthyone.mcb.chatomizer.messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum UsageMessage {

    CHAT("{TAG}/{LABEL} chat <message>"),
    STYLE("{TAG}/{LABEL} style <style name>"),
    STYLE_DIRECT("{TAG}/{LABEL} <style name>"),
    STYLE_INFO("{TAG}/{LABEL} info <style name>");

    private final String TAG = ChatColor.GOLD + "[Chatomizer] " + ChatColor.RED;
    private String message;

    private UsageMessage(String message) {
        this.message = message.replace("{TAG}", TAG);
    }

    public void sendTo(CommandSender sender, String label) {
        sender.sendMessage(message.replace("{LABEL}", label));
    }

    public String getMessage(String label) {
        return message.replace("{LABEL}", label);
    }

}