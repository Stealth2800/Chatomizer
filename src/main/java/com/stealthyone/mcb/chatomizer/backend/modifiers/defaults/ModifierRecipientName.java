package com.stealthyone.mcb.chatomizer.backend.modifiers.defaults;

import com.stealthyone.mcb.chatomizer.api.ChatModifier;
import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;

public class ModifierRecipientName extends ChatModifier {

    public ModifierRecipientName() {
        super("RNAME", true);
    }

    @Override
    public String getReplacement(Chatter sender, Chatter recipient) {
        return recipient.getName();
    }

}