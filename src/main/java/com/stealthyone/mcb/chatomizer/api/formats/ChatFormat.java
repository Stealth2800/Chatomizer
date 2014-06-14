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
package com.stealthyone.mcb.chatomizer.api.formats;

import java.util.Map;

/**
 * Represents a chat format.
 */
public interface ChatFormat {

    /**
     * Returns the name of the chat format.
     *
     * @return The name of the chat format.
     */
    public String getName();

    /**
     * Returns the creator of the chat format.
     *
     * @return The creator of the chat format.
     */
    public String getCreator();

    /**
     * Whether or not to hide the format in the /chatomizer list command.
     *
     * @return True to hide the format.
     *         False to show the format.
     */
    public boolean isHidden();

    /**
     * Whether or not to perform a permission check when a player selects the format.
     * Permission node: chatomizer.formats.<name>
     *
     * @return True to perform permission check.
     *         False to ignore permission check.
     */
    public boolean checkPermission();

    /**
     * Returns the default group format.
     *
     * @return The default group format.
     */
    public GroupFormat getDefaultGroupFormat();

    /**
     * Returns a group format for a given group.
     *
     * @param groupName Group format for a given group.
     * @return Name of group to retrieve format for.
     */
    public GroupFormat getGroupFormat(String groupName);

    /**
     * Returns a map of all available group formats.
     * Structure: <Group name, Group's format>
     *
     * @return Map of all available group formats.
     */
    public Map<String, GroupFormat> getAllGroupFormats();

}