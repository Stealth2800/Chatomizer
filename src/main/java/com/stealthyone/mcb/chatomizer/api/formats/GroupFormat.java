package com.stealthyone.mcb.chatomizer.api.formats;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

/**
 * Represents a group's format.
 */
public class GroupFormat {

    private String groupName;
    private String format;

    /**
     * Creates a new group format representation.
     *
     * @param groupName Name of the group this object represents.
     * @param format The format itself.
     */
    public GroupFormat(String groupName, String format) {
        Validate.notNull(groupName, "Group name cannot be null.");
        Validate.notNull(format, "Format cannot be null.");

        this.groupName = groupName;
        this.format = format;
    }

    /**
     * Returns the name of the group.
     *
     * @return The name of the group.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Returns the format.
     *
     * @return The format.
     */
    public String getFormat() {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    /**
     * Returns the raw format.
     *
     * @return The raw format;
     */
    public String getFormatRaw() {
        return format;
    }

}