package com.youtube.hempfest.permissions.util.events;

import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.event.HandlerList;

public class UserPermissionUpdateEvent extends PermissionUpdateEvent {

	private static final HandlerList handlers = new HandlerList();

	private final String uuid;

	private final String world;

	private final List<String> permissions;

	private final PermissionUpdateType type;

	public UserPermissionUpdateEvent(PermissionUpdateType type, String uuid, String world, String... permissions) {
		this.type = type;
		this.uuid = uuid;
		this.world = world;
		this.permissions = Arrays.asList(permissions);
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public String[] getHolderPerms() {
		return MyPermissions.getInstance().getPermissionHook().playerPermissions(UUID.fromString(uuid), world);
	}

	public String getHolder() {
		return uuid;
	}

	public PermissionUpdateType getType() {
		return type;
	}

	public String getWorld() {
		return world;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
