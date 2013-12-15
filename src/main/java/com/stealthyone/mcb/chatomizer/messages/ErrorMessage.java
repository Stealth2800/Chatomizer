/*
 * HelpEX - Simple yet powerful command-based help API for Bukkit
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

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.stbukkitlib.lib.messages.MessageReferencer;
import org.bukkit.command.CommandSender;

public enum ErrorMessage implements MessageReferencer {

    FORMAT_ALREADY_SET,
    FORMAT_DEFAULT_ALREADY_SET,
    FORMAT_INVALID,
    FORMAT_NO_PERMISSION,
    FORMAT_UNABLE_TO_SET,

    MUST_BE_PLAYER,
    NO_PERMISSION,
    PAGE_MUST_BE_INT;

    private String path;

    private ErrorMessage() {
        this.path = "errors." + toString().toLowerCase();
    }

    @Override
    public String getMessagePath() {
        return path;
    }

    @Override
    public String getMessage() {
        return ChatomizerPlugin.getInstance().getMessageManager().getMessage(this);
    }

    @Override
    public String getMessage(String... replacements) {
        return ChatomizerPlugin.getInstance().getMessageManager().getMessage(this, replacements);
    }

    @Override
    public void sendTo(CommandSender sender) {
        sender.sendMessage(getMessage());
    }

    @Override
    public void sendTo(CommandSender sender, String... replacements) {
        sender.sendMessage(getMessage(replacements));
    }

}