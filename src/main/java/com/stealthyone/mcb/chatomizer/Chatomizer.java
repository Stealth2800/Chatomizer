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
package com.stealthyone.mcb.chatomizer;

import com.stealthyone.mcb.chatomizer.backend.ChatomizerChatManager;
import com.stealthyone.mcb.chatomizer.backend.formats.FormatManager;
import com.stealthyone.mcb.chatomizer.backend.modifiers.ChatomizerModifierManager;
import com.stealthyone.mcb.chatomizer.commands.CmdChatomizer;
import com.stealthyone.mcb.chatomizer.hooks.VaultHook;
import com.stealthyone.mcb.chatomizer.listeners.PlayerListener;
import com.stealthyone.mcb.stbukkitlib.autosaving.Autosavable;
import com.stealthyone.mcb.stbukkitlib.autosaving.Autosaver;
import com.stealthyone.mcb.stbukkitlib.help.HelpManager;
import com.stealthyone.mcb.stbukkitlib.messages.MessageManager;
import com.stealthyone.mcb.stbukkitlib.updates.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;

public class Chatomizer extends JavaPlugin implements Autosavable {

    private static Chatomizer instance;

    private HelpManager helpManager;
    private MessageManager messageManager;
    private UpdateChecker updateChecker;

    private ChatomizerChatManager chatManager;
    private ChatomizerModifierManager modifierManager;
    private FormatManager formatManager;

    private VaultHook hookVault;

    @Override
    public void onLoad() {
        Chatomizer.instance = this;
        getDataFolder().mkdir();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
        saveConfig();

        /* Load hooks */
        hookVault = new VaultHook(this);
        hookVault.load();

        /* Load managers */
        helpManager = new HelpManager(this);
        helpManager.reload();

        messageManager = new MessageManager(this);
        messageManager.reloadMessages();

        formatManager = new FormatManager(this);
        formatManager.load();

        chatManager = new ChatomizerChatManager(this);
        chatManager.load();

        modifierManager = new ChatomizerModifierManager(this);
        modifierManager.load();

        /* Register events */
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        /* Register commands */
        getCommand("chatomizer").setExecutor(new CmdChatomizer(this));

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            //Failed
        }

        if (!Autosaver.scheduleForMe(this, this, getConfig().getInt("Autosave interval", 0))) {
            getLogger().warning("Autosaving disabled. It is recommended that you enable it in order to prevent data loss!");
        }
        updateChecker = new UpdateChecker(this, 71196);
        getLogger().info(String.format("Chatomizer v%s by Stealth2800 ENABLED.", getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        saveAll();
        getLogger().info(String.format("Chatomizer v%s by Stealth2800 DISABLED.", getDescription().getVersion()));
        Chatomizer.instance = null;
    }

    public static Chatomizer getInstance() {
        return instance;
    }

    public void reloadAll() {
        reloadConfig();
        helpManager.reload();
        messageManager.reloadMessages();
        formatManager.reload();
        chatManager.reload();
    }

    @Override
    public void saveAll() {
        formatManager.save();
        chatManager.save();
        saveConfig();
    }

    public ChatomizerChatManager getChatManager() {
        return chatManager;
    }

    public FormatManager getFormatManager() {
        return formatManager;
    }

    public HelpManager getHelpManager() {
        return helpManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ChatomizerModifierManager getModifierManager() {
        return modifierManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public VaultHook getHookVault() {
        return hookVault;
    }

}