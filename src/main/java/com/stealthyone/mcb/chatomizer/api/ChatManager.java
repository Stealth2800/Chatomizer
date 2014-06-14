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

import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.api.chatters.ChatterIdentifier;
import org.bukkit.entity.Player;

/**
 * Represents Chatomizer's chat manager.
 * The chat manager tracks chatters and the sending/handling of the chat event.
 */
public interface ChatManager {

    /**
     * Loads a chatter.
     *
     * @param chatter Chatter to load.
     */
    public boolean loadChatter(Chatter chatter);

    /**
     * Saves a chatter.
     *
     * @param chatter Chatter to save.
     */
    public void saveChatter(Chatter chatter);

    /**
     * Unloads a chatter
     *
     * @param chatter Chatter to unload.
     */
    public boolean unloadChatter(Chatter chatter);

    /**
     * Returns the console chatter.
     *
     * @return The console chatter.
     */
    public Chatter getConsoleChatter();

    /**
     * Returns the player chatter object for a specified player.
     *
     * @param player Player to retrieve chatter object of.
     * @return Chatter object for player.
     */
    public Chatter getPlayerChatter(Player player);

    /**
     * Returns a generic chatter matching the specified identifier.
     *
     * @param identifier Identifier of chatter to retrieve.
     * @return Chatter matching identifier.
     *         Null if no chatter matching identifier was found.
     */
    public Chatter getChatter(ChatterIdentifier identifier);

    /**
     * Creates a chat event with the console sender using a specified message.
     *
     * @param message Message to send as the console.
     * @return The newly created event.
     */
    public MultiChatEvent createConsoleChatEvent(String message);

    /**
     * Creates a chat event with a given sender and a specified message.
     *
     * @param sender Chatter that is sending the message.
     * @param message The message being sent.
     * @return The newly created event.
     */
    public MultiChatEvent createChatEvent(Chatter sender, String message);

}