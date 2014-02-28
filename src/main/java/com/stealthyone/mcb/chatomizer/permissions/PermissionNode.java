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
package com.stealthyone.mcb.chatomizer.permissions;

import com.stealthyone.mcb.chatomizer.messages.ErrorMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public enum PermissionNode {

    ADMIN_RELOAD,
    ADMIN_SAVE,

    CHAT(PermissionDefault.TRUE),
    CHAT_COLOR,
    CHAT_FORMATTING,
    CHAT_MAGIC,

    STYLE_CHANGE,
    STYLE_DEFAULT,
    STYLE_INFO,
    STYLE_LIST;

    private Permission permission;

    private PermissionNode() {
        this(Permission.DEFAULT_PERMISSION);
    }

    private PermissionNode(PermissionDefault defaultState) {
        permission = new Permission("chatomizer." + toString().toLowerCase().replace("_", "."), defaultState);
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

    public Permission getPermission() {
        return permission;
    }

    public final static VariablePermissionNode STYLES = VariablePermissionNode.STYLES;

}