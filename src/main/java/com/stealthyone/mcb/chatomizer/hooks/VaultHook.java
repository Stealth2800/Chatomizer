package com.stealthyone.mcb.chatomizer.hooks;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultHook {

    private JavaPlugin plugin;

    private boolean hooked;

    private Chat chat;
    private Permission permission;

    public VaultHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            hooked = false;
            plugin.getLogger().info("Unable to find optional Vault dependency. Some features will not be available unless you install it.");
            return;
        }

        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
            plugin.getLogger().info("Found permission provider '" + permissionProvider.getProvider().getName() + "' via Vault");
        }

        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
            plugin.getLogger().info("Found chat provider '" + chatProvider.getProvider().getName() + "' via Vault");
        }

        if (chat == null || permission == null) {
            plugin.getLogger().info("Unable to hook with Vault, chat and/or permission provider not found.");
            hooked = false;
        } else {
            plugin.getLogger().info("Successfully hooked with optional Vault dependency");
            hooked = true;
        }
    }

    public Permission getPermission() {
        if (!hooked) {
            return null;
        }
        return permission;
    }

    public Chat getChat() {
        if (!hooked) {
            return null;
        }
        return chat;
    }

}