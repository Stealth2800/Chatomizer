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
import com.stealthyone.mcb.chatomizer.api.chatters.ChatterIdentifier;
import com.stealthyone.mcb.chatomizer.api.formats.ChatFormat;
import com.stealthyone.mcb.chatomizer.backend.ChatomizerChatManager;
import com.stealthyone.mcb.chatomizer.backend.formats.FormatManager;
import com.stealthyone.mcb.stbukkitlib.utils.QuickSet;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.*;

/**
 * Represents a Chatomizer chat event.
 */
public class MultiChatEvent extends Event implements Cancellable {

    private static HandlerList handlerList = new HandlerList();
    private boolean isCancelled = false;

    private ChatomizerChatManager chatManager;

    private Chatter sender;
    private String message;

    private Set<ChatterIdentifier> recipients;

    private Map<ChatterIdentifier, String> overriddenFormats = new HashMap<>();
    private Map<ChatterIdentifier, String> overriddenMessages = new HashMap<>();

    /**
     * Creates a new MultiChatEvent. You should use ChatomizerAPI.createChatEvent rather than this directly.
     *
     * @param sender Sender of the message.
     * @param message Message to send.
     */
    public MultiChatEvent(Chatter sender, String message) {
        Validate.notNull(sender, "Sender cannot be null.");
        Validate.notNull(message, "Message cannot be null.");

        this.chatManager = Chatomizer.getInstance().getChatManager();

        this.sender = sender;
        this.message = message;

        recipients = new QuickSet<>(chatManager.getConsoleChatter().getIdentifier()).build();
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayerRecipient(player);
        }
    }

    /**
     * Returns the sender of the chat message.
     *
     * @return Sender of the message.
     */
    public Chatter getSender() {
        return sender;
    }

    /**
     * Returns the message for a Chatter.
     *
     * @return The chat message for a Chatter.
     */
    public String getMessage(Chatter chatter) {
        ChatterIdentifier identifier = chatter.getIdentifier();
        return overriddenMessages.containsKey(identifier) ? overriddenMessages.get(identifier) : message;
    }

    /**
     * Adds a player recipient.
     *
     * @param player Player to add.
     * @return True if successfully added player.
     *         False if player is already in the list.
     */
    public boolean addPlayerRecipient(Player player) {
        Validate.notNull(player, "Player cannot be null.");
        return addRecipient(chatManager.getPlayerChatter(player));
    }

    /**
     * Removes a player recipient.
     *
     * @param player Player recipient to remove.
     * @return True if successfully removed player.
     *         False if the player isn't a recipient.
     */
    public boolean removePlayerRecipient(Player player) {
        Validate.notNull(player, "Player cannot be null.");
        return removeRecipient(chatManager.getPlayerChatter(player));
    }

    /**
     * Adds a chatter recipient.
     *
     * @param chatter Chatter to add as a recipient.
     * @return True if successfully added the chatter as a recipient.
     *         False if the chatter is already a recipient.
     */
    public boolean addRecipient(Chatter chatter) {
        Validate.notNull(chatter, "Chatter cannot be null.");
        if (!chatManager.isChatterRegistered(chatter)) {
            throw new IllegalArgumentException("Chatter '" + chatter.getIdentifier() + "' is not registered with Chatomizer.");
        }
        return recipients.add(chatter.getIdentifier());
    }

    /**
     * Removes a chatter recipient.
     *
     * @param chatter Chatter recipient to remove.
     * @return True if successfully removed chatter.
     *         False if the chatter isn't a recipient.
     */
    public boolean removeRecipient(Chatter chatter) {
        Validate.notNull(chatter, "Chatter cannot be null.");
        return recipients.remove(chatter.getIdentifier());
    }

    /**
     * Returns a set of all the recipients of the chat event.
     *
     * @return A set of the recipients in the chat event.
     */
    public Set<Chatter> getRecipients() {
        Set<Chatter> returnSet = new HashSet<>();
        List<ChatterIdentifier> invalid = new ArrayList<>();

        for (ChatterIdentifier identifier : recipients) {
            Chatter chatter = chatManager.getChatter(identifier);
            if (chatter == null) {
                invalid.add(identifier);
            } else {
                returnSet.add(chatter);
            }
        }

        for (ChatterIdentifier identifier : invalid) {
            recipients.remove(identifier);
        }

        return Collections.unmodifiableSet(returnSet);
    }

    /**
     * Sets the recipients for the chat event.
     *
     * @param chatters Chatters to set as the new recipients.
     */
    public void setRecipients(List<Chatter> chatters) {
        Validate.notNull(chatters, "Player list cannot be null.");

        recipients.clear();
        for (Chatter c : chatters) {
            try {
                addRecipient(c);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Returns the final format set for a recipient.
     *
     * @param chatter Recipient to get format of.
     */
    public ChatFormat getFormat(Chatter chatter) {
        Validate.notNull(chatter, "Chatter cannot be null.");

        ChatterIdentifier identifier = chatter.getIdentifier();
        if (!recipients.contains(identifier)) {
            throw new IllegalArgumentException("Chatter '" + chatter.getIdentifier() + "' isn't a recipient.");
        }

        FormatManager formatManager = Chatomizer.getInstance().getFormatManager();
        ChatFormat format = null;
        if (overriddenFormats.containsKey(identifier)) {
            format = formatManager.getFormat(overriddenFormats.get(identifier));
        }

        if (format == null) {
            format = formatManager.getFormat(chatter.getChatFormat());
        }
        return format;
    }

    /**
     * Overrides a player's chosen chat format.
     *
     * @param chatter Player to override format for.
     * @param format Format to set.
     *               Null if you want the player to use their selected chat format.
     */
    public void setFormat(Chatter chatter, ChatFormat format) {
        if (format == null) {
            overriddenFormats.remove(chatter.getIdentifier());
        } else {
            overriddenFormats.put(chatter.getIdentifier(), format.getName());
        }
    }

    /**
     * Sets the default message for the event.
     *
     * @param message Message to set.
     *                Should not be null. Cancel the event if you wish to not send any messages.
     */
    public void setMessage(String message) {
        Validate.notNull(message, "Message cannot be null.");

        this.message = message;
    }

    /**
     * Overrides a message for a specified player.
     *
     * @param chatter Player to override message for.
     * @param message Message to use.
     *                Null if you want the player to receive the default message.
     */
    public void setMessage(Chatter chatter, String message) {
        if (message == null) {
            overriddenMessages.remove(chatter.getIdentifier());
        } else {
            overriddenMessages.put(chatter.getIdentifier(), message);
        }
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

}