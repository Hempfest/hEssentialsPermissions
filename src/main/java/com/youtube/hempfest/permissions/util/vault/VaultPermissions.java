package com.youtube.hempfest.permissions.util.vault;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;

public class VaultPermissions extends Permission {
    
    @Override
    public String getName() {
        return HempfestPermissions.getInstance().getDescription().getName();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSuperPermsCompat() {
        return true;
    }

    @Override
    public boolean playerHas(String world, String player, String permission) {
        return false;
    }

    @Override
    public boolean playerHas(String world, OfflinePlayer player, String permission) {
        PermissionHook listener = new PermissionHook();
        return listener.playerHas(player, world, permission);
    }

    @Override
    public boolean playerAdd(String world, String playerName, String permission) {
        PermissionHook listener = new PermissionHook();
        return true;
    }

    @Override
    public boolean playerAdd(String world, OfflinePlayer player, String permission) {
        PermissionHook listener = new PermissionHook();
        listener.playerGive(player, world, permission);
        return true;
    }

    @Override
    public boolean playerRemove(String world, String playerName, String permission) {
        PermissionHook listener = new PermissionHook();
        return false;
    }

    @Override
    public boolean playerRemove(String world, OfflinePlayer player, String permission) {
        PermissionHook listener = new PermissionHook();
        listener.playerTake(player, world, permission);
        return false;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        PermissionHook listener = new PermissionHook();
        return listener.groupHas(group, world, permission);
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        PermissionHook listener = new PermissionHook();
        listener.groupGive(group, world, permission);
        return true;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        PermissionHook listener = new PermissionHook();
        listener.groupTake(group, world, permission);
        return true;
    }

    @Override
    public boolean playerInGroup(String world, String playerName, String group) {
        PermissionHook listener = new PermissionHook();
        return false;
    }

    @Override
    public boolean playerInGroup(String world, OfflinePlayer player, String group) {
        PermissionHook listener = new PermissionHook();
        return listener.playerInGroup(player, world, group);
    }

    @Override
    public boolean playerAddGroup(String world, String playerName, String group) {
        PermissionHook listener = new PermissionHook();
        return false;
    }

    @Override
    public boolean playerAddGroup(String world, OfflinePlayer player, String group) {
        PermissionHook listener = new PermissionHook();
        listener.playerGiveGroup(player, world, group);
        return true;
    }

    @Override
    public boolean playerRemoveGroup(String world, String playerName, String group) {
        PermissionHook listener = new PermissionHook();
        return false;
    }

    @Override
    public boolean playerRemoveGroup(String world, OfflinePlayer player, String group) {
        PermissionHook listener = new PermissionHook();
        listener.playerTakeGroup(player,world, group);
        return true;
    }

    @Override
    public String[] getPlayerGroups(String world, String playerName) {
        PermissionHook listener = new PermissionHook();
        return new String[0];
    }

    @Override
    public String[] getPlayerGroups(String world, OfflinePlayer player) {
        PermissionHook listener = new PermissionHook();
        return listener.getGroups(player, world);
    }

    @Override
    public String getPrimaryGroup(String world, OfflinePlayer player) {
        UtilityManager um = new UtilityManager();
        PermissionHook listener = new PermissionHook();
        return listener.getGroup(player, world);
    }

    @Override
    public String getPrimaryGroup(String world, String playerName) {
        PermissionHook listener = new PermissionHook();
        return null;
    }

    @Override
    public String[] getGroups() {
        PermissionHook listener = new PermissionHook();
        return listener.getAllGroups();
    }

    @Override
    public boolean hasGroupSupport() {
        return true;
    }
}
