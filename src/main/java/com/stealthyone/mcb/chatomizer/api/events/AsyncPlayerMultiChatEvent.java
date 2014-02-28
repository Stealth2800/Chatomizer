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
package com.stealthyone.mcb.chatomizer.api.events;

import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.backend.chatters.Chatter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AsyncPlayerMultiChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    private Chatter sender;
    private Map<Chatter, ChatFormat> recipients;

    private String message = "";
    private Map<Chatter, String> recipientMessages = new HashMap<>();

    public AsyncPlayerMultiChatEvent(Chatter sender, String message, Map<Chatter, ChatFormat> recipients, boolean sendToConsole, ChatFormat consoleFormat) {
        this.sender = sender;
        this.message = message;
        this.recipients = recipients;
        recipients.put(sender, sender.getChatFormat());
        if (sendToConsole) {
            //recipients.put(new ChatterConsole(), consoleFormat != null ? consoleFormat : ChatomizerPlugin.getInstance().getFormatManager().getDefaultFormat());
        }
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Chatter getSender() {
        return sender;
    }

    public String getBaseMessage() {
        return message;
    }

    public void setBaseMessage(String message) {
        this.message = message;
    }

    public String getChatterMessage(Chatter chatter) {
        String message = recipientMessages.get(chatter);
        return message == null ? this.message : message;
    }

    public void setChatterMessage(Chatter chatter, String message) {
        if (message == null || message.equals("")) {
            recipientMessages.remove(chatter);
        } else {
            recipientMessages.put(chatter, message);
        }
    }

    public Map<Chatter, ChatFormat> getRecipients() {
        return recipients;
    }

    public ChatFormat getFormat(Chatter chatter) {
        return recipients.get(chatter);
    }

    public void setFormat(Chatter chatter, ChatFormat newFormat) {
        if (newFormat == null) {
            recipients.remove(chatter);
        } else {
            recipients.put(chatter, newFormat);
        }
    }

    public void setRecipients(Set<Chatter> chatters) {
        recipients.clear();
        for (Chatter chatter : chatters) {
            addRecipient(chatter);
        }
    }

    public void addRecipient(Chatter chatter) {
        recipients.put(chatter, chatter.getChatFormat());
    }

    public void removeRecipient(Player player) {
        recipients.remove(player);
        recipientMessages.remove(player);
    }

}