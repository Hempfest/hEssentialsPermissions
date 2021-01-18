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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupInheritanceRemove extends BukkitCommand {


    public GroupInheritanceRemove(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
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
            arguments.addAll(Arrays.asList(listener.getAllGroups(p.getWorld().getName())));
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
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <groupToRem>");
                return true;
            }

            if (length == 1) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <groupToRem>");
                return true;
            }

            if (length == 2) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <groupToRem>");
                return true;
            }

            if (length == 3) {
                String group = args[0];
                String world = args[1];
                String groupToRem = args[2];
                if (!Arrays.asList(listener.getAllGroups(world)).contains(group)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + group + '"' + " doesn't exist in world " + '"' + world + '"');
                    return true;
                }
                if (!Arrays.asList(listener.getAllGroups(world)).contains(groupToRem)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + groupToRem + '"' + " doesn't exist in world " + '"' + world + '"');
                    return true;
                }
                DataManager dm = new DataManager();
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(world)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + world + '"' + " not found.");
                    return true;
                }
                Config groups = dm.getGroups(world);
                List<String> inher = groups.getConfig().getStringList(group + ".inheritance");
                if (!inher.contains(groupToRem)) {
                    sendMessage(commandSender, um.prefix + "&c&oGroup " + '"' + group + '"' + " doesn't inherit permissions from group " + '"' + groupToRem + '"');
                    return true;
                }
                inher.remove(groupToRem);
                groups.getConfig().set(group + ".inheritance", inher);
                groups.saveConfig();
                sendMessage(commandSender, um.prefix + "&a&oYou just removed group-inheritance &f" + groupToRem + " &a&ofrom group &f" + '"' + group + '"' + " &a&oin world &f" + '"' + world + '"');
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
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <groupToRem>");
            return true;
        }

        if (length == 1) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <groupToRem>");
            return true;
        }

        if (length == 2) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <groupToRem>");
            return true;
        }

        if (length == 3) {
            String group = args[0];
            String world = args[1];
            String groupToRem = args[2];
            if (!Arrays.asList(listener.getAllGroups(world)).contains(group)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + group + '"' + " doesn't exist in world " + '"' + world + '"');
                return true;
            }
            if (!Arrays.asList(listener.getAllGroups(world)).contains(groupToRem)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + groupToRem + '"' + " doesn't exist in world " + '"' + world + '"');
                return true;
            }
            DataManager dm = new DataManager();
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(world)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + world + '"' + " not found.");
                return true;
            }
            Config groups = dm.getGroups(world);
            List<String> inher = groups.getConfig().getStringList(group + ".inheritance");
            if (!inher.contains(groupToRem)) {
                sendMessage(p, um.prefix + "&c&oGroup " + '"' + group + '"' + " doesn't inherit permissions from group " + '"' + groupToRem + '"');
                return true;
            }
            inher.remove(groupToRem);
            groups.getConfig().set(group + ".inheritance", inher);
            groups.saveConfig();
            sendMessage(p, um.prefix + "&a&oYou just removed group-inheritance &f" + groupToRem + " &a&ofrom group &f" + '"' + group + '"' + " &a&oin world &f" + '"' + world + '"');
        }


        return false;
    }
}
