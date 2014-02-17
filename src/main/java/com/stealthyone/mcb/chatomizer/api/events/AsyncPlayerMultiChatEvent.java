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

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AsyncPlayerMultiChatEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;

    private boolean sendToConsole;
    private ChatFormat consoleFormat;
    private Map<Player, ChatFormat> recipients;

    private String message = "";
    private String consoleMessage = "";
    private Map<Player, String> recipientMessages = new HashMap<>();

    public AsyncPlayerMultiChatEvent(Player sender, String message, Map<Player, ChatFormat> recipients, boolean sendToConsole, ChatFormat consoleFormat) {
        super(sender);
        this.message = message;
        this.recipients = recipients;
        this.sendToConsole = sendToConsole;
        this.consoleFormat = consoleFormat;
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

    public String getBaseMessage() {
        return message;
    }

    public void setBaseMessage(String message) {
        this.message = message;
    }

    public String getConsoleMessage() {
        return consoleMessage.equals("") ? message : consoleMessage;
    }

    public void setConsoleMessage(String message) {
        consoleMessage = message;
    }

    public String getPlayerMessage(Player player) {
        String message = recipientMessages.get(player);
        return message == null ? this.message : message;
    }

    public void setPlayerMessage(Player player, String message) {
        if (message == null || message.equals("")) {
            recipientMessages.remove(player);
        } else {
            recipientMessages.put(player, message);
        }
    }

    public Map<Player, ChatFormat> getRecipients() {
        return recipients;
    }

    public ChatFormat getFormat(Player player) {
        return recipients.get(player);
    }

    public void setFormat(Player player, ChatFormat newFormat) {
        if (newFormat == null) {
            recipients.remove(player);
        } else {
            recipients.put(player, newFormat);
        }
    }

    public void setRecipients(Set<Player> players) {
        recipients.clear();
        for (Player player : players) {
            addRecipient(player);
        }
    }

    public void addRecipient(Player player) {
        recipients.put(player, ChatomizerPlugin.getInstance().getPlayerManager().getFormat(player));
    }

    public void removeRecipient(Player player) {
        recipients.remove(player);
        recipientMessages.remove(player);
    }

    public boolean sendToConsole() {
        return sendToConsole;
    }

    public void setSendToConsole(boolean sendToConsole) {
        this.sendToConsole = sendToConsole;
    }

    public ChatFormat getConsoleFormat() {
        return consoleFormat;
    }

    public void setConsoleFormat(ChatFormat consoleFormat) {
        this.consoleFormat = consoleFormat;
    }

}