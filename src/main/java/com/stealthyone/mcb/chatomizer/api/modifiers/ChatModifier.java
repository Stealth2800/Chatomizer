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

import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;
import org.apache.commons.lang.Validate;

/**
 * Represents a chat modifier.
 */
public abstract class ChatModifier {

    private String code;
    private boolean recipientSpecific;

    /**
     * Creates a new chat modifier.
     *
     * @param code Code to replace in chat. (Ex. "{GUILD}")
     * @param recipientSpecific Whether or not this chat modifier is recipient specific.
     */
    public ChatModifier(String code, boolean recipientSpecific) {
        Validate.notNull(code, "Code cannot be null.");

        this.code = "{" + code.toUpperCase() + "}";
        this.recipientSpecific = recipientSpecific;
    }

    public final String getCode() {
        return code;
    }

    public final boolean isRecipientSpecific() {
        return recipientSpecific;
    }

    /**
     * Returns the replacement for a specified sender and optional recipient.
     *
     * @param sender Sender of the message containing the code.
     * @param recipient Recipient of the message containing the code.
     *                  NOTE: If you use this variable, you need to mark this chat modifier as recipientSpecific = true
     * @return The replacement for a specified sender and recipient.
     *         Return null or an empty string if there is no replacement.
     */
    public abstract String getReplacement(Chatter sender, Chatter recipient);

}