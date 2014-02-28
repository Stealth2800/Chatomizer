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
package com.stealthyone.mcb.chatomizer.commands;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.ChatomizerPlugin.Log;
import com.stealthyone.mcb.chatomizer.api.ChatFormat;
import com.stealthyone.mcb.chatomizer.messages.ErrorMessage;
import com.stealthyone.mcb.chatomizer.messages.NoticeMessage;
import com.stealthyone.mcb.chatomizer.messages.UsageMessage;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.chatomizer.utils.PlayerUtils;
import com.stealthyone.mcb.chatomizer.utils.StringUtils;
import com.stealthyone.mcb.chatomizer.utils.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map.Entry;

public class CmdChatomizer implements CommandExecutor {

    private ChatomizerPlugin plugin;

    public CmdChatomizer(ChatomizerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (label.equalsIgnoreCase("chatstyle") || label.equalsIgnoreCase("cstyle")) {
                cmdStyle(sender, command, label, args, 0);
                return true;
            } else {
                switch (args[0].toLowerCase()) {
                    case "chat":
                        cmdChat(sender, command, label, args);
                        return true;

                    /* Default format info */
                    case "default":
                        cmdDefault(sender, command, label, args);
                        return true;

                    /* Format info command */
                    case "info":
                        cmdInfo(sender, command, label, args);
                        return true;

                    /* List formats command */
                    case "list":
                        cmdList(sender, command, label, args);
                        return true;

                    /* Reload config command */
                    case "reload":
                        cmdReload(sender, command, label, args);
                        return true;

                    /* Save data command */
                    case "save":
                        cmdSave(sender, command, label, args);
                        return true;

                    /* Change format command */
                    case "style":case "format":
                        cmdStyle(sender, command, label, args, 1);
                        return true;

                    /* Version command */
                    case "version":case "about":
                        cmdVersion(sender, command, label, args);
                        return true;
                }
            }
        }
        if (PlayerUtils.isSenderPlayer(sender)) {
            NoticeMessage.FORMAT_NOTICE.sendTo(sender, plugin.getPlayerManager().getFormat((Player) sender).getName());
        } else {
            sender.sendMessage("polished help coming soon");
            sender.sendMessage("commands: default, info, list, reload, save, style/format, version/about");
        }
        return true;
    }

    /*
     * Send chat message (mainly for console usage)
     */
    private void cmdChat(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.CHAT.isAllowed(sender, true)) return;

        plugin.getChatterManager().
    }

    /*
     * Default format info/set default format
     */
    private void cmdDefault(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.STYLE_DEFAULT.isAllowed(sender, true)) return;

        if (args.length == 1) {
            NoticeMessage.FORMAT_DEFAULT_NOTICE.sendTo(sender, plugin.getFormatManager().getDefaultFormat().getName());
        } else {
            String formatName = args[1];
            ChatFormat format = plugin.getFormatManager().getFormat(formatName);
            if (format == null) {
                ErrorMessage.FORMAT_INVALID.sendTo(sender, formatName);
            } else if (plugin.getFormatManager().setDefaultFormat(format)) {
                NoticeMessage.FORMAT_DEFAULT_SET.sendTo(sender, format.getName());
            } else {
                ErrorMessage.FORMAT_DEFAULT_ALREADY_SET.sendTo(sender, format.getName());
            }
        }
    }

    /*
     * Get format info command
     */
    private void cmdInfo(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.STYLE_INFO.isAllowed(sender, true)) {
            return;
        } else if (args.length < 2) {
            UsageMessage.STYLE_INFO.sendTo(sender, label);
            return;
        }

        ChatFormat format = plugin.getFormatManager().doesFormatExist(args[1]) ? plugin.getFormatManager().getFormat(args[1]) : null;
        if (format == null) {
            ErrorMessage.FORMAT_INVALID.sendTo(sender, args[1]);
        } else {
            sender.sendMessage(ChatColor.DARK_GRAY + "=====" + ChatColor.GREEN + "Format: " + ChatColor.GOLD + format.getName() + ChatColor.DARK_GRAY + "=====");
            sender.sendMessage(ChatColor.RED + "Creator: " + ChatColor.YELLOW + format.getCreator());
            sender.sendMessage(ChatColor.RED + "Default format: " + ChatColor.YELLOW + format.getDefaultFormat());
            for (Entry<String, String> entry : format.getAllFormats().entrySet()) {
                sender.sendMessage(ChatColor.RED + entry.getKey() + ": " + ChatColor.YELLOW + entry.getValue());
            }
        }
    }

    /*
     * List styles command
     */
    private void cmdList(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.STYLE_LIST.isAllowed(sender, true)) return;

        boolean isPlayer = sender instanceof Player;

        StringBuilder list = new StringBuilder();
        for (ChatFormat format : plugin.getFormatManager().getAllowedFormats(sender)) {
            if (!format.isHidden()) {
                if (list.length() > 0)
                    list.append(ChatColor.DARK_GRAY).append(", ");
                if (isPlayer) {
                    list.append(plugin.getPlayerManager().getFormat(sender.getName()).equals(format) ? ChatColor.GREEN : ChatColor.YELLOW).append(format.getName());
                } else {
                    list.append(ChatColor.YELLOW).append(format.getName());
                }
            }
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "=====" + ChatColor.GREEN + "Chat Formats" + ChatColor.DARK_GRAY + "=====");
        sender.sendMessage(list.length() == 0 ? "" + ChatColor.RED + ChatColor.ITALIC + "Nothing here" : ChatColor.YELLOW + list.toString());
    }

    /*
     * Reload plugin data from disk
     */
    private void cmdReload(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.ADMIN_RELOAD.isAllowed(sender, true)) return;

        plugin.reloadConfig();
        plugin.getFormatManager().reloadFormats();
        plugin.getPlayerManager().reloadPlayers();
        NoticeMessage.PLUGIN_RELOADED.sendTo(sender);
    }

    /*
     * Save plugin data to disk
     */
    private void cmdSave(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.ADMIN_SAVE.isAllowed(sender, true)) return;

        plugin.saveAll();
        NoticeMessage.PLUGIN_SAVED.sendTo(sender);
    }

    /*
     * Change style command
     */
    private void cmdStyle(CommandSender sender, Command command, String label, String[] args, int startIndex) {
        if (!PermissionNode.STYLE_CHANGE.isAllowed(sender, true)) return;

        if (!PlayerUtils.isSenderPlayer(sender, ErrorMessage.MUST_BE_PLAYER.getMessage())) return;

        if (args.length < startIndex + 1) {
            if (startIndex == 0) {
                UsageMessage.STYLE_DIRECT.sendTo(sender, label);
            } else {
                UsageMessage.STYLE.sendTo(sender, label);
            }
            return;
        }

        String formatName = args[startIndex];
        ChatFormat format = plugin.getFormatManager().doesFormatExist(formatName) ? plugin.getFormatManager().getFormat(formatName) : null;
        if (format == null) {
            Log.debug("format is null");
            if (StringUtils.equalsIgnoreCaseMultiple(formatName, "none", "default")) {
                format = plugin.getFormatManager().getDefaultFormat();
            } else {
                ErrorMessage.FORMAT_INVALID.sendTo(sender, args[startIndex]);
                return;
            }
        } else if (!PermissionNode.STYLES.isAllowed(sender, format.getName().toLowerCase())) {
            ErrorMessage.FORMAT_NO_PERMISSION.sendTo(sender, format.getName());
            return;
        }

        if (plugin.getPlayerManager().setFormat((Player) sender, format)) {
            NoticeMessage.FORMAT_SET.sendTo(sender, format.getName());
        } else {
            ErrorMessage.FORMAT_ALREADY_SET.sendTo(sender, format.getName());
        }
    }

    /*
     * Version command
     */
    private void cmdVersion(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GOLD + " v" + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.GOLD + "Created by Stealth2800");
        sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.AQUA + "http://stealthyone.com/bukkit");
        UpdateChecker updateChecker = plugin.getUpdateChecker();
        if (updateChecker.checkForUpdates()) {
            String curVer = plugin.getDescription().getVersion();
            String remVer = updateChecker.getNewVersion().replace("v", "");
            sender.sendMessage(ChatColor.RED + "A different version was found on BukkitDev! (Current: " + curVer + " | Remote: " + remVer + ")");
            sender.sendMessage(ChatColor.RED + "You can download it from " + updateChecker.getVersionLink());
        }
    }

}