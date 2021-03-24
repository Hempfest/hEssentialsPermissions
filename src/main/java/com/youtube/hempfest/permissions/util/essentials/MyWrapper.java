package com.youtube.hempfest.permissions.util.essentials;

import com.github.sanctum.myessentials.api.AddonQuery;

public class MyWrapper {

	public static void queue() {
		AddonQuery.register(MyConnection.class);
	}

}
