package com.youtube.hempfest.permissions.util;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.PermissionAttachment;

public class BukkitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        Player p = (Player) e.getPlayer();
        UtilityManager um = new UtilityManager(p);
        um.generateUserFile();
        HempfestPermissions.getInstance().hookPermissions(p);
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();
        if (!from.getWorld().getName().equals(to.getWorld().getName())) {
            UtilityManager um = new UtilityManager(p);
            um.generateUserFile();
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        HempfestPermissions.getInstance().playerPermissions.remove(p.getUniqueId());
    }




}
