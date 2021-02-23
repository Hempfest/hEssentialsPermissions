package com.youtube.hempfest.permissions.util.layout;

import com.youtube.hempfest.permissions.HempfestPermissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public class PermissionBase extends PermissibleBase {

	private final Player p;

	public PermissionBase(Player p) {
		super(p);
		this.p = p;
	}

	@Override
	public boolean isOp() {
		return super.isOp();
	}

	@Override
	public void setOp(boolean value) {
		super.setOp(value);
	}

	@Override
	public boolean hasPermission(String permission) {
		return HempfestPermissions.getInstance().um.userPermissions.get(p).contains(permission) || HempfestPermissions.getInstance().um.userPermissions.get(p).contains("*") || super.hasPermission(permission) || isOp();
	}

	@Override
	public boolean hasPermission(Permission perm) {
		return HempfestPermissions.getInstance().um.userPermissions.get(p).contains(perm.getName()) || HempfestPermissions.getInstance().um.userPermissions.get(p).contains("*") || super.hasPermission(perm) || isOp();
	}
}
