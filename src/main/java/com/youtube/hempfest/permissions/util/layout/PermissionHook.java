package com.youtube.hempfest.permissions.util.layout;

import com.github.sanctum.labyrinth.data.FileManager;
import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.events.GroupPermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.events.PermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.events.UserPermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateType;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import com.youtube.hempfest.permissions.util.UtilityManager;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PermissionHook {

    private final UtilityManager um = UtilityManager.get();

   
    public String[] getAllGroups() {
        DataManager dm = new DataManager();
        List<String> array = new ArrayList<>();
        for (String w : um.getWorlds()) {
            FileManager world = dm.getGroups(w);
            FileConfiguration fc = world.getConfig();
            array.addAll(fc.getKeys(false));
        }
        return array.toArray(new String[0]);
    }

    public String[] getAllGroups(String world) {
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            return getAllGroups();
        }
        DataManager dm = new DataManager();
        FileManager w = dm.getGroups(world);
            FileConfiguration fc = w.getConfig();
        List<String> array = new ArrayList<>(fc.getKeys(false));
        return array.toArray(new String[0]);
    }

    public String[] getAllUserIDs(String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager w = dm.getUsers(world);
        FileConfiguration fc = w.getConfig();
        List<String> array = new ArrayList<>(Objects.requireNonNull(fc.getConfigurationSection("User-List")).getKeys(false));
        return array.toArray(new String[0]);
    }

    public String[] getAllUserNames(String world) {
        DataManager dm = new DataManager();
        List<String> array = new ArrayList<>();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager w = dm.getUsers(world);
        FileConfiguration fc = w.getConfig();
        for (String user : Objects.requireNonNull(fc.getConfigurationSection("User-List")).getKeys(false)) {
            array.add(fc.getString("User-List." + user + ".username"));
        }
        return array.toArray(new String[0]);
    }

   
    public String getGroup(OfflinePlayer player, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        return users.getConfig().getString("User-List." + player.getUniqueId().toString() + ".group");
    }

    public String getGroup(UUID playerID, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        return users.getConfig().getString("User-List." + playerID.toString() + ".group");
    }

   
    public String[] getGroups(OfflinePlayer player, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        List<String> groups = new ArrayList<>(users.getConfig().getStringList("User-List." + player.getUniqueId().toString() + ".sub-groups"));
        groups.add(getGroup(player, world));
        return groups.toArray(new String[0]);
    }

   
    public boolean playerInGroup(OfflinePlayer player, String world, String group) {
        return getGroup(player, world).equals(group);
    }

   
    public boolean playerGiveGroup(OfflinePlayer player, String world, String group) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> groups = new ArrayList<>(Arrays.asList(getGroups(player, world)));
        groups.add(group);
        u.set("User-List." + player.getUniqueId().toString() + ".sub-groups", groups);
        users.saveConfig();
        System.out.printf("[%s] - Gave sub-group " + '"' + group + '"' + " to player " + '"' + player + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        PermissionUpdateEvent event = new PermissionUpdateEvent();
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerTakeGroup(OfflinePlayer player, String world, String group) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> groups = new ArrayList<>(Arrays.asList(getGroups(player, world)));
        groups.add(group);
        u.set("User-List." + player.getUniqueId().toString() + ".sub-groups", groups);
        users.saveConfig();
        System.out.printf("[%s] - Removed sub-group " + '"' + group + '"' + " from player " + '"' + player + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        PermissionUpdateEvent event = new PermissionUpdateEvent();
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerHas(OfflinePlayer player, String world, String permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        if (u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions").contains(permission)) {
            return true;
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        for (String subGroup : getGroups(player, world)) {
            if (g.getStringList(subGroup + ".permissions").contains(permission)) {
                return true;
            }
        }
        return false;
    }

   
    public boolean groupHas(String group, String world, String permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        return g.getStringList(group + ".permissions").contains(permission);
    }

    public int groupWeight(String group, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        return g.getInt(group + ".weight");
    }

   
    public boolean groupGive(String group, String world, String permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        if (perms.contains(permission)) {
            return false;
        }
        perms.add(permission);
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.printf("[%s] - Gave permission " + '"' + permission + '"' + " to group " + '"' + group + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Added, group, world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

    public boolean groupGive(String group, String world, List<String> permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        for (String perm : permission) {
            if (!perms.contains(perm)) {
                perms.add(perm);
            }
        }
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.printf("[%s] - Gave permission " + '"' + permission + '"' + " to group " + '"' + group + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Added, group, world, permission.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean groupTake(String group, String world, String permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        perms.remove(permission);
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.printf("[%s] - Removed permission " + '"' + permission + '"' + " from group " + '"' + group + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Removed, group, world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

    public boolean groupTake(String group, String world, List<String> permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = new ArrayList<>(g.getStringList(group + ".permissions"));
        for (String perm : permission) {
            perms.remove(perm);
        }
        g.set(group + ".permissions", perms);
        groups.saveConfig();
        System.out.printf("[%s] - Removed permission " + '"' + permission + '"' + " from group " + '"' + group + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        GroupPermissionUpdateEvent event = new GroupPermissionUpdateEvent(PermissionUpdateType.Removed, group, world, permission.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            event.query();
        }
        return true;
    }

   
    public boolean playerGive(OfflinePlayer player, String world, String permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        perms.add(permission);
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.printf("[%s] - Gave permission " + '"' + permission + '"' + " to player " + '"' + player.getName() + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Added, player.getUniqueId().toString(), world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (player.isOnline()) {
                event.query(player.getPlayer());
            }
        }
        return true;
    }

   
    public void playerGive(OfflinePlayer player, String world, List<String> permissions) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        perms.addAll(permissions);
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.printf("[%s] - Gave permission's " + permissions.toString() + " to player " + '"' + player.getName() + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Added, player.getUniqueId().toString(), world, permissions.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (player.isOnline()) {
                event.query(player.getPlayer());
            }
        }
    }

   
    public boolean playerTake(OfflinePlayer player, String world, String permission) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        perms.remove(permission);
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.printf("[%s] - Removed permission " + '"' + permission + '"' + " from player " + '"' + player.getName() + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Removed, player.getUniqueId().toString(), world, permission);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (player.isOnline()) {
                event.query(player.getPlayer());
            }
        }
        return true;
    }

   
    public void playerTake(OfflinePlayer player, String world, List<String> permissions) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        for (String result : permissions) {
            perms.remove(result);
        }
        u.set("User-List." + player.getUniqueId().toString() + ".permissions", perms);
        users.saveConfig();
        System.out.printf("[%s] - Removed permission's " + permissions.toString() + " from player " + '"' + player.getName() + '"' + " in world " + '"' + world + "\"%n", MyPermissions.getInstance().getDescription().getName());
        UserPermissionUpdateEvent event = new UserPermissionUpdateEvent(PermissionUpdateType.Removed, player.getUniqueId().toString(), world, permissions.toArray(new String[0]));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (player.isOnline()) {
                event.query(player.getPlayer());
            }
        }
    }

   
    public String[] playerPermissions(OfflinePlayer player, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + player.getUniqueId().toString() + ".permissions");
        perms.addAll(Arrays.asList(groupPermissions(getGroup(player.getUniqueId(), world), world)));
        for (String sgroup : getGroups(player, world)) {
            perms.addAll(Arrays.asList(groupPermissions(sgroup, world)));
        }
        return perms.toArray(new String[0]);
    }

    public String[] playerPermissions(UUID uuid, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + uuid.toString() + ".permissions");
        perms.addAll(Arrays.asList(groupPermissions(getGroup(uuid, world), world)));
        for (String sgroup : getGroups(Bukkit.getOfflinePlayer(uuid), world)) {
            perms.addAll(Arrays.asList(groupPermissions(sgroup, world)));
        }
        return perms.toArray(new String[0]);
    }

    public String[] playerDirectPermissions(UUID uuid, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager users = dm.getUsers(world);
        FileConfiguration u = users.getConfig();
        List<String> perms = u.getStringList("User-List." + uuid.toString() + ".permissions");
        return perms.toArray(new String[0]);
    }

    public String[] groupDirectPermissions(String group, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = g.getStringList(group + ".permissions");
        return perms.toArray(new String[0]);
    }

    public String[] groupPermissions(String group, String world) {
        DataManager dm = new DataManager();
        if (MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getBoolean("global.ignore-world-container")) {
            world = MyPermissions.getInstance().getFileList().find("Config", "data").getConfig().getString("global.world");
        }
        FileManager groups = dm.getGroups(world);
        FileConfiguration g = groups.getConfig();
        List<String> perms = g.getStringList(group + ".permissions");
        for (String gr : g.getStringList(group + ".inheritance")) {
            perms.addAll(g.getStringList(gr + ".permissions"));
        }
        return perms.toArray(new String[0]);
    }
}
