package com.youtube.hempfest.permissions.commands.group;

import com.github.sanctum.labyrinth.data.FileManager;
import com.github.sanctum.labyrinth.library.TextLib;
import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.gui.GUI;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GroupSetWeight extends BukkitCommand {


    public GroupSetWeight(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
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
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
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
        if (args.length == 3) {
            arguments.clear();
            for (int i = 0; i < 100; i++) {
                arguments.add(i + "");
            }
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return super.tabComplete(sender, alias, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String commandLabel, String[] args) {
        UtilityManager um = MyPermissions.getInstance().getManager();
        if (!(commandSender instanceof Player)) {
            int length = args.length;
            if (length == 0) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <weight>");
                return true;
            }

            if (length == 1) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <weight>");
                return true;
            }

            if (length == 2) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <weight>");
                return true;
            }

            if (length == 3) {
                String groupname = args[0];
                String worldName = args[1];
                try {
                    Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sendMessage(commandSender, "Invalid group weight.");
                    return true;
                }
                int weight = Integer.parseInt(args[2]);
                DataManager dm = new DataManager();
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                FileManager groups = dm.getGroups(worldName);
                if (!groups.getConfig().getKeys(false).contains(groupname)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + groupname + '"' + " doesn't exist in world " + '"' + worldName + '"');
                    return true;
                }
                um.setWeight(groupname, worldName, weight);
                sendMessage(commandSender, "Set the weight of group " + groupname + " in world " + worldName + " to " + weight);
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
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <weight>");
            return true;
        }

        if (length == 1) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <weight>");
            return true;
        }

        if (length == 2) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName> <weight>");
            return true;
        }

        if (length == 3) {
            String groupname = args[0];
            String worldName = args[1];
            try {
                Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sendMessage(p, "&cInvalid group weight.");
                return true;
            }
            int weight = Integer.parseInt(args[2]);
            DataManager dm = new DataManager();
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            FileManager groups = dm.getGroups(worldName);
            if (!groups.getConfig().getKeys(false).contains(groupname)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + groupname + '"' + " doesn't exist in world " + '"' + worldName + '"');
                return true;
            }
            um.setWeight(groupname, worldName, weight);
            sendMessage(p, "&b&oSet the weight of group &f" + groupname + " &b&oin world &f" + worldName + " &b&oto &f" + weight);
            return true;
        }


        return false;
    }
}
