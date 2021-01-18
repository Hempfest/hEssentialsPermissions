package com.youtube.hempfest.permissions.util.events;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateLog;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateType;
import org.bukkit.event.HandlerList;

public class GroupPermissionUpdateEvent extends UserPermissionUpdateEvent {

	private final String group;
	private final String world;
	private static final HandlerList handlers = new HandlerList();
	private PermissionUpdateLog log;

	public GroupPermissionUpdateEvent(PermissionUpdateType type, String group, String world, String... permissions) {
		super(type, group, world, permissions);
		this.world = world;
		this.group = group;
		switch (type) {
			case Added:
				this.log = new PermissionUpdateLog(super.getType() + " permission(s) " + super.getPermissions().toString() + " to - group " + '"' + group + '"');
				break;
			case Removed:
				this.log = new PermissionUpdateLog(super.getType() + " permission(s) " + super.getPermissions().toString() + " from - group " + '"' + group + '"');
				break;
		}
	}

	@Override
	public String[] getHolderPerms() {
		return HempfestPermissions.getInstance().listener.groupPermissions(group, world);
	}

	@Override
	public PermissionUpdateLog getLog() {
		return log;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
