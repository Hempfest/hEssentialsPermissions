package com.youtube.hempfest.permissions.util.yml;

import com.youtube.hempfest.permissions.util.yml.Config;
import org.bukkit.entity.Player;

public class DataManager {

    Player p;

    public DataManager() {}

   public DataManager(Player p) {
       this.p = p;
   }

    public Config getUsers(String world) {
        return new Config("Users", "worlds/" + world);
    }

    public Config getGroups(String world) {
        return new Config("Groups", "worlds/" + world);
    }

}
