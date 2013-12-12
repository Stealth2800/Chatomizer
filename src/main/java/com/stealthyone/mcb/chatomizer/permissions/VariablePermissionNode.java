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

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public enum VariablePermissionNode {

    STYLE(1);

    private int varCount;
    private String permission;

    private VariablePermissionNode(int varCount) {
        this.varCount = varCount;
    }

    public boolean isAllowed(CommandSender sender, String... variables) {
        Validate.notNull(sender);
        Validate.notNull(variables);
        if (variables.length != varCount)
            throw new IllegalArgumentException("Invalid number of variables for VariablePermissionNode: " + toString());

        return sender.hasPermission(permission + "." + Arrays.toString(variables).replace("[", ".").replace("]", "").replace(",", "."));
    }

}