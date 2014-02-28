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
package com.stealthyone.mcb.chatomizer;

import com.stealthyone.mcb.chatomizer.backend.chatters.ChatterManager;
import com.stealthyone.mcb.chatomizer.backend.modifiers.ModifierManager;
import com.stealthyone.mcb.chatomizer.backend.players.PlayerManager;
import com.stealthyone.mcb.chatomizer.backend.formats.FormatManager;
import com.stealthyone.mcb.chatomizer.backend.hooks.HookVault;
import com.stealthyone.mcb.chatomizer.commands.CmdChatomizer;
import com.stealthyone.mcb.chatomizer.config.ConfigHelper;
import com.stealthyone.mcb.chatomizer.listeners.PlayerListener;
import com.stealthyone.mcb.chatomizer.utils.MessageManager;
import com.stealthyone.mcb.chatomizer.utils.TimeUtils;
import com.stealthyone.mcb.chatomizer.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatomizerPlugin extends JavaPlugin {

    public static class Log {

        public static void debug(String msg) {
            if (ConfigHelper.DEBUG.get())
                instance.logger.log(Level.INFO, String.format("[%s DEBUG] %s", instance.getName(), msg));
        }

        public static void info(String msg) {
            instance.logger.log(Level.INFO, String.format("[%s] %s", instance.getName(), msg));
        }

        public static void warning(String msg) {
            instance.logger.log(Level.WARNING, String.format("[%s] %s", instance.getName(), msg));
        }

        public static void severe(String msg) {
            instance.logger.log(Level.SEVERE, String.format("[%s] %s", instance.getName(), msg));
        }
    }

    private static ChatomizerPlugin instance;
    {
        instance = this;
    }

    public static ChatomizerPlugin getInstance() {
        return instance;
    }

    private Logger logger;

    private MessageManager messageManager;
    private UpdateChecker updateChecker;

    private HookVault hookVault;

    private ChatterManager chatterManager;
    private FormatManager formatManager;
    private ModifierManager modifierManager;
    private PlayerManager playerManager;

    @Override
    public void onLoad() {
        logger = Bukkit.getLogger();
        getDataFolder().mkdir();
        new File(getDataFolder() + File.separator + "data").mkdir();
    }

    @Override
    public void onEnable() {
        /* Setup config */
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);
        saveConfig();

        /* Setup hooks */
        hookVault = new HookVault(this);

        /* Setup important plugin components */
        messageManager = new MessageManager(this);
        updateChecker = UpdateChecker.scheduleForMe(this, 71196);

        chatterManager = new ChatterManager(this);
        formatManager = new FormatManager(this);
        modifierManager = new ModifierManager(this);
        playerManager = new PlayerManager(this);

        /* Register listeners */
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        /* Register commands */
        getCommand("chatomizer").setExecutor(new CmdChatomizer(this));

        int autosaveInterval = getConfig().getInt("Autosave interval");
        if (autosaveInterval >= 1) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    saveAll();
                }
            }, autosaveInterval * 1200L, autosaveInterval * 1200L);
            Log.info("Autosaving enabled. Saving every " + TimeUtils.translateSeconds(autosaveInterval * 60));
        } else {
            Log.warning("Autosaving DISABLED. It is recommended that you enable it to prevent data loss.");
        }

        Log.info(String.format("%s v%s by Stealth2800 enabled.", getName(), getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        saveAll();
        Log.info(String.format("%s v%s by Stealth2800 disabled.", getName(), getDescription().getVersion()));
    }

    public void saveAll() {
        playerManager.saveAll();
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public HookVault getHookVault() {
        return hookVault;
    }

    public ChatterManager getChatterManager() {
        return chatterManager;
    }

    public FormatManager getFormatManager() {
        return formatManager;
    }

    public ModifierManager getModifierManager() {
        return modifierManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

}