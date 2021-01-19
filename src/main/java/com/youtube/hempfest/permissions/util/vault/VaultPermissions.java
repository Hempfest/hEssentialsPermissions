package com.youtube.hempfest.permissions.util.vault;

import com.youtube.hempfest.permissions.HempfestPermissions;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;

public class VaultPermissions extends Permission {

    private final PermissionHook listener = HempfestPermissions.getInstance().listener;

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

        return listener.playerHas(player, world, permission);
    }

    @Override
    public boolean playerAdd(String world, String playerName, String permission) {
        return true;
    }

    @Override
    public boolean playerAdd(String world, OfflinePlayer player, String permission) {
        listener.playerGive(player, world, permission);
        return true;
    }

    @Override
    public boolean playerRemove(String world, String playerName, String permission) {
        return false;
    }

    @Override
    public boolean playerRemove(String world, OfflinePlayer player, String permission) {
        listener.playerTake(player, world, permission);
        return false;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        return listener.groupHas(group, world, permission);
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        listener.groupGive(group, world, permission);
        return true;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        listener.groupTake(group, world, permission);
        return true;
    }

    @Override
    public boolean playerInGroup(String world, String playerName, String group) {
        return false;
    }

    @Override
    public boolean playerInGroup(String world, OfflinePlayer player, String group) {
        return listener.playerInGroup(player, world, group);
    }

    @Override
    public boolean playerAddGroup(String world, String playerName, String group) {
        return false;
    }

    @Override
    public boolean playerAddGroup(String world, OfflinePlayer player, String group) {
        listener.playerGiveGroup(player, world, group);
        return true;
    }

    @Override
    public boolean playerRemoveGroup(String world, String playerName, String group) {
        return false;
    }

    @Override
    public boolean playerRemoveGroup(String world, OfflinePlayer player, String group) {
        listener.playerTakeGroup(player,world, group);
        return true;
    }

    @Override
    public String[] getPlayerGroups(String world, String playerName) {
        return new String[0];
    }

    @Override
    public String[] getPlayerGroups(String world, OfflinePlayer player) {
        return listener.getGroups(player, world);
    }

    @Override
    public String getPrimaryGroup(String world, OfflinePlayer player) {
        return listener.getGroup(player, world);
    }

    @Override
    public String getPrimaryGroup(String world, String playerName) {
        return null;
    }

    @Override
    public String[] getGroups() {
        return listener.getAllGroups();
    }

    @Override
    public boolean hasGroupSupport() {
        return true;
    }
}
