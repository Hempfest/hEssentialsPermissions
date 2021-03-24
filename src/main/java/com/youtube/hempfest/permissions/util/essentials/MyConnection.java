package com.youtube.hempfest.permissions.util.essentials;

import com.github.sanctum.myessentials.api.CommandData;
import com.github.sanctum.myessentials.api.EssentialsAddon;
import com.github.sanctum.myessentials.model.CommandBuilder;
import com.youtube.hempfest.permissions.MyPermissions;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.event.Listener;

public class MyConnection extends EssentialsAddon {

	private final Collection<Listener> LISTENERS = new HashSet<>();

	private final Map<CommandData, Class<? extends CommandBuilder>> COMMANDS = new HashMap<>();

	private final Map<Object, Object> DATA = new HashMap<>();

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public EssentialsAddon getInstance() {
		return this;
	}

	@Override
	public String[] getAuthors() {
		return new String[]{"Hempfest"};
	}

	@Override
	public String getAddonName() {
		return "myPermissions";
	}

	@Override
	public String getAddonDescription() {
		return "The connection between myEssentials & myPermissions";
	}

	@Override
	public Collection<Listener> getListeners() {
		return LISTENERS;
	}

	@Override
	public Map<CommandData, Class<? extends CommandBuilder>> getCommands() {
		return COMMANDS;
	}

	@Override
	public Map<Object, Object> getData() {
		return DATA;
	}

	@Override
	protected void apply() {
		LISTENERS.add(new MyListeners());
		Map<String, Integer> GROUP_MAP = new HashMap<>();
		Map<UUID, String> USER_MAP = new HashMap<>();
		for (String w : MyPermissions.getInstance().getManager().getWorlds()) {
			for (String group : MyPermissions.getInstance().getPermissionHook().getAllGroups(w)) {

				GROUP_MAP.put(group, MyPermissions.getInstance().getPermissionHook().groupWeight(group, w));
			}
		}
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			for (String w : MyPermissions.getInstance().getManager().getWorlds()) {
				USER_MAP.put(player.getUniqueId(), MyPermissions.getInstance().getPermissionHook().getGroup(player, w));
				break;
			}
		}
		DATA.put("USER", USER_MAP);
		DATA.put("GROUP", GROUP_MAP);
	}
}
