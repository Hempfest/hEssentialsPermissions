package com.youtube.hempfest.permissions.commands.group;

import com.github.sanctum.labyrinth.formatting.component.Text;
import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.yml.Config;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import java.util.LinkedList;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupPermissionList extends BukkitCommand {


    public GroupPermissionList(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
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
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName>");
                return true;
            }

            if (length == 1) {
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName>");
                return true;
            }

            if (length == 2) {
                String groupname = args[0];
                String worldName = args[1];
                DataManager dm = new DataManager();
                List<String> worlds = Arrays.asList(um.getWorlds());
                if (!worlds.contains(worldName)) {
                    sendMessage(commandSender, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                    return true;
                }
                Config groups = dm.getGroups(worldName);
                if (!groups.getConfig().getKeys(false).contains(groupname)) {
                    sendMessage(commandSender, um.prefix + "&c&oA group by the name of " + '"' + groupname + '"' + " doesn't exist in world " + '"' + worldName + '"');
                    return true;
                }
                List<String> inher = groups.getConfig().getStringList(groupname + ".inheritance");
                List<String> perms = groups.getConfig().getStringList(groupname + ".permissions");
                sendMessage(commandSender, um.prefix + "Permissions for group " + '"' + groupname + '"' + " in world " + '"' + worldName + '"' + ": " + perms.toString().replaceAll(",", "&b,&f"));
                if (!inher.isEmpty()) {
                    sendMessage(commandSender, um.prefix + "&e&oGroup " + '"' + groupname + '"' + " also inherits permissions from group(s): &f&n" + inher.toString());
                }
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
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName>");
            return true;
        }

        if (length == 1) {
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <groupName> <worldName>");
            return true;
        }

        if (length == 2) {
            String groupname = args[0];
            String worldName = args[1];
            DataManager dm = new DataManager();
            List<String> worlds = Arrays.asList(um.getWorlds());
            if (!worlds.contains(worldName)) {
                sendMessage(p, um.prefix + "&c&oWorld " + '"' + worldName + '"' + " not found.");
                return true;
            }
            Config groups = dm.getGroups(worldName);
            if (!groups.getConfig().getKeys(false).contains(groupname)) {
                sendMessage(p, um.prefix + "&c&oA group by the name of " + '"' + groupname + '"' + " doesn't exist in world " + '"' + worldName + '"');
                return true;
            }
            List<String> inher = groups.getConfig().getStringList(groupname + ".inheritance");
            List<String> perms = groups.getConfig().getStringList(groupname + ".permissions");
            final List<BaseComponent> textComponents = new LinkedList<>();
            textComponents.add(new Text().textHoverable(um.prefix + "Permissions for group " + '"' + groupname + '"' + " in world " + '"' + worldName + '"' + ": ", "", ""));
            for (String perm : perms) {
                textComponents.add(new Text().textRunnable("", perm, "&b,&f ", "&c&oClick to remove &e&o&n" + perm, "gremp " + groupname + " " + worldName + " " + perm));
            }
            p.spigot().sendMessage(textComponents.toArray(new BaseComponent[0]));
            if (!inher.isEmpty()) {
                sendMessage(p, um.prefix + "&e&oGroup " + '"' + groupname + '"' + " also inherits permissions from group(s): &f&n" + inher.toString());
            }
            return true;
        }


        return false;
    }
}
