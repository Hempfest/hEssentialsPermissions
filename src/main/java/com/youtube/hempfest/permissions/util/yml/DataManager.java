package com.youtube.hempfest.permissions.util.yml;

import com.github.sanctum.labyrinth.data.FileManager;
import com.youtube.hempfest.permissions.MyPermissions;
import org.bukkit.entity.Player;

public class DataManager {

    Player p;

    public DataManager() {}

   public DataManager(Player p) {
       this.p = p;
   }

    public FileManager getUsers(String world) {
        return MyPermissions.getInstance().getFileList().find("Users", "worlds/" + world);
    }

    public FileManager getGroups(String world) {
        return MyPermissions.getInstance().getFileList().find("Groups", "worlds/" + world);
    }

}
