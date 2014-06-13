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
package com.stealthyone.mcb.chatomizer.api;

import com.stealthyone.mcb.chatomizer.Chatomizer;
import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.api.modifiers.ModifierManager;

public final class ChatomizerAPI {

    private ChatomizerAPI() { }

    /**
     * Returns the chat manager instance.
     *
     * @return The current chat manager instance.
     */
    public static ChatManager getChatManager() {
        return Chatomizer.getInstance().getChatManager();
    }

    /**
     * Returns the modifier manager instance.
     *
     * @return The current modifier manager instance.
     */
    public static ModifierManager getModifierManager() {
        return Chatomizer.getInstance().getModifierManager();
    }

    /**
     * Creates a new chat event and handles it for you.
     *
     * @param sender Sender of the message.
     * @param message Message to be sent.
     * @return The new event instance.
     */
    public static MultiChatEvent createChatEvent(Chatter sender, String message) {
        return ChatomizerAPI.getChatManager().createChatEvent(sender, message);
    }

}