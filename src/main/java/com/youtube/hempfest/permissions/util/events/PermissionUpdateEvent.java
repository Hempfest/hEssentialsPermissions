package com.youtube.hempfest.permissions.util.events;

import com.youtube.hempfest.permissions.MyPermissions;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PermissionUpdateEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	public void query() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			MyPermissions.getInstance().getManager().userPermissions.put(p, Arrays.asList(MyPermissions.getInstance().getPermissionHook().playerPermissions(p, p.getWorld().getName())));
			p.updateCommands();
		}
	}

	public void query(Player p) {
		MyPermissions.getInstance().getManager().userPermissions.put(p, Arrays.asList(MyPermissions.getInstance().getPermissionHook().playerPermissions(p, p.getWorld().getName())));
		p.updateCommands();
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
}
