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
package com.stealthyone.mcb.chatomizer.api.chatters;

/**
 * Represents a chatter.
 */
public abstract class Chatter {

    private String chatFormat = null;
    private boolean muted = false;
    private ChatterIdentifier identifier;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Chatter) {
            return getName().equals(((Chatter) obj).getName());
        } else {
            return false;
        }
    }

    /**
     * Returns the name of the chatter's current chat format.
     *
     * @return The name of the chatter's chat format.
     */
    public String getChatFormat() {
        return chatFormat;
    }

    /**
     * Sets the chatter's current format.
     *
     * @param chatFormat Name of the chat format to set.
     * @return True if successful.
     *         False if the input format is already set for the chatter.
     */
    public boolean setChatFormat(String chatFormat) {
        if ((this.chatFormat == null && chatFormat == null)
                || (this.chatFormat != null && chatFormat != null && this.chatFormat.equalsIgnoreCase(chatFormat))) {
            return false;
        }
        this.chatFormat = chatFormat;
        return true;
    }

    /**
     * Returns whether or not the chatter is muted.
     *
     * @return True if the chatter is muted.
     *         False if the chatter is not muted.
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Sets the muted status for the chatter.
     *
     * @param muted True to mute the chatter.
     *              False to unmute the chatter.
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    /**
     * Returns the chatter identifier for this chatter.
     *
     * @return The chatter identifier. CANNOT BE NULL. MUST be set before registering the chatter with Chatomizer.
     */
    public final ChatterIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Sets the chatter identifier for this chatter.
     *
     * @param identifier The chatter identifier. CANNOT BE NULL. MUST be set before registering the chatter with Chatomizer.
     */
    public void setIdentifier(ChatterIdentifier identifier) {
        if (this.identifier != null) {
            throw new IllegalStateException("ChatterIdentifier has already been defined.");
        }
        this.identifier = identifier;
    }

    /**
     * Saves (if applicable) any data for the chatter.
     */
    public abstract void save();

    /**
     * Returns the name of the chatter.
     *
     * @return The name of the chatter.
     */
    public abstract String getName();

    /**
     * Returns the display name of the chatter.
     *
     * @return The display name of the chatter.
     */
    public abstract String getDisplayName();

    /**
     * Returns the name of the world the chatter is in.
     *
     * @return Name of the chatter's current world.
     *         Null if not applicable.
     */
    public abstract String getWorldName();

    /**
     * Returns whether or not the chatter has permission for a specified node.
     *
     * @param permission Permission node to check.
     * @return True if the chatter has permission.
     *         False if the chatter doesn't have permission.
     */
    public abstract boolean hasPermission(String permission);

    /**
     * Sends a message to the chatter.
     *
     * @param message Message to send to the chatter.
     */
    public abstract void sendMessage(String message);

}