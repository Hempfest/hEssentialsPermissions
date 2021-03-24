package com.youtube.hempfest.permissions.commands.user;

import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPermissionRemove extends BukkitCommand {


    public UserPermissionRemove(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
        super(name, description, usageMessage, aliases);
        setPermission(permission);
    }

    private void sendMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String notPlayer() {
        return String.format("[%s] - You aren't a player..", MyPermissions.getInstance().getDescription().getName());
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
        if (args.length == 3) {
            arguments.clear();
            arguments.addAll(MyPermissions.getInstance().getManager().getPermissionsLibrary());
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        if (args.length == 4) {
            arguments.clear();
            arguments.addAll(MyPermissions.getInstance().getManager().getPermissionsLibrary());
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[3].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        if (args.length == 5) {
            arguments.clear();
            arguments.addAll(MyPermissions.getInstance().getManager().getPermissionsLibrary());
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[4].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return super.tabComplete(sender, alias, args);
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        UtilityManager um = MyPermissions.getInstance().getManager();
        PermissionHook listener = new PermissionHook();
        if (!(commandSender instanceof Player)) {
            int length = args.length;
            if (length == 0) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <perm1?+>");
                return true;
            }

            if (length == 1) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <perm1?+>");
                return true;
            }

            if (length == 2) {
                sendMessage(commandSender, um.prefix + "&c&oYou must enter a permission node(s).");
                return true;
            }

            if (length == 3) {
                String playerName = args[0];
                String worldName = args[1];
                String permission = args[2];
                if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                    return true;
                }

                List<String> permList = Arrays.asList(listener.playerDirectPermissions(um.usernameToUUID(playerName), worldName));
                if (!permList.contains(permission)) {
                    sendMessage(commandSender, um.prefix + "&c&oPlayer " + '"' + playerName + '"' + " already has no access to permission " + '"' + permission + '"' + " in world " + '"' + worldName + '"');
                    return true;
                }
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                OfflinePlayer player = Bukkit.getOfflinePlayer(um.usernameToUUID(playerName));
                listener.playerTake(player, worldName, permission);
                sendMessage(commandSender, um.prefix + "&d&oYou just removed permission &f" + '"' + permission + '"' + " &d&ofrom player &f" + '"' + playerName + '"' + " &d&oin world &f" + '"' + worldName + '"');
                return true;
            }

            if (length == 4) {
                String playerName = args[0];
                String worldName = args[1];
                String permission = args[2];
                String permission2 = args[3];
                if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                    return true;
                }
                List<String> results = Arrays.asList(permission, permission2);
                List<String> permList = Arrays.asList(listener.playerDirectPermissions(um.usernameToUUID(playerName), worldName));
                for (String result : results) {
                    if (!permList.contains(result)) {
                        sendMessage(commandSender, um.prefix + "&c&oPlayer " + '"' + playerName + '"' + " already has no access to permission " + '"' + result + '"' + " in world " + '"' + worldName + '"');
                        return true;
                    }
                }
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                List<String> perms = new ArrayList<>(Arrays.asList(permission, permission2));
                OfflinePlayer player = Bukkit.getOfflinePlayer(um.usernameToUUID(playerName));
                listener.playerTake(player, worldName, perms);
                sendMessage(commandSender, um.prefix + "&d&oYou just removed permissions &f" + perms.toString() + " &d&ofrom player &f" + '"' + playerName + '"' + " &d&oin world &f" + '"' + worldName + '"');
                return true;
            }

            if (length == 5) {
                String playerName = args[0];
                String worldName = args[1];
                String permission = args[2];
                String permission2 = args[3];
                String permission3 = args[4];
                if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                    sendMessage(commandSender, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                    return true;
                }
                List<String> results = Arrays.asList(permission, permission2, permission3);
                List<String> permList = Arrays.asList(listener.playerDirectPermissions(um.usernameToUUID(playerName), worldName));
                for (String result : results) {
                    if (!permList.contains(result)) {
                        sendMessage(commandSender, um.prefix + "&c&oPlayer " + '"' + playerName + '"' + " already has no access to permission " + '"' + result + '"' + " in world " + '"' + worldName + '"');
                        return true;
                    }
                }
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                List<String> perms = new ArrayList<>(Arrays.asList(permission, permission2, permission3));
                OfflinePlayer player = Bukkit.getOfflinePlayer(um.usernameToUUID(playerName));
                listener.playerTake(player, worldName, perms);
                sendMessage(commandSender, um.prefix + "&d&oYou just removed permissions &f" + perms.toString() + " &d&ofrom player &f" + '"' + playerName + '"' + " &d&oin world &f" + '"' + worldName + '"');
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
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <perm1?+>");
            return true;
        }

        if (length == 1) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <playerName> <worldName> <perm1?+>");
            return true;
        }

        if (length == 2) {
            sendMessage(p, um.prefix + "&c&oYou must enter a permission node(s).");
            return true;
        }

        if (length == 3) {
            String playerName = args[0];
            String worldName = args[1];
            String permission = args[2];
            if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                sendMessage(p, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                return true;
            }
            if (MyPermissions.getInstance().getPermissionHook().groupWeight(MyPermissions.getInstance().getPermissionHook().getGroup(um.usernameToUUID(playerName), worldName), worldName) >= MyPermissions.getInstance().getPermissionHook().groupWeight(MyPermissions.getInstance().getPermissionHook().getGroup(p, worldName), worldName)) {
                sendMessage(p, um.prefix + "&c&oThis user has higher than or equal to rank priority, unable to modify user permissions.");
                return true;
            }
            List<String> permList = Arrays.asList(listener.playerDirectPermissions(um.usernameToUUID(playerName), worldName));
            if (!permList.contains(permission)) {
                sendMessage(p, um.prefix + "&c&oPlayer " + '"' + playerName + '"' + " already has no access to permission " + '"' + permission + '"' + " in world " + '"' + worldName + '"');
                return true;
            }
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(um.usernameToUUID(playerName));
            listener.playerTake(player, worldName, permission);
            sendMessage(p, um.prefix + "&d&oYou just removed permission &f" + '"' + permission + '"' + " &d&ofrom player &f" + '"' + playerName + '"' + " &d&oin world &f" + '"' + worldName + '"');
            return true;
        }

        if (length == 4) {
            String playerName = args[0];
            String worldName = args[1];
            String permission = args[2];
            String permission2 = args[3];
            if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                sendMessage(p, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                return true;
            }
            if (MyPermissions.getInstance().getPermissionHook().groupWeight(MyPermissions.getInstance().getPermissionHook().getGroup(um.usernameToUUID(playerName), worldName), worldName) >= MyPermissions.getInstance().getPermissionHook().groupWeight(MyPermissions.getInstance().getPermissionHook().getGroup(p, worldName), worldName)) {
                sendMessage(p, um.prefix + "&c&oThis user has higher than or equal to rank priority, unable to modify user permissions.");
                return true;
            }
            List<String> results = Arrays.asList(permission, permission2);
            List<String> permList = Arrays.asList(listener.playerDirectPermissions(um.usernameToUUID(playerName), worldName));
            for (String result : results) {
                if (!permList.contains(result)) {
                    sendMessage(p, um.prefix + "&c&oPlayer " + '"' + playerName + '"' + " already has no access to permission " + '"' + result + '"' + " in world " + '"' + worldName + '"');
                    return true;
                }
            }
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            List<String> perms = new ArrayList<>(Arrays.asList(permission, permission2));
            OfflinePlayer player = Bukkit.getOfflinePlayer(um.usernameToUUID(playerName));
            listener.playerTake(player, worldName, perms);
            sendMessage(p, um.prefix + "&d&oYou just removed permissions &f" + perms.toString() + " &d&ofrom player &f" + '"' + playerName + '"' + " &d&oin world &f" + '"' + worldName + '"');
            return true;
        }

        if (length == 5) {
            String playerName = args[0];
            String worldName = args[1];
            String permission = args[2];
            String permission2 = args[3];
            String permission3 = args[4];
            if (!Arrays.asList(listener.getAllUserNames(worldName)).contains(playerName)) {
                sendMessage(p, um.prefix + "&c&oA user by the name of " + '"' + playerName + '"' + " was not found in world " + '"' + worldName + '"');
                return true;
            }
            if (MyPermissions.getInstance().getPermissionHook().groupWeight(MyPermissions.getInstance().getPermissionHook().getGroup(um.usernameToUUID(playerName), worldName), worldName) >= MyPermissions.getInstance().getPermissionHook().groupWeight(MyPermissions.getInstance().getPermissionHook().getGroup(p, worldName), worldName)) {
                sendMessage(p, um.prefix + "&c&oThis user has higher than or equal to rank priority, unable to modify user permissions.");
                return true;
            }
            List<String> results = Arrays.asList(permission, permission2, permission3);
            List<String> permList = Arrays.asList(listener.playerDirectPermissions(um.usernameToUUID(playerName), worldName));
            for (String result : results) {
                if (!permList.contains(result)) {
                    sendMessage(p, um.prefix + "&c&oPlayer " + '"' + playerName + '"' + " already has no access to permission " + '"' + result + '"' + " in world " + '"' + worldName + '"');
                    return true;
                }
            }
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            List<String> perms = new ArrayList<>(Arrays.asList(permission, permission2, permission3));
            OfflinePlayer player = Bukkit.getOfflinePlayer(um.usernameToUUID(playerName));
            listener.playerTake(player, worldName, perms);
            sendMessage(p, um.prefix + "&d&oYou just removed permissions &f" + perms.toString() + " &d&ofrom player &f" + '"' + playerName + '"' + " &d&oin world &f" + '"' + worldName + '"');
            return true;
        }


        return false;
    }
}
