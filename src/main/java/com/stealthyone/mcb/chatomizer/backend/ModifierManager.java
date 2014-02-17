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
package com.stealthyone.mcb.chatomizer.backend;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.api.ChatModifier;
import com.stealthyone.mcb.chatomizer.backend.modifiers.*;

import java.util.HashMap;
import java.util.Map;

public class ModifierManager {

    private ChatomizerPlugin plugin;

    private Map<String, ChatModifier> registeredModifiers = new HashMap<>();

    public ModifierManager(ChatomizerPlugin plugin) {
        this.plugin = plugin;

        /* Register built in modifiers. */
        registerModifier(new ModifierSenderName());
        registerModifier(new ModifierSenderDispName());
        registerModifier(new ModifierSenderWorld());
        registerModifier(new ModifierVaultGroup());
        registerModifier(new ModifierVaultPrefix());
        registerModifier(new ModifierVaultSuffix());
    }

    public boolean registerModifier(ChatModifier modifier) {
        String code = modifier.getCode();
        if (registeredModifiers.containsKey(code)) {
            Log.warning("Unable to register chat modifier '" + code + "' -> already registered.");
            return false;
        } else {
            registeredModifiers.put(code, modifier);
            Log.info("Registered chat modifier '" + code + "'");
            return true;
        }
    }

    public boolean unregisterModifier(ChatModifier modifier) {
        String code = modifier.getCode();
        if (!registeredModifiers.containsKey(code)) {
            Log.warning("Unable to unregister chat modifier '" + code + "' -> not registered.");
            return false;
        } else {
            registeredModifiers.remove(code);
            Log.info("Unregistered chat modifier '" + code + "'");
            return true;
        }
    }

    public Map<String, ChatModifier> getRegisteredModifiers() {
        return registeredModifiers;
    }

}