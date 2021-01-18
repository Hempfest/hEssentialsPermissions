package com.youtube.hempfest.permissions.util.events.misc;

import com.youtube.hempfest.permissions.HempfestPermissions;

public class PermissionUpdateLog {

	private String info;

	public PermissionUpdateLog(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public void OverrideInfo(String text) {
		this.info = text;
	}

	public void sendToConsole() {
		HempfestPermissions.getInstance().getLogger().info(info);
	}

}
