package com.youtube.hempfest.permissions.util.layout;

import com.youtube.hempfest.permissions.MyPermissions;
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
		MyPermissions.getInstance().getManager().inject(permission);
		try {
			return MyPermissions.getInstance().getManager().userPermissions.get(p).contains(permission) || MyPermissions.getInstance().getManager().userPermissions.get(p).contains("*") || super.hasPermission(permission) || isOp();
		} catch (NullPointerException e) {
			return super.hasPermission(permission);
		}
	}

	@Override
	public boolean hasPermission(Permission perm) {
		MyPermissions.getInstance().getManager().inject(perm.getName());
		try {
			return MyPermissions.getInstance().getManager().userPermissions.get(p).contains(perm.getName()) || MyPermissions.getInstance().getManager().userPermissions.get(p).contains("*") || super.hasPermission(perm) || isOp();
		} catch (NullPointerException e) {
			return super.hasPermission(perm);
		}
	}

}
