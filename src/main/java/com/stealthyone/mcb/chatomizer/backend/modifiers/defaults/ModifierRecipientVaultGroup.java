package com.stealthyone.mcb.chatomizer.backend.modifiers.defaults;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.ChatModifier;
import com.stealthyone.mcb.chatomizer.backend.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.backend.chatters.ChatterPlayer;
import net.milkbowl.vault.permission.Permission;

public class ModifierRecipientVaultGroup extends ChatModifier {

    public ModifierRecipientVaultGroup() {
        super("RGROUP", true);
    }

    @Override
    public String getReplacement(Chatter sender, Chatter recipient) {
        Permission permission = ChatomizerPlugin.getInstance().getHookVault().getPermission();
        return (permission == null || !(recipient instanceof ChatterPlayer)) ? "" : permission.getPrimaryGroup(recipient.getWorldName(), recipient.getName());
    }

}