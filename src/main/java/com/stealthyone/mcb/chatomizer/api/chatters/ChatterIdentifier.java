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

import org.apache.commons.lang.Validate;

/**
 * Identifies a chatter.
 */
public class ChatterIdentifier {

    private String identification;

    /**
     * Creates a new chatter identification instance.
     *
     * @param identification The unique identification for the chatter.
     */
    public ChatterIdentifier(String identification) {
        Validate.notNull(identification, "Identification cannot be null.");
        this.identification = identification;
    }

    @Override
    public String toString() {
        return identification;
    }

    @Override
    public int hashCode() {
        return identification.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof ChatterIdentifier) {
            return ((ChatterIdentifier) obj).identification.equals(this.identification);
        } else {
            return false;
        }
    }

    /**
     * Returns the raw identification represented by the identifier.
     *
     * @return The raw identification.
     */
    public String getIdentification() {
        return identification;
    }

}