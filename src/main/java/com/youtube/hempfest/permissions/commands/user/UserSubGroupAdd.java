package com.youtube.hempfest.permissions.commands.user;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.yml.Config;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserSubGroupAdd extends BukkitCommand {


    public UserSubGroupAdd(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
        super(name, description, usageMessage, aliases);
        setPermission(permission);
    }

    private void sendMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String notPlayer() {
        return String.format("[%s] - You aren't a player..", HempfestPermissions.getInstance().getDescription().getName());
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        UtilityManager um = new UtilityManager();
        PermissionHook listener = new PermissionHook();
        if (!(commandSender instanceof Player)) {
            int length = args.length;
            if (length == 0) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
                return true;
            }

            if (length == 1) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
                return true;
            }

            if (length == 3) {
                String playerName = args[0];
                String worldName = args[1];
                String groupName = args[2];
                DataManager dm = new DataManager();
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                Config users = dm.getUsers(worldName);
                if (!Arrays.asList(listener.getAllGroups(worldName)).contains(groupName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " doesn't exist in world " + '"' + worldName + '"');
                    return true;
                }
                if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                    return true;
                }
                List<String> sgroups = users.getConfig().getStringList("User-List." + um.usernameToUUID(playerName) + ".sub-groups");
                if (sgroups.contains(groupName)) {
                    sendMessage(commandSender, um.prefix + "&c&oThe player already inherits permissions from the group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
                    return true;
                }
                sgroups.add(groupName);
                users.getConfig().set("User-List." + um.usernameToUUID(playerName) + ".sub-groups", sgroups);
                users.saveConfig();
                sendMessage(commandSender, um.prefix + "&a&oAdded sub-group " + '"' + groupName + '"' + " to player " + '"' + playerName + '"' + " inheritance list in world " + '"' + worldName + '"');
                System.out.println(String.format("[%s] - Added sub-group inheritance " + '"' + groupName + '"' + " to player " + '"' + playerName + '"' + " inheritance list in world " + '"' + worldName + '"', HempfestPermissions.getInstance().getDescription().getName()));
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
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
            return true;
        }

        if (length == 1) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
            return true;
        }

        if (length == 3) {
            String playerName = args[0];
            String worldName = args[1];
            String groupName = args[2];
            DataManager dm = new DataManager();
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            Config users = dm.getUsers(worldName);
            if (!Arrays.asList(listener.getAllGroups(worldName)).contains(groupName)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " doesn't exist in world " + '"' + worldName + '"');
                return true;
            }
            if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                sendMessage(p, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                return true;
            }
            List<String> sgroups = users.getConfig().getStringList("User-List." + um.usernameToUUID(playerName) + ".sub-groups");
            if (sgroups.contains(groupName)) {
                sendMessage(p, um.prefix + "&c&oThe player already inherits permissions from the group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
                return true;
            }
            sgroups.add(groupName);
            users.getConfig().set("User-List." + um.usernameToUUID(playerName) + ".sub-groups", sgroups);
            users.saveConfig();
            sendMessage(p, um.prefix + "&a&oAdded sub-group " + '"' + groupName + '"' + " to player " + '"' + playerName + '"' + " inheritance list in world " + '"' + worldName + '"');
            System.out.println(String.format("[%s] - Added sub-group inheritance " + '"' + groupName + '"' + " to player " + '"' + playerName + '"' + " inheritance list in world " + '"' + worldName + '"', HempfestPermissions.getInstance().getDescription().getName()));
            return true;
        }


        return false;
    }
}
