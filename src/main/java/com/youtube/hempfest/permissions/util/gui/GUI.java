package com.youtube.hempfest.permissions.util.gui;

import com.github.sanctum.labyrinth.gui.InventoryRows;
import com.github.sanctum.labyrinth.gui.builder.PaginatedBuilder;
import com.github.sanctum.labyrinth.gui.builder.PaginatedClick;
import com.github.sanctum.labyrinth.gui.builder.PaginatedClose;
import com.github.sanctum.labyrinth.gui.builder.PaginatedMenu;
import com.github.sanctum.labyrinth.gui.menuman.Menu;
import com.github.sanctum.labyrinth.gui.menuman.MenuBuilder;
import com.github.sanctum.labyrinth.library.StringUtils;
import com.youtube.hempfest.permissions.MyPermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GUI {

	private static final Map<PagedMenu, UUID> util = new HashMap<>();

	private static final NamespacedKey addonKey = new NamespacedKey(MyPermissions.getInstance(), "permission_node");

	public static NamespacedKey getAddonKey() {
		return addonKey;
	}

	public static UUID getId(PagedMenu type) {
		return util.get(type);
	}

	protected static List<String> color(String... text) {
		ArrayList<String> convert = new ArrayList<>();
		for (String t : text) {
			convert.add(StringUtils.translate(t));
		}
		return convert;
	}

	private static ItemStack getLeft() {
		ItemStack left = new ItemStack(Material.DARK_OAK_BUTTON);
		ItemMeta meta = left.getItemMeta();
		meta.setDisplayName(StringUtils.translate("&cPrevious page"));
		left.setItemMeta(meta);
		return left;
	}

	private static ItemStack getRight() {
		ItemStack left = new ItemStack(Material.DARK_OAK_BUTTON);
		ItemMeta meta = left.getItemMeta();
		meta.setDisplayName(StringUtils.translate("&aNext page"));
		left.setItemMeta(meta);
		return left;
	}

	private static ItemStack getBack() {
		ItemStack left = new ItemStack(Material.BARRIER);
		ItemMeta meta = left.getItemMeta();
		meta.setDisplayName(StringUtils.translate("&3Go back."));
		left.setItemMeta(meta);
		return left;
	}

	private static String color(String text) {
		return StringUtils.translate(text);
	}

	/**
	 * A multi-paged GUI screen.
	 */
	public enum PagedMenu {
		GROUP_GIVE, GROUP_TAKE, USER_GIVE, USER_TAKE;

		public @NotNull
		PaginatedMenu get(String group, String world) {
			PaginatedMenu menu;
			LinkedList<String> append;
			PaginatedBuilder builder;
			switch (this) {
				case USER_GIVE:
				case USER_TAKE:
				case GROUP_GIVE:
					append = new LinkedList<>(MyPermissions.getInstance().getManager().getPermissionsLibrary());
					builder = new PaginatedBuilder(MyPermissions.getInstance())
							.setTitle(color("&3&o" + group + " &6permission permitting &3&o»"))
							.setAlreadyFirst(color("&c&oYou are already on the first page of permissions."))
							.setAlreadyLast(color("&c&oYou are already on the last page of permissions."))
							.setNavigationLeft(getLeft(), 48, PaginatedClick::sync)
							.setNavigationRight(getRight(), 50, PaginatedClick::sync)
							.setNavigationBack(getBack(), 49, click -> SingleMenu.GROUP_EDIT.get(group, world).open(click.getPlayer()))
							.setSize(InventoryRows.SIX)
							.setCloseAction(PaginatedClose::clear)
							.setupProcess(element -> element.applyLogic(e -> {
								if (GUI.getId(PagedMenu.GROUP_GIVE).equals(e.getId())) {
									e.buildItem(() -> {

										ItemStack i = e.getItem();
										i.setType(Material.PAPER);
										i.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
										i.removeEnchantment(Enchantment.VANISHING_CURSE);
										ItemMeta meta = i.getItemMeta();
										meta.setDisplayName(StringUtils.translate("&2&o" + e.getContext() + " &8&l»"));
										meta.getPersistentDataContainer().set(getAddonKey(), PersistentDataType.STRING, e.getContext());
										meta.setLore(color("&a&oClick to give the permission", "&a&onode to this group."));
										i.setItemMeta(meta);

										return i;
									});
									e.action().setClick(click -> {
										Player p = click.getPlayer();
										String node = click.getClickedItem().getItemMeta().getPersistentDataContainer().get(getAddonKey(), PersistentDataType.STRING);
										assert node != null;
										MyPermissions.getInstance().getPermissionHook().groupGive(group, world, node);
										PagedMenu.GROUP_GIVE.get(group, world).open(p);
									});
								}
							}))
							.addBorder()
							.setBorderType(Material.GRAY_STAINED_GLASS_PANE)
							.setFillType(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
							.fill()
							.collect(new LinkedList<>(append))
							.limit(28);
					break;
				case GROUP_TAKE:
					append = new LinkedList<>(Arrays.asList(MyPermissions.getInstance().getPermissionHook().groupDirectPermissions(group, world)));
					builder = new PaginatedBuilder(MyPermissions.getInstance())
							.setTitle(color("&3&o" + group + " &6permission removal &3&o»"))
							.setAlreadyFirst(color("&c&oYou are already on the first page of permissions."))
							.setAlreadyLast(color("&c&oYou are already on the last page of permissions."))
							.setNavigationLeft(getLeft(), 48, PaginatedClick::sync)
							.setNavigationRight(getRight(), 50, PaginatedClick::sync)
							.setNavigationBack(getBack(), 49, click -> SingleMenu.GROUP_EDIT.get(group, world).open(click.getPlayer()))
							.setSize(InventoryRows.SIX)
							.setCloseAction(PaginatedClose::clear)
							.setupProcess(element -> element.applyLogic(e -> {
								if (GUI.getId(PagedMenu.GROUP_TAKE).equals(e.getId())) {
									e.buildItem(() -> {

										ItemStack i = e.getItem();
										i.setType(Material.PAPER);
										i.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
										i.removeEnchantment(Enchantment.VANISHING_CURSE);
										ItemMeta meta = i.getItemMeta();
										meta.setDisplayName(StringUtils.translate("&3&o" + e.getContext() + " &8&l»"));
										meta.getPersistentDataContainer().set(getAddonKey(), PersistentDataType.STRING, e.getContext());
										meta.setLore(color("&b&oClick to remove the permission", "&b&onode from this group."));
										i.setItemMeta(meta);

										return i;
									});
									e.action().setClick(click -> {
										Player p = click.getPlayer();
										String node = click.getClickedItem().getItemMeta().getPersistentDataContainer().get(getAddonKey(), PersistentDataType.STRING);
										assert node != null;
										MyPermissions.getInstance().getPermissionHook().groupTake(group, world, node);
										PagedMenu.GROUP_TAKE.get(group, world).open(p);
									});
								}
							}))
							.addBorder()
							.setBorderType(Material.GRAY_STAINED_GLASS_PANE)
							.setFillType(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
							.fill()
							.collect(new LinkedList<>(append))
							.limit(28);
					break;

				default:
					throw new IllegalStateException("Unexpected menu type: " + this);
			}
			util.put(this, builder.getId());
			menu = builder.build();
			return menu;
		}
	}

	/**
	 * A single paged GUI screen.
	 */
	public enum SingleMenu {
		GROUP_EDIT, USER_EDIT;

		public @NotNull
		Menu get(String object, String world) {
			MenuBuilder builder = null;
			switch (this) {
				case GROUP_EDIT:
					builder = new MenuBuilder(InventoryRows.ONE, color("&2&oEditing Group " + object + " &3&o»"))
							.cancelLowerInventoryClicks(false)
							.addElement(new ItemStack(Material.WATER_BUCKET))
							.setLore(color("&2&oTake permissions away from group " + object))
							.setText(color("&7[&3&lTake&7]"))
							.setAction(click -> {
								Player p = click.getPlayer();
								PagedMenu.GROUP_TAKE.get(object, world).open(p);
							})
							.assignToSlots(3)
							.addElement(new ItemStack(Material.BUCKET))
							.setLore(color("&a&oGive permissions to group " + object))
							.setText(color("&7[&c&lGive&7]"))
							.setAction(click -> {
								Player p = click.getPlayer();
								PagedMenu.GROUP_GIVE.get(object, world).open(p);
							})
							.assignToSlots(5)
							.setFiller(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
							.setText(" ")
							.set();
					break;
				case USER_EDIT:
					builder = new MenuBuilder(InventoryRows.ONE, color("&2&oManage Essential Addons &0&l»"))
							.cancelLowerInventoryClicks(false)
							.addElement(new ItemStack(Material.WATER_BUCKET))
							.setLore(color("&2&oTake permissions away from group " + object))
							.setText(color("&7[&3&lTake&7]"))
							.setAction(click -> {
								Player p = click.getPlayer();
								PagedMenu.GROUP_TAKE.get(object, world).open(p);
							})
							.assignToSlots(3)
							.addElement(new ItemStack(Material.BUCKET))
							.setLore(color("&a&oGive permissions to group " + object))
							.setText(color("&7[&c&lGive&7]"))
							.setAction(click -> {
								Player p = click.getPlayer();
								PagedMenu.GROUP_GIVE.get(object, world).open(p);
							})
							.assignToSlots(5)
							.setFiller(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE))
							.setText(" ")
							.set();
					break;
				default:
					throw new IllegalStateException("Unexpected menu type: " + this);
			}
			return builder.create(MyPermissions.getInstance());
		}
	}



}
