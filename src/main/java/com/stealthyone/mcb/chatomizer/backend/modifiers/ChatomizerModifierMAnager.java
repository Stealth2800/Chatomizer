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
package com.stealthyone.mcb.chatomizer.backend.modifiers;

import com.stealthyone.mcb.chatomizer.Chatomizer;
import com.stealthyone.mcb.chatomizer.api.modifiers.ChatModifier;
import com.stealthyone.mcb.chatomizer.api.modifiers.ModifierManager;
import com.stealthyone.mcb.chatomizer.backend.modifiers.defaults.*;
import com.stealthyone.mcb.stbukkitlib.logging.LogHelper;
import org.apache.commons.lang.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatomizerModifierManager implements ModifierManager {

    private Chatomizer plugin;

    private Map<String, ChatModifier> registeredModifiers = new HashMap<>();

    public ChatomizerModifierManager(Chatomizer plugin) {
        this.plugin = plugin;
    }

    public void load() {
        /* Register built in modifiers. */
        registerModifier(new ModifierRecipientName());
        registerModifier(new ModifierRecipientDisplayName());
        registerModifier(new ModifierRecipientWorld());
        registerModifier(new ModifierRecipientVaultGroup());
        registerModifier(new ModifierRecipientVaultPrefix());
        registerModifier(new ModifierRecipientVaultSuffix());

        registerModifier(new ModifierSenderName());
        registerModifier(new ModifierSenderDisplayName());
        registerModifier(new ModifierSenderWorld());
        registerModifier(new ModifierSenderVaultGroup());
        registerModifier(new ModifierSenderVaultPrefix());
        registerModifier(new ModifierSenderVaultSuffix());
    }

    @Override
    public boolean registerModifier(ChatModifier modifier) {
        Validate.notNull(modifier, "Modifier cannot be null.");

        String code = modifier.getCode();
        if (registeredModifiers.containsKey(code)) {
            plugin.getLogger().warning("Unable to register chat modifier '" + code + "' - already registered.");
            return false;
        } else {
            registeredModifiers.put(code, modifier);
            LogHelper.debug(plugin, "Registered chat modifier '" + code + "'");
            return true;
        }
    }

    @Override
    public boolean unregisterModifier(ChatModifier modifier) {
        Validate.notNull(modifier, "Modifier cannot be null.");

        String code = modifier.getCode();
        if (!registeredModifiers.containsKey(code)) {
            plugin.getLogger().warning("Unable to unregister chat modifier '" + code + "' - not registered.");
            return false;
        } else {
            registeredModifiers.remove(code);
            plugin.getLogger().info("Unregistered chat modifier '" + code + "'");
            return true;
        }
    }

    public Map<String, ChatModifier> getRegisteredModifiers() {
        return Collections.unmodifiableMap(registeredModifiers);
    }

}