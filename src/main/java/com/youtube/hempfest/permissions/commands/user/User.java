package com.youtube.hempfest.permissions.commands.user;

import com.github.sanctum.labyrinth.formatting.string.PaginatedAssortment;
import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.UtilityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class User extends BukkitCommand {

	public User() {
		super("user");
		setAliases(Collections.singletonList("u"));
		setPermission("mess.user");
	}

	List<String> getMenu() {
		List<String> array = new ArrayList<>();
		array.add("/uaddp <playerName> <worldName> <perm1> <perm2?> <perm3?> - &eAdd player specific permissions");
		array.add(" ");
		array.add("/uremp <playerName> <worldName> <perm1> <perm2?> <perm3?> - &eRevoke player specific permissions");
		array.add(" ");
		array.add("/usetg <playerName> <worldName> <groupName> - &eSet a users primary group in a given world.");
		array.add(" ");
		array.add("/uaddsg <playerName> <worldName> <subGroupName> - &eAdd a subgroup to a user in a specific world for them to inherit permissions from");
		array.add(" ");
		array.add("/ulistp <playerName> <worldName> - &eList all user permissions within a specified world.");
		array.add(" ");
		array.add("/uremsg <playerName> <worldName> <subGroupName> - &eRevoke a subgroup from a user in a specific world");
		array.add(" ");
		array.add("/uload - &eReload the " + '"' + "Users" + '"' + " file through-out all worlds.");
		return array;
	}

	private void sendMessage(CommandSender player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] args) {
		UtilityManager um = MyPermissions.getInstance().getManager();
		if (!(commandSender instanceof Player)) {

			return true;
		}

		Player p = (Player) commandSender;
		if (!p.hasPermission(this.getPermission())) {
			sendMessage(p, um.prefix + "&c&oYou do not have permission " + '"' + this.getPermission() + '"');
			return true;
		}
		if (args.length == 0) {
			PaginatedAssortment helpAssist = new PaginatedAssortment(p, getMenu());
			helpAssist.setListTitle("&7&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			helpAssist.setListBorder("&7&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			helpAssist.setNavigateCommand("user");
			helpAssist.setLinesPerPage(3);
			helpAssist.export(1);
			return true;
		}
		if (args.length == 1) {
			try {
				int page = Integer.parseInt(args[0]);
				PaginatedAssortment helpAssist = new PaginatedAssortment(p, getMenu());
				helpAssist.setListTitle("&7&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
				helpAssist.setListBorder("&7&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
				helpAssist.setNavigateCommand("user");
				helpAssist.setLinesPerPage(3);
				helpAssist.export(page);
			} catch (NumberFormatException e) {
				sendMessage(p, "&c&oInvalid page number!");
			}
			return true;
		}
		return false;
	}

}
