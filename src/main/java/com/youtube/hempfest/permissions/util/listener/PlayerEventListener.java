package com.youtube.hempfest.permissions.util.listener;

import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.events.PermissionUpdateEvent;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UtilityManager um = UtilityManager.get(p);
        um.generateUserFile();
        MyPermissions.getInstance().getManager().userPermissions.put(p, Arrays.asList(MyPermissions.getInstance().getPermissionHook().playerPermissions(p, p.getWorld().getName())));
        MyPermissions.getInstance().inject(p);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        MyPermissions.getInstance().getManager().userPermissions.remove(p);
    }

    @EventHandler
    public void onTeleport(PlayerChangedWorldEvent e) {
            PermissionUpdateEvent event = new PermissionUpdateEvent();
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                if (!MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
                    event.query(e.getPlayer());
                }
            }
        }


}
