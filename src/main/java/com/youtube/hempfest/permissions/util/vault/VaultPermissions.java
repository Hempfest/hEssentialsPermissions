package com.youtube.hempfest.permissions.util.vault;

import com.youtube.hempfest.permissions.MyPermissions;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;

public class VaultPermissions extends Permission {

    private final PermissionHook listener = MyPermissions.getInstance().getPermissionHook();

    @Override
    public String getName() {
        return MyPermissions.getInstance().getDescription().getName();
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
        return listener.playerHas(MyPermissions.getInstance().getManager().getUser(player), world, permission);
    }

    @Override
    public boolean playerHas(String world, OfflinePlayer player, String permission) {
        return listener.playerHas(player, world, permission);
    }

    @Override
    public boolean playerAdd(String world, String playerName, String permission) {
        return listener.playerGive(MyPermissions.getInstance().getManager().getUser(playerName), world, permission);
    }

    @Override
    public boolean playerAdd(String world, OfflinePlayer player, String permission) {
        return listener.playerGive(player, world, permission);
    }

    @Override
    public boolean playerRemove(String world, String playerName, String permission) {
        return listener.playerTake(MyPermissions.getInstance().getManager().getUser(playerName), world, permission);
    }

    @Override
    public boolean playerRemove(String world, OfflinePlayer player, String permission) {
        return listener.playerTake(player, world, permission);
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        return listener.groupHas(group, world, permission);
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        return listener.groupGive(group, world, permission);
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        return listener.groupTake(group, world, permission);
    }

    @Override
    public boolean playerInGroup(String world, String playerName, String group) {
        return listener.playerInGroup(MyPermissions.getInstance().getManager().getUser(playerName), world, group);
    }

    @Override
    public boolean playerInGroup(String world, OfflinePlayer player, String group) {
        return listener.playerInGroup(player, world, group);
    }

    @Override
    public boolean playerAddGroup(String world, String playerName, String group) {
        return listener.playerGiveGroup(MyPermissions.getInstance().getManager().getUser(playerName), world, group);
    }

    @Override
    public boolean playerAddGroup(String world, OfflinePlayer player, String group) {
        return listener.playerGiveGroup(player, world, group);
    }

    @Override
    public boolean playerRemoveGroup(String world, String playerName, String group) {
        return listener.playerTakeGroup(MyPermissions.getInstance().getManager().getUser(playerName),world, group);
    }

    @Override
    public boolean playerRemoveGroup(String world, OfflinePlayer player, String group) {
        return listener.playerTakeGroup(player,world, group);
    }

    @Override
    public String[] getPlayerGroups(String world, String playerName) {
        return listener.getGroups(MyPermissions.getInstance().getManager().getUser(playerName), world);
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
        return listener.getGroup(MyPermissions.getInstance().getManager().getUser(playerName), world);
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
