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
package com.stealthyone.mcb.chatomizer.api.modifiers;

/**
 * Represents Chatomizer's modifier manager.
 * The modifier manager manages all registered chat modifiers for use in chat events.
 */
public interface ModifierManager {

    /**
     * Registers a chat modifier.
     *
     * @param modifier Modifier to register.
     * @return True if successful.
     *         False if modifier is already registered.
     */
    public boolean registerModifier(ChatModifier modifier);

    /**
     * Unregisters a chat modifier.
     *
     * @param modifier Modifier to unregister.
     * @return True if successful.
     *         False if modifier was never registered.
     */
    public boolean unregisterModifier(ChatModifier modifier);

}