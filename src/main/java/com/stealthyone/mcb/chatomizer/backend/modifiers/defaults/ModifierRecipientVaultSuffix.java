package com.stealthyone.mcb.chatomizer.backend.modifiers.defaults;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.ChatModifier;
import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.api.chatters.ChatterPlayer;
import net.milkbowl.vault.chat.Chat;

public class ModifierRecipientVaultSuffix extends ChatModifier {

    public ModifierRecipientVaultSuffix() {
        super("RSUFFIX", true);
    }

    @Override
    public String getReplacement(Chatter sender, Chatter recipient) {
        Chat chat = ChatomizerPlugin.getInstance().getHookVault().getChat();
        return (chat == null || !(recipient instanceof ChatterPlayer)) ? "" : chat.getPlayerSuffix(recipient.getWorldName(), recipient.getName());
    }

}