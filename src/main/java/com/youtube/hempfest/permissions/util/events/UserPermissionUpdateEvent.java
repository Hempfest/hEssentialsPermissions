package com.youtube.hempfest.permissions.util.events;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateLog;
import com.youtube.hempfest.permissions.util.events.misc.PermissionUpdateType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.event.HandlerList;

public class UserPermissionUpdateEvent extends PermissionUpdateEvent {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled;

	private boolean logPrinting;

	private final String uuid;

	private final String world;

	private final List<String> permissions;

	private final PermissionUpdateType type;

	private PermissionUpdateLog log;

	public UserPermissionUpdateEvent(PermissionUpdateType type, String uuid, String world, String... permissions) {
		this.type = type;
		this.uuid = uuid;
		this.world = world;
		this.permissions = Arrays.asList(permissions);
		switch (type){
			case Removed:
				this.log = new PermissionUpdateLog(type + " permission(s) " + this.permissions.toString() + " from - user " + '"' + uuid + '"');
				break;
			case Added:
				this.log = new PermissionUpdateLog(type + " permission(s) " + this.permissions.toString() + " to - user " + '"' + uuid + '"');
				break;
		}
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public String[] getHolderPerms() {
		return HempfestPermissions.getInstance().listener.playerPermissions(UUID.fromString(uuid), world);
	}

	public String getHolder() {
		return uuid;
	}

	@Override
	public boolean isLogPrinting() {
		return logPrinting;
	}

	@Override
	public void setLogPrinting(boolean logPrinting) {
		this.logPrinting = logPrinting;
	}

	public PermissionUpdateType getType() {
		return type;
	}

	public PermissionUpdateLog getLog() {
		return log;
	}

	public String getWorld() {
		return world;
	}

	@Override
	public void query() {
		super.query();
		if (logPrinting) {
			getLog().sendToConsole();
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
}
