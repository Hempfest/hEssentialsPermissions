package com.youtube.hempfest.permissions.util.layout;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class PermissionRefresh extends BukkitRunnable {

    public PermissionRefresh() {}

    private final PermissionHook listener = new PermissionHook();

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                    if (HempfestPermissions.getInstance().playerPermissions.containsKey(p.getUniqueId())) {
                        unsetPermissions(p);
                    }
            }
        }
    }
    public void unsetPermissions(Player player) {
        PermissionAttachment attachment = HempfestPermissions.getInstance().playerPermissions.get(player.getUniqueId());

        for (Map.Entry<String, Boolean> e : attachment.getPermissions().entrySet()) {
            if (!Arrays.asList(listener.playerPermissions(player.getUniqueId(), player.getWorld().getName())).contains(e.getKey())) {
                attachment.unsetPermission(e.getKey());
            }
        }
        for (String perm : listener.playerPermissions(player.getUniqueId(), player.getWorld().getName())) {
            if (!attachment.getPermissions().containsKey(perm)) {
                attachment.setPermission(perm, true);
            }
        }
        HempfestPermissions.getInstance().playerPermissions.put(player.getUniqueId(), attachment);
        player.updateCommands();
    }

}
