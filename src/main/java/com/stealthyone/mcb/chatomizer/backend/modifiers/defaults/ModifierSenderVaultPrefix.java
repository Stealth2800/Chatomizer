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
package com.stealthyone.mcb.chatomizer.backend.modifiers.defaults;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.ChatModifier;
import com.stealthyone.mcb.chatomizer.backend.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.backend.chatters.ChatterPlayer;
import net.milkbowl.vault.chat.Chat;

public class ModifierSenderVaultPrefix extends ChatModifier {

    public ModifierSenderVaultPrefix() {
        super("SPREFIX", false);
    }

    @Override
    public String getReplacement(Chatter sender, Chatter recipient) {
        Chat chat = ChatomizerPlugin.getInstance().getHookVault().getChat();
        return (chat == null || !(sender instanceof ChatterPlayer)) ? "" : chat.getPlayerPrefix(sender.getWorldName(), sender.getName());
    }

}