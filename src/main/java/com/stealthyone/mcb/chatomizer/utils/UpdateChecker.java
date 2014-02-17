/*
 * StBukkitLib - Set of useful Bukkit-related classes
 * Copyright (C) 2013 Stealth2800 <stealth2800@stealthyone.com>
 * Website: <http://google.com/>
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
package com.stealthyone.mcb.chatomizer.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateChecker {

    public static UpdateChecker scheduleForMe(JavaPlugin plugin, int pluginId) {
        UpdateChecker updateChecker = new UpdateChecker(plugin, pluginId);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, updateChecker.getRunnable(), 40L, 432000L);
        return updateChecker;
    }

    private JavaPlugin plugin;
    private Logger logger;
    private int pluginId;
    private UpdateCheckRunnable runnable;

    private URL url;
    private boolean updateNeeded = false;
    private String newVersion = "", versionLink = "";

    public UpdateChecker(JavaPlugin plugin, int pluginId) {
        if (plugin == null) throw new IllegalArgumentException();
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        runnable = new UpdateCheckRunnable(this);
        try {
            this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + pluginId);
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "[UpdateChecker] Invalid pluginId '" + pluginId + "'");
            ex.printStackTrace();
        }
    }

    public UpdateCheckRunnable getRunnable() {
        return runnable;
    }

    public boolean isUpdateNeeded() {
        return updateNeeded;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public String getVersionLink() {
        return versionLink;
    }

    public boolean checkForUpdates() {
        return checkForUpdates(false);
    }

    public boolean checkForUpdates(boolean log) {
        String curVersion = "v" + plugin.getDescription().getVersion();
        if (!plugin.getConfig().getBoolean("Check for updates", false)) {
            // Check config value
            if (log) {
                logger.log(Level.INFO, "[UpdateChecker] Update checker is disabled, enable in config for auto update checking.");
                logger.log(Level.INFO, "[UpdateChecker] You can also check for updates by typing the version command.");
            }
            updateNeeded = false;
        } else if (StringUtils.containsMultipleIgnoreCaseInput(curVersion, "SNAPSHOT", "BETA", "ALPHA")) {
            // Check to see if version is a snapshot, beta, or alpha version
            if (log) {
                logger.log(Level.INFO, "[UpdateChecker] Currently running a snapshot, beta, or alpha build. Update check cancelled.");
            }
            updateNeeded = false;
        } else {
            // Do actual update check
            try {
                URLConnection conn = this.url.openConnection();
                conn.setConnectTimeout(5000);

                conn.addRequestProperty("User-Agent", "StBukkitLib (plugin: " + plugin.getName() + ")");

                conn.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = reader.readLine();

                JSONArray array = (JSONArray) JSONValue.parse(response);

                if (array.size() == 0) {
                    if (log) plugin.getLogger().warning("[UpdateChecker] Unable to find any files.");
                    return false;
                }

                this.newVersion = (String) ((JSONObject) array.get(array.size() - 1)).get("name");
                this.versionLink = (String) ((JSONObject) array.get(array.size() - 1)).get("downloadUrl");
                updateNeeded = !curVersion.equals(newVersion);
                if (updateNeeded && log) {
                    logger.log(Level.INFO, "[UpdateChecker] Found a different version on BukkitDev! (Remote: " + newVersion + " | Current: " + curVersion + ")");
                    logger.log(Level.INFO, "[UpdateChecker] You can download it from: " + versionLink);
                }
            } catch (IOException e) {
                if (log) {
                    plugin.getLogger().warning("[UpdateChecker] Unable to check for updates (" + e.getMessage() + ")");
                }
                updateNeeded = false;
            }
        }
        return updateNeeded;
    }

}