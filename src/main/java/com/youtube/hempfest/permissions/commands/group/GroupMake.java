package com.youtube.hempfest.permissions.commands.group;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.yml.Config;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupMake extends BukkitCommand {


    public GroupMake(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
        super(name, description, usageMessage, aliases);
        setPermission(permission);
    }

    private void sendMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String notPlayer() {
        return String.format("[%s] - You aren't a player..", HempfestPermissions.getInstance().getDescription().getName());
    }
    private final List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> result = new ArrayList<>();
        if (args.length == 2) {
            arguments.clear();
            for (World w : Bukkit.getWorlds()) {
                arguments.add(w.getName());
            }
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        UtilityManager um = new UtilityManager();
        PermissionHook listener = new PermissionHook();
        if (!(commandSender instanceof Player)) {
            int length = args.length;
            if (length == 0) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <inheritance?>");
                return true;
            }

            if (length == 1) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <inheritance?>");
                return true;
            }

            if (length == 2) {
                String groupName = args[0];
                String worldName = args[1];
                DataManager dm = new DataManager();
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                Config groups = dm.getGroups(worldName);
                FileConfiguration fc = groups.getConfig();
                if (fc.getKeys(false).contains(groupName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " already exists in world " + '"' + worldName + '"');
                    return true;
                }
                um.generateNewGroup(groupName, worldName);
                sendMessage(commandSender, um.prefix + "&a&oYou created the group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
                return true;
            }

            if (length == 3) {
                String groupName = args[0];
                String worldName = args[1];
                String inherit = args[2];
                DataManager dm = new DataManager();
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                Config groups = dm.getGroups(worldName);
                FileConfiguration fc = groups.getConfig();
                if (fc.getKeys(false).contains(groupName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " already exists in world " + '"' + worldName + '"');
                    return true;
                }
                um.generateNewGroup(groupName, worldName, inherit);
                sendMessage(commandSender, um.prefix + "&a&oYou created the group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
                sendMessage(commandSender, um.prefix + "&e&oInheritance default: &f&n" + inherit);
                return true;
            }
            return true;
        }

        /*
        // VARIABLE CREATION
        //  \/ \/ \/ \/ \/ \/
         */
        int length = args.length;
        Player p = (Player) commandSender;


        /*
        //  /\ /\ /\ /\ /\ /\
        //
         */
        if (!p.hasPermission(this.getPermission())) {
            sendMessage(p, um.prefix + "&c&oYou do not have permission " + '"' + this.getPermission() + '"');
            return true;
        }
        if (length == 0) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <inheritance?>");
            return true;
        }

        if (length == 1) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <inheritance?>");
            return true;
        }

        if (length == 2) {
            String groupName = args[0];
            String worldName = args[1];
            DataManager dm = new DataManager();
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            Config groups = dm.getGroups(worldName);
            FileConfiguration fc = groups.getConfig();
            if (fc.getKeys(false).contains(groupName)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " already exists in world " + '"' + worldName + '"');
                return true;
            }
            um.generateNewGroup(groupName, worldName);
            sendMessage(p, um.prefix + "&a&oYou created the group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
            return true;
        }

        if (length == 3) {
            String groupName = args[0];
            String worldName = args[1];
            String inherit = args[2];
            DataManager dm = new DataManager();
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            Config groups = dm.getGroups(worldName);
            FileConfiguration fc = groups.getConfig();
            if (fc.getKeys(false).contains(groupName)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " already exists in world " + '"' + worldName + '"');
                return true;
            }
            um.generateNewGroup(groupName, worldName, inherit);
            sendMessage(p, um.prefix + "&a&oYou created the group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
            sendMessage(p, um.prefix + "&e&oInheritance default: &f&n" + inherit);
            return true;
        }
        return true;
    }
}
