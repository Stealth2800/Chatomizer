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

    public String getChatFormat() {
        return chatFormat;
    }

    public boolean setChatFormat(String chatFormat) {
        if ((this.chatFormat == null && chatFormat == null)
                || (this.chatFormat != null && chatFormat != null && this.chatFormat.equalsIgnoreCase(chatFormat))) {
            return false;
        }
        this.chatFormat = chatFormat;
        return true;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public final ChatterIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ChatterIdentifier identifier) {
        if (this.identifier != null) {
            throw new IllegalStateException("ChatterIdentifier has already been defined.");
        }
        this.identifier = identifier;
    }

    public abstract void save();

    public abstract String getName();

    public abstract String getDisplayName();

    public abstract String getWorldName();

    public abstract void sendMessage(String message);

}