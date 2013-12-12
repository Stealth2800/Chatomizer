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
package com.stealthyone.mcb.chatomizer.commands;

import com.stealthyone.mcb.chatomizer.ChatomizerPlugin;
import com.stealthyone.mcb.chatomizer.api.Chatomizer;
import com.stealthyone.mcb.chatomizer.backend.formats.ChatFormat;
import com.stealthyone.mcb.chatomizer.messages.ErrorMessage;
import com.stealthyone.mcb.chatomizer.messages.NoticeMessage;
import com.stealthyone.mcb.chatomizer.messages.UsageMessage;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.stbukkitlib.backend.exceptions.PlayerIdNotExistsException;
import com.stealthyone.mcb.stbukkitlib.lib.updating.UpdateChecker;
import com.stealthyone.mcb.stbukkitlib.lib.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            NoticeMessage.FORMAT_NOTICE.sendTo(sender, Chatomizer.players.getFormat((Player) sender).getName());
        } else {
            sender.sendMessage("-help-");
        }
        return true;
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

        ChatFormat format = Chatomizer.formats.getFormat(args[1]);
        if (format == null) {
            ErrorMessage.FORMAT_INVALID.sendTo(sender, args[1]);
        } else {
            sender.sendMessage(ChatColor.DARK_GRAY + "=====" + ChatColor.GREEN + "Format: " + ChatColor.GOLD + format.getName() + ChatColor.DARK_GRAY + "=====");
            sender.sendMessage(ChatColor.RED + " Creator: " + ChatColor.YELLOW + format.getCreator());
            sender.sendMessage(ChatColor.RED + " Format: " + ChatColor.YELLOW + format.getFormat());
            sender.sendMessage(ChatColor.RED + " Format (raw): " + ChatColor.YELLOW + format.getFormat(true));
        }
    }

    /*
     * List styles command
     */
    private void cmdList(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.STYLE_LIST.isAllowed(sender, true)) return;

        sender.sendMessage(ChatColor.DARK_GRAY + "=====" + ChatColor.GREEN + "Chat Formats" + ChatColor.DARK_GRAY + "=====");
        sender.sendMessage(ChatColor.YELLOW + " " + Chatomizer.formats.getAllowedFormats(sender).toString().replace("[", "").replace("]", ""));
    }

    /*
     * Reload plugin data from disk
     */
    private void cmdReload(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionNode.ADMIN_RELOAD.isAllowed(sender, true)) return;

        plugin.reloadConfig();
        Chatomizer.formats.reloadFormats();
        Chatomizer.players.reloadPlayers();
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

        ChatFormat format = Chatomizer.formats.getFormat(args[startIndex]);
        if (format == null) {
            ErrorMessage.FORMAT_INVALID.sendTo(sender, args[startIndex]);
            return;
        } else if (!PermissionNode.STYLES.isAllowed(sender, format.getName().toLowerCase())) {
            ErrorMessage.FORMAT_NO_PERMISSION.sendTo(sender, format.getName());
            return;
        }

        try {
            if (Chatomizer.players.setFormat((Player) sender, format)) {
                NoticeMessage.FORMAT_SET.sendTo(sender, format.getName());
            } else {
                ErrorMessage.FORMAT_ALREADY_SET.sendTo(sender, format.getName());
            }
        } catch (PlayerIdNotExistsException ex) {
            ErrorMessage.FORMAT_UNABLE_TO_SET.sendTo(sender, format.getName());
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