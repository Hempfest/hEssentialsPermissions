package com.youtube.hempfest.permissions.commands.group;

import com.youtube.hempfest.hempcore.formatting.string.PaginatedAssortment;
import com.youtube.hempfest.permissions.util.UtilityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class Group extends BukkitCommand {

	public Group() {
		super("group");
		setAliases(Collections.singletonList("g"));
		setPermission("hpermissions.group");
	}

	List<String> getMenu() {
		List<String> array = new ArrayList<>();

		array.add("/gaddp <groupName> <worldName> <perm1> <perm2?> <perm3?> - &eAdd permission nodes to a group");
		array.add(" ");
		array.add("/gremp <groupName> <worldName> <perm1> <perm2?> <perm3?> - &eTake permission nodes from a group");
		array.add(" ");
		array.add("/gmake <groupName> <worldName> <inheritance?> - &eMake a group with or without default inheritance in a specified world.");
		array.add(" ");
		array.add("/gdel <groupName> <worldName> - &eDelete a group in a specified world.");
		array.add(" ");
		array.add("/gaddi <groupName> <worldName> <groupToAdd> - &eAdd a group to the specified groups inheritance list");
		array.add(" ");
		array.add("/gtakei <groupName> <worldName> <groupToRemove> - &eRemove a group from the specified groups inheritance list");
		array.add(" ");
		array.add("/glistp <groupName> <worldName> - &eList all group permissions within a specified world.");
		array.add(" ");
		array.add("/glist <worldName> - &eList all groups within a specified world.\n");
		array.add(" ");
		array.add("/gload - &eReload the " + '"' + "Groups" + '"' + " file through-out all worlds.");
		return array;
	}

	private void sendMessage(CommandSender player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	public boolean execute(CommandSender commandSender, String s, String[] args) {
		UtilityManager um = new UtilityManager();
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
			helpAssist.setNavigateCommand("group");
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
				helpAssist.setNavigateCommand("group");
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
