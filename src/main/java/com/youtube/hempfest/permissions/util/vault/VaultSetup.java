package com.youtube.hempfest.permissions.util.vault;

import com.youtube.hempfest.permissions.HempfestPermissions;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Logger;

public class VaultSetup {

    private final Logger log = Logger.getLogger("Minecraft");
    HempfestPermissions plugin;
    Permission provider;

    public VaultSetup(HempfestPermissions plugin) {
        this.plugin = plugin;
    }

    public void hook() {
        provider = plugin.perms;
        Bukkit.getServicesManager().register(Permission.class, this.provider, this.plugin, ServicePriority.High);
        log.info(String.format("[%s] - Vault permissions hooked!", plugin.getDescription().getName()));
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Permission.class, this.provider);
        log.info(String.format("[%s] - Vault permissions un-hooked!", plugin.getDescription().getName()));
    }


}
