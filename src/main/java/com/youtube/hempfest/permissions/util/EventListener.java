package com.youtube.hempfest.permissions.util;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.events.PermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.layout.PermissionBase;
import com.youtube.hempfest.permissions.util.yml.Config;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UtilityManager um = new UtilityManager(p);
        um.generateUserFile();
        HempfestPermissions.getInstance().inject(p);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        HempfestPermissions.getInstance().playerPermissions.remove(p.getUniqueId());
    }

    @EventHandler
    public void onTeleport(PlayerChangedWorldEvent e) {
            PermissionUpdateEvent event = new PermissionUpdateEvent();
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                if (!Config.get("Config", "data").getBoolean("global.ignore-world-container")) {
                    event.query(e.getPlayer());
                }
            }
        }


}
