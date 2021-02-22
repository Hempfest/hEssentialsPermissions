package com.youtube.hempfest.permissions.util.events;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateType;
import org.bukkit.event.HandlerList;

public class GroupPermissionUpdateEvent extends UserPermissionUpdateEvent {

	private final String group;
	private final String world;
	private static final HandlerList handlers = new HandlerList();

	public GroupPermissionUpdateEvent(PermissionUpdateType type, String group, String world, String... permissions) {
		super(type, group, world, permissions);
		this.world = world;
		this.group = group;
	}

	@Override
	public String[] getHolderPerms() {
		return HempfestPermissions.getInstance().listener.groupPermissions(group, world);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
