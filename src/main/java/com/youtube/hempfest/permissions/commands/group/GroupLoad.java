package com.youtube.hempfest.permissions.commands.group;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.yml.DataManager;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import com.youtube.hempfest.permissions.util.yml.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GroupLoad extends BukkitCommand {


    public GroupLoad(String name, String description, String permission, String usageMessage, ArrayList<String> aliases) {
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
                sendMessage(commandSender, um.prefix + "&c&o/" + commandLabel + " <worldName>");
                DataManager dm = new DataManager();
                Config.get("Config", "data").reload();
                for (String world : um.getWorlds()) {
                    Config groups = dm.getGroups(world);
                    groups.reload();
                    sendMessage(commandSender, um.prefix + "&e&oRe-loaded groups file in world " + '"' + "&f&n" + world + "&e&o" + '"');
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
            sendMessage(p, um.prefix + "&c&o/" + commandLabel + " <worldName>");
            DataManager dm = new DataManager();
            for (String world : um.getWorlds()) {
                Config groups = dm.getGroups(world);
                groups.reload();
                sendMessage(p, um.prefix + "&e&oRe-loaded groups file in world " + '"' + "&f&n" + world + "&e&o" + '"');
            }
            return true;
        }


        return false;
    }
}
