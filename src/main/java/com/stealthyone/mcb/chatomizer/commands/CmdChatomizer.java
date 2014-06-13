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
package com.stealthyone.mcb.chatomizer.commands;

import com.stealthyone.mcb.chatomizer.Chatomizer;
import com.stealthyone.mcb.chatomizer.api.formats.ChatFormat;
import com.stealthyone.mcb.chatomizer.api.ChatomizerAPI;
import com.stealthyone.mcb.chatomizer.api.chatters.Chatter;
import com.stealthyone.mcb.chatomizer.messages.Messages.ErrorMessages;
import com.stealthyone.mcb.chatomizer.messages.Messages.NoticeMessages;
import com.stealthyone.mcb.chatomizer.messages.Messages.PluginMessages;
import com.stealthyone.mcb.chatomizer.messages.Messages.UsageMessages;
import com.stealthyone.mcb.chatomizer.permissions.PermissionNode;
import com.stealthyone.mcb.stbukkitlib.updates.UpdateChecker;
import com.stealthyone.mcb.stbukkitlib.utils.QuickMap;
import com.stealthyone.mcb.stbukkitlib.utils.StringUtils;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CmdChatomizer implements CommandExecutor {

    private Chatomizer plugin;

    public CmdChatomizer(Chatomizer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("chat")) {
            cmdChat(sender, label, args, 0);
            return true;
        } else if (label.equalsIgnoreCase("chatstyle") || label.equalsIgnoreCase("cstyle")) {
            cmdFormat(sender, label, args, 0);
            return true;
        } else {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    /* Send a chat message */
                    case "chat":
                        cmdChat(sender, label, args, 1);
                        return true;

                    /* View/set default format */
                    case "default":

                        return true;

                    /* Show format info */
                    case "info":

                        return true;

                    /* List formats */
                    case "list":
                        cmdList(sender);
                        return true;

                    /* Reload plugin configuration command */
                    case "reload":
                        cmdReload(sender);
                        return true;

                    /* Save plugin data command */
                    case "save":
                        cmdSave(sender);
                        return true;

                    /* Change current chat format */
                    case "style":case "format":
                        cmdFormat(sender, label, args, 1);
                        return true;

                    /* Plugin version command */
                    case "version":
                        cmdVersion(sender);
                        return true;

                    default:
                        ErrorMessages.UNKNOWN_COMMAND.sendTo(sender);
                        break;
                }
            }
        }
        return true;
    }

    private Chatter getChatter(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return plugin.getChatManager().getConsoleChatter();
        } else if (sender instanceof Player) {
            return plugin.getChatManager().getPlayerChatter((Player) sender);
        } else {
            return null;
        }
    }

    private void cmdChat(CommandSender sender, String label, String[] args, int startIndex) {
        if (!PermissionNode.CHAT.isAllowedAlert(sender)) return;

        if (args.length < startIndex + 1) {
            UsageMessages.CHATOMIZER_CHAT.sendTo(sender, new QuickMap<>("{LABEL}", label).build());
            return;
        }

        Chatter chatter = getChatter(sender);
        if (chatter == null) {
            ErrorMessages.UNABLE_TO_CHAT.sendTo(sender);
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = startIndex + 1; i < args.length; i++) {
            if (message.length() > 0) {
                message.append(" ");
            }
            message.append(args[i]);
        }

        ChatomizerAPI.createChatEvent(chatter, message.toString());
    }

    /* List formats */
    private void cmdList(CommandSender sender) {
        if (!PermissionNode.FORMATS_LIST.isAllowedAlert(sender)) return;

        boolean isPlayer = sender instanceof Player;

        Chatter chatter = getChatter(sender);
        if (chatter == null) {
            ErrorMessages.UNABLE_TO_CHAT.sendTo(sender);
            return;
        }

        String curFormat = chatter.getChatFormat();

        FancyMessage list = new FancyMessage();
        boolean started = false;
        for (ChatFormat format : plugin.getFormatManager().getAllowedFormats(sender)) {
            if (!format.isHidden()) {
                if (started)
                    list.then(", ").color(ChatColor.DARK_GRAY);

                if (isPlayer) {
                    try {
                        list.then(format.getName());
                    } catch (Exception ex) {
                        list.text(format.getName());
                    }
                    list.color(format.getName().equals(curFormat) ? ChatColor.GREEN : ChatColor.YELLOW);
                } else {
                    try {
                        list.then(format.getName());
                    } catch (Exception ex) {
                        list.text(format.getName());
                    }
                    list.color(ChatColor.YELLOW);
                }

                list.formattedTooltip(new FancyMessage().text("Click to select format").color(ChatColor.GREEN))
                    .command("/cstyle " + format.getName());

                started = true;
            }
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "=====" + ChatColor.GREEN + "Chat Formats" + ChatColor.DARK_GRAY + "=====");
        if (list.toOldMessageFormat().length() == 0) {
            PluginMessages.FORMATS_LIST_NONE.sendTo(sender);
        } else {
            if (isPlayer) {
                list.send((Player) sender);
            } else {
                sender.sendMessage(list.toOldMessageFormat());
            }
        }
    }

    /* Reload configuration */
    private void cmdReload(CommandSender sender) {
        if (!PermissionNode.RELOAD.isAllowedAlert(sender)) return;

        try {
            plugin.reloadAll();
            NoticeMessages.PLUGIN_RELOADED.sendTo(sender);
        } catch (Exception ex) {
            ErrorMessages.RELOAD_ERROR.sendTo(sender, new QuickMap<>("{MESSAGE}", ex.getMessage()).build());
        }
    }

    /* Save data */
    private void cmdSave(CommandSender sender) {
        if (!PermissionNode.SAVE.isAllowedAlert(sender)) return;

        try {
            plugin.saveAll();
            NoticeMessages.PLUGIN_SAVED.sendTo(sender);
        } catch (Exception ex) {
            ErrorMessages.SAVE_ERROR.sendTo(sender, new QuickMap<>("{MESSAGE}", ex.getMessage()).build());
        }
    }

    /* Set chat format command */
    private void cmdFormat(CommandSender sender, String label, String[] args, int startIndex) {
        if (!PermissionNode.FORMATS_CHANGE.isAllowedAlert(sender)) return;

        Chatter chatter = getChatter(sender);
        if (chatter == null) {
            ErrorMessages.UNABLE_TO_GET_CHATTER.sendTo(sender);
            return;
        }

        if (args.length < startIndex + 1) {
            if (startIndex == 0) {
                UsageMessages.CHATOMIZER_FORMAT_DIRECT.sendTo(sender, new QuickMap<>("{LABEL}", label).build());
            } else {
                UsageMessages.CHATOMIZER_FORMAT.sendTo(sender, new QuickMap<>("{LABEL}", label).build());
            }
            return;
        }

        String formatName = args[startIndex];
        ChatFormat format = plugin.getFormatManager().doesFormatExist(formatName) ? plugin.getFormatManager().getFormat(formatName) : null;
        if (format == null) {
            if (StringUtils.equalsIgnoreCaseMultiple(formatName, "none", "default")) {
                format = plugin.getFormatManager().getDefaultFormat();
            } else {
                ErrorMessages.FORMAT_NOT_FOUND.sendTo(sender, new QuickMap<>("{FORMAT}", args[startIndex]).build());
                return;
            }
        } else if (format.checkPermission() && !PermissionNode.FORMATS.isAllowed(sender, format.getName().toLowerCase())) {
            ErrorMessages.NO_PERMISSION_FORMAT.sendTo(sender, new QuickMap<>("{FORMAT}", format.getName()).build());
            return;
        }

        if (chatter.setChatFormat(format.getName())) {
            NoticeMessages.FORMAT_SET.sendTo(sender, new QuickMap<>("{FORMAT}", format.getName()).build());
        } else {
            ErrorMessages.FORMAT_ALREADY_SET.sendTo(sender, new QuickMap<>("{FORMAT}", format.getName()).build());
        }
    }

    /* Plugin version */
    private void cmdVersion(CommandSender sender) {
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Chatomizer" + ChatColor.GOLD + " v" + plugin.getDescription().getVersion());
        sender.sendMessage("" + ChatColor.BLUE + ChatColor.ITALIC + "Created by Stealth2800");
        sender.sendMessage("" + ChatColor.AQUA + ChatColor.UNDERLINE + "http://stealthyone.com/");
        UpdateChecker updateChecker = plugin.getUpdateChecker();
        if (updateChecker.checkForUpdates()) {
            String curVer = plugin.getDescription().getVersion();
            String remVer = updateChecker.getNewVersion().replace("v", "");
            sender.sendMessage(ChatColor.RED + "A different version was found on BukkitDev! (Current: " + curVer + " | Remote: " + remVer + ")");
            sender.sendMessage(ChatColor.RED + "You can download it from " + updateChecker.getVersionLink());
        }
    }

}