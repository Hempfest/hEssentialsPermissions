package com.youtube.hempfest.permissions.util.layout;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.events.GroupPermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.events.PermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.events.UserPermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateType;
import com.youtube.hempfest.permissions.util.yml.Config;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import com.youtube.hempfest.permissions.util.UtilityManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PermissionHook {

    UtilityManager um = new UtilityManager();

   
    public String[] getAllGroups() {
        DataManager dm = new DataManager();
        List<String> array = new ArrayList<>();
        for (String w : um.getWorlds()) {
            Config world = dm.getGroups(w);
            FileConfiguration fc = world.getConfig();
            for (String group : fc.getKeys(false)) {
                array.add(group);
            }
        }
        return array.toArray(new String[0]);
    }

    public String[] getAllGroups(String world) {
        DataManager dm = new DataManager();
        List<String> array = new ArrayList<>();
            Config w = dm.getGroups(world);
            FileConfiguration fc = w.getConfig();
            for (String group : fc.getKeys(false)) {
                array.add(group);
            }
        return array.toArray(new String[0]);
    }

    public String[] getAllUserIDs(String world) {
        DataManager dm = new DataManager();
        List<String> array = new ArrayList<>();
        Config w = dm.getUsers(world);
        FileConfiguration fc = w.getConfig();
        for (String user : fc.getConfigurationSection("User-List").getKeys(false)) {
            array.add(user);
        }
        return array.toArray(new String[0]);
    }

    public String[] getAllUserNames(String world) {
        DataManager dm = new DataManager();
        List<String> array = new ArrayList<>();
        Config w = dm.getUsers(world);
        FileConfiguration fc = w.getConfig();
        for (String user : fc.getConfigurationSection("User-List").getKeys(false)) {
            array.add(fc.getString("User-List." + user + ".username"));
        }
        return array.toArray(new String[0]);
    }

   
    public String getGroup(OfflinePlayer player, String world) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        return users.getConfig().getString("User-List." + player.getUniqueId().toString() + ".group");
    }

    public String getGroup(UUID playerID, String world) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        return users.getConfig().getString("User-List." + playerID.toString() + ".group");
    }

   
    public String[] getGroups(OfflinePlayer player, String world) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        List<String> groups = new ArrayList<>(users.getConfig().getStringList("User-List." + player.getUniqueId().toString() + ".sub-groups"));
        groups.add(getGroup(player, world));
        return groups.toArray(new String[0]);
    }

   
    public boolean playerInGroup(OfflinePlayer player, String world, String group) {
        if (getGroup(player, world).equals(group)) {
            return true;
        }
        return false;
    }

   
    public boolean playerGiveGroup(OfflinePlayer player, String world, String group) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> groups = new ArrayList<>(Arrays.asList(getGroups(player, world)));
        groups.add(group);
        u.set("User-List." + player.getUniqueId().toString() + ".sub-groups", groups);
        users.saveConfig();
        System.out.println(String.format("[%s] - Gave sub-group " + '"' + group + '"' + " to player " + '"' + player + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        PermissionUpdateEvent event = new PermissionUpdateEvent();
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerTakeGroup(OfflinePlayer player, String world, String group) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> groups = new ArrayList<>(Arrays.asList(getGroups(player, world)));
        groups.add(group);
        u.set("User-List." + player.getUniqueId().toString() + ".sub-groups", groups);
        users.saveConfig();
        System.out.println(String.format("[%s] - Removed sub-group " + '"' + group + '"' + " from player " + '"' + player + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        PermissionUpdateEvent event = new PermissionUpdateEvent();
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerHas(OfflinePlayer player, String world, String permission) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        if (u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions").contains(permission)) {
            return true;
        }
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        for (String sgroup : getGroups(player, world)) {
            if (g.getStringList(sgroup + ".permissions").contains(permission)) {
                return true;
            }
        }
        return false;
    }

   
    public boolean groupHas(String group, String world, String permission) {
        DataManager dm = new DataManager();
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        if (g.getStringList(group + ".permissions").contains(permission)) {
            return true;
        }
        return false;
    }

   
    public boolean groupGive(String group, String world, String permission) {
        DataManager dm = new DataManager();
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        perms.add(permission);
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.println(String.format("[%s] - Gave permission " + '"' + permission + '"' + " to group " + '"' + group + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Added, group, world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return false;
    }

    public boolean groupGive(String group, String world, List<String> permission) {
        DataManager dm = new DataManager();
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        for (String perm : permission) {
            perms.add(perm);
        }
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.println(String.format("[%s] - Gave permission " + '"' + permission + '"' + " to group " + '"' + group + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Added, group, world, permission.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return false;
    }

   
    public boolean groupTake(String group, String world, String permission) {
        DataManager dm = new DataManager();
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        perms.remove(permission);
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.println(String.format("[%s] - Removed permission " + '"' + permission + '"' + " from group " + '"' + group + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Removed, group, world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return false;
    }

    public boolean groupTake(String group, String world, List<String> permission) {
        DataManager dm = new DataManager();
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        for (String perm : permission) {
            perms.remove(perm);
        }
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.println(String.format("[%s] - Removed permission " + '"' + permission + '"' + " from group " + '"' + group + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Removed, group, world, permission.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return false;
    }

   
    public boolean playerGive(OfflinePlayer player, String world, String permission) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        perms.add(permission);
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.println(String.format("[%s] - Gave permission " + '"' + permission + '"' + " to player " + '"' + player.getName() + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Added, player.getUniqueId().toString(), world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerGive(OfflinePlayer player, String world, List<String> permissions) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        for (String result : permissions) {
            perms.add(result);
        }
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.println(String.format("[%s] - Gave permission's " + permissions.toString() + " to player " + '"' + player.getName() + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Added, player.getUniqueId().toString(), world, permissions.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerTake(OfflinePlayer player, String world, String permission) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        perms.remove(permission);
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.println(String.format("[%s] - Removed permission " + '"' + permission + '"' + " from player " + '"' + player.getName() + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Removed, player.getUniqueId().toString(), world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerTake(OfflinePlayer player, String world, List<String> permissions) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        for (String result : permissions) {
            perms.remove(result);
        }
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.println(String.format("[%s] - Removed permission's " + permissions.toString() + " from player " + '"' + player.getName() + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Removed, player.getUniqueId().toString(), world, permissions.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public String[] playerPermissions(OfflinePlayer player, String world) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        perms.addAll(g.getStringList(getGroup(player.getUniqueId(), world) + ".permissions"));
        List<String> inheritance = g.getStringList(getGroup(player.getUniqueId(), world) + ".inheritance");
        for (String sgroup : getGroups(player, world)) {
            perms.addAll(g.getStringList(sgroup + ".permissions"));
            inheritance.addAll(g.getStringList(sgroup + ".inheritance"));
        }
        for (String igroup : inheritance) {
            perms.addAll(g.getStringList(igroup + ".permissions"));
        }
        return perms.toArray(new String[0]);
    }

    public String[] playerPermissions(UUID uuid, String world) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + uuid.toString() + ".permissions");
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        perms.addAll(g.getStringList(getGroup(uuid, world) + ".permissions"));
        List<String> inheritance = g.getStringList(getGroup(uuid, world) + ".inheritance");
        for (String sgroup : getGroups(Bukkit.getOfflinePlayer(uuid), world)) {
            perms.addAll(g.getStringList(sgroup + ".permissions"));
            inheritance.addAll(g.getStringList(sgroup + ".inheritance"));
        }
        for (String igroup : inheritance) {
            perms.addAll(g.getStringList(igroup + ".permissions"));
        }
        return perms.toArray(new String[0]);
    }

    public String[] playerDirectPermissions(UUID uuid, String world) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + uuid.toString() + ".permissions");
        return perms.toArray(new String[0]);
    }

    public String[] groupPermissions(String group, String world) {
        DataManager dm = new DataManager();
        Config groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = g.getStringList(group + ".permissions");
        for (String gr : g.getStringList(group + ".inheritance")) {
            for (String perm : g.getStringList(gr + ".permissions")) {
                perms.add(perm);
            }
        }
        return perms.toArray(new String[0]);
    }
}
