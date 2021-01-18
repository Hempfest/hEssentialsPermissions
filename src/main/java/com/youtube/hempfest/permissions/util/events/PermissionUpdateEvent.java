package com.youtube.hempfest.permissions.util.events;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionUpdateEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private boolean logPrinting;

	private final List<String> permsAdded = new ArrayList<>();

	private final List<String> permsRemoved = new ArrayList<>();

	private final List<PermissionUpdateLog> logs = new ArrayList<>();

	public List<String> getPermsAdded() {
		return permsAdded;
	}

	public List<String> getPermsRemoved() {
		return permsRemoved;
	}

	public List<PermissionUpdateLog> getLogs() {
		return logs;
	}

	public void query() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (HempfestPermissions.getInstance().playerPermissions.containsKey(p.getUniqueId())) {
				PermissionAttachment attachment = HempfestPermissions.getInstance().playerPermissions.get(p.getUniqueId());
				int rem = 0;
				for (Map.Entry<String, Boolean> e : attachment.getPermissions().entrySet()) {
					if (!Arrays.asList(HempfestPermissions.getInstance().listener.playerPermissions(p.getUniqueId(), p.getWorld().getName())).contains(e.getKey())) {
						attachment.unsetPermission(e.getKey());
						permsRemoved.add(e.getKey());
						rem++;
					}
				}
				int add = 0;
				for (String perm : HempfestPermissions.getInstance().listener.playerPermissions(p.getUniqueId(), p.getWorld().getName())) {
					if (!attachment.getPermissions().containsKey(perm)) {
						attachment.setPermission(perm, true);
						permsAdded.add(perm);
						add++;
					}
				}
				this.logs.add(new PermissionUpdateLog("Player " + p.getName() + " had (" + add + ") permissions added and (" + rem + ") permissions removed."));
				HempfestPermissions.getInstance().playerPermissions.put(p.getUniqueId(), attachment);
				p.updateCommands();
			}
		}
		if (logPrinting) {
			for (PermissionUpdateLog log : this.logs) {
				log.sendToConsole();
			}
		}
	}


	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}


	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}

	public void setLogPrinting(boolean logPrinting) {
		this.logPrinting = logPrinting;
	}

	public boolean isLogPrinting() {
		return logPrinting;
	}
}
