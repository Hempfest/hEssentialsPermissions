package com.youtube.hempfest.permissions.util;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import com.youtube.hempfest.permissions.util.yml.Config;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class UtilityManager {
    Player p;

    public String prefix = "&7[&b&ohPermissions&7]&r ";

    public HashMap<Player, String> playerStringMap = new HashMap<>();

    public UtilityManager() {}

    public UtilityManager(Player executor) {
        this.p = executor;
    }

    public boolean runningVault() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault"))
            return true;
        return false;
    }

    public void generateWorlds() {
        for (int j = 0; j < getWorlds().length; j++) {
            String w = getWorlds()[j];
            Config toGenerate = Config.get("Groups", "worlds/" + w);
            Config usersFile = Config.get("Users", "worlds/" + w);
            if (!toGenerate.exists() || toGenerate.getConfig().getKeys(false).isEmpty()) {
                FileConfiguration world = toGenerate.getConfig();
                for (String def : getDefaultRanks()) {
                    List<String> dperm = new ArrayList<>();
                    List<String> dinher = new ArrayList<>();
                    if (def.equals("Moderator")) {
                        dperm.add("hessentials.staff");
                        dperm.add("hessentials.staff.kick");
                        dperm.add("hessentials.staff.kickall");
                        dperm.add("hessentials.staff.day");
                        dperm.add("hessentials.staff.night");
                        dinher.add("Default");
                    }
                    if (def.equals("Builder")) {
                        dperm.add("minecraft.command.gamemode");
                        dinher.add("Default");
                    }
                    if (def.equals("Admin")) {
                        dperm.add("hpermissions.group.add.permissions");
                        dperm.add("hpermissions.group.add.inheritance");
                        dperm.add("hpermissions.group.remove.permissions");
                        dperm.add("hpermissions.group.add.inheritance");
                        dperm.add("hpermissions.group.list.permissions");
                        dperm.add("hpermissions.group.create");
                        dperm.add("hpermissions.user.add.permission");
                        dperm.add("hpermissions.user.remove.permission");
                        dperm.add("hpermissions.user.set.group");
                        dperm.add("hpermissions.user.add.group");
                        dperm.add("hpermissions.user.remove.group");
                        dperm.add("hpermissions.group.list");
                        dperm.add("hpermissions.group.reload");
                        dperm.add("hpermissions.user.reload");
                        dperm.add("hpermissions.group.delete");
                        dperm.add("minecraft.command.ban");
                        dperm.add("minecraft.command.ban-ip");
                        dperm.add("minecraft.command.banlist");
                        dperm.add("minecraft.command.enchant");
                        dperm.add("minecraft.command.kick");
                        dperm.add("minecraft.command.kill");
                        dperm.add("minecraft.command.pardon");
                        dperm.add("minecraft.command.pardon-ip");
                        dinher.add("Default");
                        dinher.add("Builder");
                    }
                    if (def.equals("Owner")) {
                        dperm.add("minecraft.command.advancement");
                        dperm.add("minecraft.command.clear");
                        dperm.add("minecraft.command.debug");
                        dperm.add("minecraft.command.defaultgamemode");
                        dperm.add("minecraft.command.deop");
                        dperm.add("minecraft.command.difficulty");
                        dperm.add("minecraft.command.effect");
                        dperm.add("minecraft.command.gamerule");
                        dperm.add("minecraft.command.list");
                        dperm.add("minecraft.command.op");
                        dperm.add("minecraft.command.playsound");
                        dperm.add("minecraft.command.save-all");
                        dperm.add("minecraft.command.save-off");
                        dperm.add("minecraft.command.save-on");
                        dperm.add("minecraft.command.say");
                        dperm.add("minecraft.command.scoreboard");
                        dperm.add("minecraft.command.seed");
                        dperm.add("minecraft.command.setblock");
                        dperm.add("minecraft.command.fill");
                        dperm.add("minecraft.command.setidletimeout");
                        dperm.add("minecraft.command.setworldspawn");
                        dperm.add("minecraft.command.spawnpoint");
                        dperm.add("minecraft.command.spreadplayers");
                        dperm.add("minecraft.command.stop");
                        dperm.add("minecraft.command.summon");
                        dperm.add("minecraft.command.tellraw");
                        dperm.add("minecraft.command.testfor");
                        dperm.add("minecraft.command.testforblock");
                        dperm.add("minecraft.command.time");
                        dperm.add("minecraft.command.toggledownfall");
                        dperm.add("minecraft.command.teleport");
                        dperm.add("minecraft.command.weather");
                        dperm.add("minecraft.command.whitelist");
                        dinher.add("Admin");
                        dinher.add("Moderator");
                        dinher.add("Default");
                        dinher.add("Builder");
                    }
                    if (def.equals("Default")) {
                        world.set(def + ".default", true);
                    } else {
                        world.set(def + ".default", false);
                    }
                    world.set(def + ".permissions", dperm);
                    world.set(def + ".inheritance", dinher);
                    world.set(def + ".use-suffix", true);
                    world.set(def + ".prefix", "[" + getDefaultColors(def) + def + "&r]");
                }
                usersFile.getConfig().createSection("User-List");
                usersFile.saveConfig();
                toGenerate.saveConfig();
            }
        }
    }

    public void generateNewGroup(String group, String world, String... inheritance) {
        DataManager dm = new DataManager();
        Config toGenerate = dm.getGroups(world);
        List<String> dperm = new ArrayList<>();
        List<String> dinher = new ArrayList<>();
        if (inheritance != null) {
            for (String in : inheritance) {
                dinher.add(in);
            }
        }
        FileConfiguration fc = toGenerate.getConfig();
        fc.set(group + ".permissions", dperm);
        fc.set(group + ".inheritance", dinher);
        fc.set(group + ".default", false);
        fc.set(group + ".use-suffix", true);
        fc.set(group + ".prefix", "[&l" + group + "&r]");
        toGenerate.saveConfig();
        System.out.println(String.format("[%s] - Generated new group " + '"' + group + '"' + " in world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
    }

    public void deleteGroup(String group, String world) {
        DataManager dm = new DataManager();
        Config toGenerate = dm.getGroups(world);
        toGenerate.getConfig().set(group, null);
        toGenerate.saveConfig();
        System.out.println(String.format("[%s] - Deleted group " + '"' + group + '"' + " from world " + '"' + world + '"', HempfestPermissions.getInstance().getDescription().getName()));
    }

    public void generateUserFile() {
        int i = 0;
        UUID id = p.getUniqueId();
        for (String w : getWorlds()) {
            Config usersFile = new Config("Users", "worlds/" + w);
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
                i++;
            }
            if (i == 0)
                break;
        }
    }

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
        Config groups = dm.getGroups(world);
        FileConfiguration allG = groups.getConfig();
        for (String g : allG.getKeys(false)) {
            if (allG.getBoolean(g + ".default")) {
                return g;
            }
        }
        System.out.println(String.format("[%s] - No default group found. Please set a value to true amongst one of the groups.", HempfestPermissions.getInstance().getDescription().getName()));
        return null;
    }

    public String[] getWorlds() {
        List<String> list = new ArrayList<>();
        for (World w : Bukkit.getServer().getWorlds()) {
            list.add(w.getName());
        }
        return list.toArray(new String[0]);
    }

    public List<String> getWorldContainer() {
        final File dir = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        File d = new File(dir.getParentFile().getPath(), HempfestPermissions.getInstance().getName() + "/worlds/");
        List<String> array = new ArrayList<>();
        for (File f : d.listFiles()) {
            array.add(f.getName().replace(".yml", ""));
        }
        return array;
    }

    public void updateUsername(Player p) {
        DataManager dm = new DataManager();
        Config users = dm.getUsers(p.getWorld().getName());
        FileConfiguration fc = users.getConfig();
            fc.set("User-List." + p.getUniqueId().toString() + ".username", p.getName());
            users.saveConfig();
    }

    public UUID usernameToUUID(String username) {
        DataManager dm = new DataManager();
        List<String> worlds = Arrays.asList(getWorlds());
        Config world = dm.getUsers(worlds.get(0));
        FileConfiguration w = world.getConfig();
        UUID result = null;
        for (String user : w.getConfigurationSection("User-List").getKeys(false)) {
            if (w.getString("User-List." + user + ".username").equals(username)) {
                result = UUID.fromString(user);
            }
        }
        return result;
    }

    public String usernameFromUUID(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        if(player == null) return null;
        return player.getName();
    }

}
