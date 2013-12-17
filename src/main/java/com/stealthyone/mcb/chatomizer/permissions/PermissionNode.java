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
package com.stealthyone.mcb.chatomizer.permissions;

import com.stealthyone.mcb.chatomizer.messages.ErrorMessage;
import org.bukkit.command.CommandSender;

public enum PermissionNode {

    ADMIN_RELOAD,
    ADMIN_SAVE,

    CHAT_COLOR,
    CHAT_FORMATTING,
    CHAT_MAGIC,

    STYLE_CHANGE,
    STYLE_DEFAULT,
    STYLE_INFO,
    STYLE_LIST;

    private String permission;

    private PermissionNode() {
        permission = "chatomizer." + toString().toLowerCase().replace("_", ".");
    }

    public boolean isAllowed(CommandSender sender) {
        return isAllowed(sender, false);
    }

    public boolean isAllowed(CommandSender sender, boolean alert) {
        boolean value = sender.hasPermission(permission);
        if (!value && alert)
            ErrorMessage.NO_PERMISSION.sendTo(sender);
        return value;
    }

    public final static VariablePermissionNode STYLES = VariablePermissionNode.STYLE;

}