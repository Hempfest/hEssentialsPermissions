package com.youtube.hempfest.permissions.util;

import com.github.sanctum.labyrinth.data.FileManager;
import com.github.sanctum.labyrinth.task.Schedule;
import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UtilityManager {
    protected Player p;

    public String prefix = "&7[&bhPermissions&7]&r ";

    public Map<Player, List<String>> userPermissions = new HashMap<>();

    public FileManager FILE;

    protected UtilityManager() {
    }

    protected UtilityManager(Player executor) {
        this.p = executor;
    }

    public static UtilityManager get(Player... p) {
        UtilityManager manager = new UtilityManager();
        if (!Arrays.asList(p).isEmpty()) {
            manager.p = Arrays.asList(p).get(0);
        }
        return manager;
    }

    public boolean runningVault() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public List<String> getPermissionsLibrary() {
        return FILE.getConfig().getStringList("used-nodes");
    }

    public void inject(String permission) {
        Schedule.sync(() -> {
            if (!FILE.getConfig().getStringList("used-nodes").contains(permission)) {
                List<String> perms = new ArrayList<>(FILE.getConfig().getStringList("used-nodes"));
                perms.add(permission);
                FILE.getConfig().set("used-nodes", perms);
                FILE.saveConfig();
            }
        }).debug().run();
    }

    public void generateConfig() {
        FileManager main = MyPermissions.getInstance().getFileList().find("Config", "data");
        if (!main.exists()) {
            InputStream is = MyPermissions.getInstance().getResource("Config.yml");
            assert is != null;
            FileManager.copy(is, main.getFile());
        }
    }

    public void generateWorlds() {
        for (String w : getWorlds()) {
            FileManager toGenerate = MyPermissions.getInstance().getFileList().find("Groups", "worlds/" + w);
            FileManager usersFile = MyPermissions.getInstance().getFileList().find("Users", "worlds/" + w);
            if (!toGenerate.exists() || toGenerate.getConfig().getKeys(false).isEmpty()) {
                FileConfiguration world = toGenerate.getConfig();
                for (String rank : getDefaultRanks()) {
                    List<String> defaultP = new ArrayList<>();
                    List<String> defaultI = new ArrayList<>();
                    if (rank.equals("Moderator")) {
                        defaultP.add("mess.staff");
                        defaultP.add("mess.staff.kick");
                        defaultP.add("mess.staff.kickall");
                        defaultP.add("mess.staff.day");
                        defaultP.add("mess.staff.night");

                        defaultI.add("Default");
                    }
                    if (rank.equals("Builder")) {
                        defaultP.add("minecraft.command.gamemode");

                        defaultI.add("Default");
                    }
                    if (rank.equals("Admin")) {
                        defaultP.add("mess.group.add.permissions");
                        defaultP.add("mess.group.remove.permissions");
                        defaultP.add("mess.group.add.inheritance");
                        defaultP.add("mess.group.list.permissions");
                        defaultP.add("mess.group.create");
                        defaultP.add("mess.user.add.permission");
                        defaultP.add("mess.user.remove.permission");
                        defaultP.add("mess.user.set.group");
                        defaultP.add("mess.user.add.group");
                        defaultP.add("mess.user.remove.group");
                        defaultP.add("mess.group.list");
                        defaultP.add("mess.group.reload");
                        defaultP.add("mess.user.reload");
                        defaultP.add("mess.group.delete");
                        defaultP.add("mess.group");
                        defaultP.add("mess.user");
                        defaultP.add("minecraft.command.ban");
                        defaultP.add("minecraft.command.ban-ip");
                        defaultP.add("minecraft.command.banlist");
                        defaultP.add("minecraft.command.enchant");
                        defaultP.add("minecraft.command.kick");
                        defaultP.add("minecraft.command.kill");
                        defaultP.add("minecraft.command.pardon");
                        defaultP.add("minecraft.command.pardon-ip");

                        defaultI.add("Default");
                        defaultI.add("Moderator");
                        defaultI.add("Builder");
                    }
                    if (rank.equals("Owner")) {
                        defaultP.add("minecraft.command.advancement");
                        defaultP.add("minecraft.command.clear");
                        defaultP.add("minecraft.command.debug");
                        defaultP.add("minecraft.command.defaultgamemode");
                        defaultP.add("minecraft.command.deop");
                        defaultP.add("minecraft.command.difficulty");
                        defaultP.add("minecraft.command.effect");
                        defaultP.add("minecraft.command.gamerule");
                        defaultP.add("minecraft.command.list");
                        defaultP.add("minecraft.command.op");
                        defaultP.add("minecraft.command.playsound");
                        defaultP.add("minecraft.command.save-all");
                        defaultP.add("minecraft.command.save-off");
                        defaultP.add("minecraft.command.save-on");
                        defaultP.add("minecraft.command.say");
                        defaultP.add("minecraft.command.scoreboard");
                        defaultP.add("minecraft.command.seed");
                        defaultP.add("minecraft.command.setblock");
                        defaultP.add("minecraft.command.fill");
                        defaultP.add("minecraft.command.setidletimeout");
                        defaultP.add("minecraft.command.setworldspawn");
                        defaultP.add("minecraft.command.spawnpoint");
                        defaultP.add("minecraft.command.spreadplayers");
                        defaultP.add("minecraft.command.stop");
                        defaultP.add("minecraft.command.summon");
                        defaultP.add("minecraft.command.tellraw");
                        defaultP.add("minecraft.command.testfor");
                        defaultP.add("minecraft.command.testforblock");
                        defaultP.add("minecraft.command.time");
                        defaultP.add("minecraft.command.toggledownfall");
                        defaultP.add("minecraft.command.teleport");
                        defaultP.add("minecraft.command.weather");
                        defaultP.add("minecraft.command.whitelist");

                        defaultI.add("Admin");
                        defaultI.add("Moderator");
                        defaultI.add("Default");
                        defaultI.add("Builder");
                    }
                    if (rank.equals("Default")) {
                        world.set(rank + ".default", true);
                    } else {
                        world.set(rank + ".default", false);
                    }
                    world.set(rank + ".permissions", defaultP);
                    world.set(rank + ".inheritance", defaultI);
                    world.set(rank + ".weight", getDefaultWeight(rank));
                }
                usersFile.getConfig().createSection("User-List");
                usersFile.saveConfig();
                toGenerate.saveConfig();
            }
        }
    }

    public void setWeight(String group, String world, int weight) {
        DataManager dm = new DataManager();
        FileManager toGenerate = dm.getGroups(world);
        FileConfiguration fc = toGenerate.getConfig();
        fc.set(group + ".weight", weight);
        toGenerate.saveConfig();
        MyPermissions.getInstance().getLogger().info("- Changed group " + '"' + group + '"' + " weight in world " + '"' + world + '"' + " to " + weight);
    }

    public void generateNewGroup(String group, String world, String... inheritance) {
        DataManager dm = new DataManager();
        FileManager toGenerate = dm.getGroups(world);
        List<String> dperm = new ArrayList<>();
        List<String> dinher = new ArrayList<>();
        if (inheritance != null) {
            Collections.addAll(dinher, inheritance);
        }
        FileConfiguration fc = toGenerate.getConfig();
        fc.set(group + ".permissions", dperm);
        fc.set(group + ".inheritance", dinher);
        fc.set(group + ".default", false);
        fc.set(group + ".weight", 0);
        toGenerate.saveConfig();
        MyPermissions.getInstance().getLogger().info("- Generated new group " + '"' + group + '"' + " in world " + '"' + world + '"');
    }

    public void deleteGroup(String group, String world) {
        DataManager dm = new DataManager();
        FileManager toGenerate = dm.getGroups(world);
        toGenerate.getConfig().set(group, null);
        toGenerate.saveConfig();
        MyPermissions.getInstance().getLogger().info("[%s] - Deleted group " + '"' + group + '"' + " from world " + '"' + world + '"');
    }

    public void generateUserFile() {
        UUID id = p.getUniqueId();
        for (String w : getWorlds()) {
            FileManager usersFile = MyPermissions.getInstance().getFileList().find("Users", "worlds/" + w);
            if (!usersFile.getConfig().getConfigurationSection("User-List").getKeys(false).contains(id.toString())) {
                FileConfiguration user = usersFile.getConfig();
                List<String> dperm = new ArrayList<>();
                List<String> nsg = new ArrayList<>();
                user.set("User-List." + id.toString() + ".username", p.getName());
                user.set("User-List." + id.toString() + ".permissions", dperm);
                if (p.isOp()) {
                    user.set("User-List." + id.toString() + ".group", "Operator");
                } else {
                    user.set("User-List." + id.toString() + ".group", defaultWorldGroup(w));
                }
                user.set("User-List." + id.toString() + ".sub-groups", nsg);
                usersFile.saveConfig();
            }
        }
    }

    private int getDefaultWeight(String group) {
        int result = 0;
        switch (group) {
            case "Moderator":
            case "Builder":
                result = 1;
                break;
            case "Admin":
                result = 2;
                break;
            case "Owner":
                result = 3;
                break;
            case "Operator":
                result = 4;
                break;

        }
        return result;
    }

    /*
    private String getDefaultColors(String group) {
        String result = "&f";
        switch (group) {
            case "Default":
                result = "&7";
                break;
            case "Moderator":
                result = "&5";
                break;
            case "Builder":
                result = "&b&o";
                break;
            case "Admin":
                result = "&c";
                break;
            case "Owner":
                result = "&4&o";
                break;
            case "Operator":
                result = "&c&l";
                break;

        }
        return result;
    }

     */

    private String[] getDefaultRanks() {
        List<String> list = new ArrayList<>();
        list.add("Default");
        list.add("Moderator");
        list.add("Builder");
        list.add("Admin");
        list.add("Owner");
        list.add("Operator");
        return list.toArray(new String[0]);
    }

    public String defaultWorldGroup(String world) {
        DataManager dm = new DataManager();
        FileManager groups = dm.getGroups(world);
        FileConfiguration allG = groups.getConfig();
        for (String g : allG.getKeys(false)) {
            if (allG.getBoolean(g + ".default")) {
                return g;
            }
        }
        MyPermissions.getInstance().getLogger().info("[%s] - No default group found. Please set a value to true amongst one of the groups.");
        return null;
    }

    public String[] getWorlds() {
        List<String> list = new ArrayList<>();
        for (World w : Bukkit.getServer().getWorlds()) {
            list.add(w.getName());
        }
        return list.toArray(new String[0]);
    }

    public File getWorldContainer() {
        final File dir = MyPermissions.getInstance().getDataFolder();
        return new File(dir.getParentFile().getPath(), MyPermissions.getInstance().getName() + "/worlds/");
    }

    public OfflinePlayer getUser(String name) {
        OfflinePlayer result = null;
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName().equals(name)) {
                result = player;
                break;
            }
        }
        return result;
    }

    public void updateUsername(Player p) {
        DataManager dm = new DataManager();
        FileManager users = dm.getUsers(p.getWorld().getName());
        FileConfiguration fc = users.getConfig();
            fc.set("User-List." + p.getUniqueId().toString() + ".username", p.getName());
            users.saveConfig();
    }

    public UUID usernameToUUID(String username) {
        DataManager dm = new DataManager();
        List<String> worlds = Arrays.asList(getWorlds());
        FileManager world = dm.getUsers(worlds.get(0));
        FileConfiguration w = world.getConfig();
        UUID result = null;
        for (String user : w.getConfigurationSection("User-List").getKeys(false)) {
            if (w.getString("User-List." + user + ".username").equals(username)) {
                result = UUID.fromString(user);
            }
        }
        if (result == null) {
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                if (player.getName().equals(username)) {
                    result = player.getUniqueId();
                    break;
                }
            }
        }
        return result;
    }

    public String usernameFromUUID(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        return player.getName() != null ? player.getName() : null;
    }

}
