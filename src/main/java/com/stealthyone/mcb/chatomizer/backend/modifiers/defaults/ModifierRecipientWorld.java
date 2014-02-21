package com.stealthyone.mcb.chatomizer.backend.modifiers.defaults;

import com.stealthyone.mcb.chatomizer.api.ChatModifier;
import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;

public class ModifierRecipientWorld extends ChatModifier {

    public ModifierRecipientWorld() {
        super("RWORLD", true);
    }

    @Override
    public String getReplacement(Chatter sender, Chatter recipient) {
        return recipient.getWorldName();
    }

}