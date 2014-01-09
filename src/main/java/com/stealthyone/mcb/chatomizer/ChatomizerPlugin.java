/*
 * Chatomizer - Basic chat plugin that allows players to choose what chat format they wish to use
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

import com.stealthyone.mcb.chatomizer.backend.PlayerManager;
import com.stealthyone.mcb.chatomizer.backend.formats.FormatManager;
import com.stealthyone.mcb.chatomizer.commands.CmdChatomizer;
import com.stealthyone.mcb.chatomizer.config.ConfigHelper;
import com.stealthyone.mcb.chatomizer.listeners.PlayerListener;
import com.stealthyone.mcb.stbukkitlib.lib.autosaving.Autosavable;
import com.stealthyone.mcb.stbukkitlib.lib.messages.MessageManager;
import com.stealthyone.mcb.stbukkitlib.lib.updating.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatomizerPlugin extends JavaPlugin implements Autosavable {

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

    private FormatManager formatManager;
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

        /* Setup important plugin components */
        messageManager = new MessageManager(this);
        updateChecker = UpdateChecker.scheduleForMe(this, 71196);

        formatManager = new FormatManager(this);
        playerManager = new PlayerManager(this);

        /* Register listeners */
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        /* Register commands */
        getCommand("chatomizer").setExecutor(new CmdChatomizer(this));

        Log.info(String.format("%s v%s by Stealth2800 enabled.", getName(), getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        saveAll();
        Log.info(String.format("%s v%s by Stealth2800 disabled.", getName(), getDescription().getVersion()));
    }

    @Override
    public void saveAll() {
        playerManager.saveAll();
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public FormatManager getFormatManager() {
        return formatManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

}