package com.youtube.hempfest.permissions.commands.user;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.events.PermissionUpdateEvent;
import com.youtube.hempfest.permissions.util.yml.Config;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserSetGroup extends BukkitCommand {


    public UserSetGroup(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
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
        PermissionHook listener = new PermissionHook();
        Player p = (Player) sender;
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            arguments.clear();
            arguments.addAll(Arrays.asList(listener.getAllUserNames(p.getWorld().getName())));
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
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
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
                return true;
            }

            if (length == 1) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
                return true;
            }

            if (length == 2) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
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
                if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                    return true;
                }
                if (!Arrays.asList(listener.getAllGroups(worldName)).contains(groupName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " doesn't exist in world " + '"' + worldName + '"');
                    return true;
                }
                users.getConfig().set("User-List." + um.usernameToUUID(playerName) + ".group", groupName);
                users.saveConfig();
                System.out.println(String.format("[%s] - Updated user " + '"' + playerName + '"' + " to group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"', HempfestPermissions.getInstance().getDescription().getName()));
                sendMessage(commandSender, um.prefix + "&d&oUpdated user " + '"' + playerName + '"' + " to group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
                if (Bukkit.getOfflinePlayer(um.usernameToUUID(playerName)).isOnline()) {
                    sendMessage(Bukkit.getPlayer(playerName), um.prefix + "&e&oYou've been moved to rank &f&n" + groupName);
                }
                PermissionUpdateEvent event = new PermissionUpdateEvent();
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    event.query();
                }
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

        if (length == 2) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <groupName>");
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
            if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                sendMessage(p, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                return true;
            }
            if (!Arrays.asList(listener.getAllGroups(worldName)).contains(groupName)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + groupName + '"' + " doesn't exist in world " + '"' + worldName + '"');
                return true;
            }
            users.getConfig().set("User-List." + um.usernameToUUID(playerName) + ".group", groupName);
            users.saveConfig();
            System.out.println(String.format("[%s] - Updated user " + '"' + playerName + '"' + " to group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"', HempfestPermissions.getInstance().getDescription().getName()));
            sendMessage(p, um.prefix + "&d&oUpdated user " + '"' + playerName + '"' + " to group " + '"' + groupName + '"' + " in world " + '"' + worldName + '"');
            if (Bukkit.getOfflinePlayer(um.usernameToUUID(playerName)).isOnline()) {
                sendMessage(Bukkit.getPlayer(playerName), um.prefix + "&e&oYou've been moved to rank &f&n" + groupName);
            }
        }

        return false;
    }
}
