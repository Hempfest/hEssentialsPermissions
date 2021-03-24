package com.youtube.hempfest.permissions;

import com.github.sanctum.labyrinth.data.FileList;
import com.github.sanctum.labyrinth.task.Schedule;
import com.youtube.hempfest.permissions.commands.group.Group;
import com.youtube.hempfest.permissions.commands.group.GroupDelete;
import com.youtube.hempfest.permissions.commands.group.GroupInheritanceAdd;
import com.youtube.hempfest.permissions.commands.group.GroupInheritanceRemove;
import com.youtube.hempfest.permissions.commands.group.GroupList;
import com.youtube.hempfest.permissions.commands.group.GroupLoad;
import com.youtube.hempfest.permissions.commands.group.GroupMake;
import com.youtube.hempfest.permissions.commands.group.GroupPermissionAdd;
import com.youtube.hempfest.permissions.commands.group.GroupPermissionList;
import com.youtube.hempfest.permissions.commands.group.GroupPermissionRemove;
import com.youtube.hempfest.permissions.commands.group.GroupSetWeight;
import com.youtube.hempfest.permissions.commands.user.User;
import com.youtube.hempfest.permissions.commands.user.UserLoad;
import com.youtube.hempfest.permissions.commands.user.UserPermissionAdd;
import com.youtube.hempfest.permissions.commands.user.UserPermissionList;
import com.youtube.hempfest.permissions.commands.user.UserPermissionRemove;
import com.youtube.hempfest.permissions.commands.user.UserSetGroup;
import com.youtube.hempfest.permissions.commands.user.UserSubGroupAdd;
import com.youtube.hempfest.permissions.commands.user.UserSubGroupRemove;
import com.youtube.hempfest.permissions.util.essentials.MyWrapper;
import com.youtube.hempfest.permissions.util.listener.PlayerEventListener;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionBase;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import com.youtube.hempfest.permissions.util.vault.VaultPermissions;
import com.youtube.hempfest.permissions.util.vault.VaultSetup;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class MyPermissions extends JavaPlugin {

	// Instance
	private static MyPermissions instance;
	private VaultPermissions perms;
	private VaultSetup vaultSetup;
	private PermissionHook permissionHook;
	private final Logger log = getLogger();
	private UtilityManager um;
	private FileList FILE_LIST;
	private final PluginManager pm = getServer().getPluginManager();

	// Start server
	public void onEnable() {
		instance = this;
		um = UtilityManager.get();
		registerCommands();
		registerEvents();
		this.FILE_LIST = FileList.search(this);
		permissionHook = new PermissionHook();
		um.generateConfig();
		if (um.runningVault()) {
			this.perms = new VaultPermissions();
			vaultSetup = new VaultSetup(this);
			vaultSetup.hook();
		}
		um.FILE = MyPermissions.getInstance().getFileList().find("Cache", "data");
		log.info("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		log.info("  ███╗   ███╗██╗   ██╗██████╗ ███████╗██████╗ ███╗   ███╗███████╗");
		log.info("  ████╗ ████║╚██╗ ██╔╝██╔══██╗██╔════╝██╔══██╗████╗ ████║██╔════╝");
		log.info("  ██╔████╔██║ ╚████╔╝ ██████╔╝█████╗  ██████╔╝██╔████╔██║███████╗");
		log.info("  ██║╚██╔╝██║  ╚██╔╝  ██╔═══╝ ██╔══╝  ██╔══██╗██║╚██╔╝██║╚════██║");
		log.info("  ██║ ╚═╝ ██║   ██║   ██║     ███████╗██║  ██║██║ ╚═╝ ██║███████║");
		log.info("  ╚═╝     ╚═╝   ╚═╝   ╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝");
		log.info("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		log.info("- Loading worlds: " + Arrays.toString(um.getWorlds()));
		Schedule.sync(um::generateWorlds).wait(5);
		Schedule.sync(this::refreshClients).run();
		if (System.getProperty("OLD") != null && System.getProperty("OLD").equals("TRUE")) {
			log.severe("- RELOAD DETECTED! Shutting down... (You are not supported in the case of corrupt data, reloading is NEVER safe and you should always restart instead.)");
			pm.disablePlugin(this);
		} else {
			System.setProperty("OLD", "FALSE");
		}
		Schedule.sync(() -> {
			if (Bukkit.getPluginManager().isPluginEnabled("myEssentials")) {
				MyWrapper.queue();
			}
		}).wait(3);
	}

	private void refreshClients() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			um.userPermissions.put(p, Arrays.asList(MyPermissions.getInstance().permissionHook.playerPermissions(p, p.getWorld().getName())));
			inject(p);
		}
	}

	public void onDisable() {
		log.info("- Goodbye friends...");

		if (um.runningVault()) {
			vaultSetup.unhook();
		}
		um.userPermissions.clear();

		if (System.getProperty("OLD").equals("FALSE")) {
			System.setProperty("OLD", "TRUE");
		}


	}

	public void inject(Player p) {
		Field permissibleBase;
		try {
			permissibleBase = p.getClass().getSuperclass().getDeclaredField("perm");
		} catch (NoSuchFieldException noSuchFieldException) {
			throw new IllegalStateException("Unable to find field! Library changes detected.", noSuchFieldException);
		}
		permissibleBase.setAccessible(true);
		try {
			permissibleBase.set(p, new PermissionBase(p));
		} catch (IllegalAccessException illegalAccessException) {
			throw new IllegalStateException("Unable to access field! Library changes detected.", illegalAccessException);
		}
		p.updateCommands();
	}

	public static MyPermissions getInstance() {
		return instance;
	}

	public UtilityManager getManager() {
		return um;
	}

	public PermissionHook getPermissionHook() {
		return permissionHook;
	}

	public FileList getFileList() {
		return FILE_LIST;
	}

	public void registerCommand(BukkitCommand command) {
		try {

			final Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(getServer());
			commandMap.register(getName(), command);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void registerEvents() {
		pm.registerEvents(new PlayerEventListener(), this);
	}

	private void registerCommands() {
		registerCommand(new Group());
		registerCommand(new User());
		ArrayList<String> aliases = new ArrayList<>(Arrays.asList("gap", "gaddp"));
		registerCommand(new GroupPermissionAdd("groupaddp", "Base command for giving group permissions.", "mess.group.add.permissions", "", aliases));
		ArrayList<String> aliases2 = new ArrayList<>(Arrays.asList("grp", "gremp"));
		registerCommand(new GroupPermissionRemove("groupremp", "Base command for removing group permissions.", "mess.group.remove.permissions", "", aliases2));
		ArrayList<String> aliases3 = new ArrayList<>(Arrays.asList("gai", "gaddi"));
		registerCommand(new GroupInheritanceAdd("groupaddi", "Base command for adding group inheritance.", "mess.group.add.inheritance", "", aliases3));
		ArrayList<String> aliases4 = new ArrayList<>(Arrays.asList("gri", "gremi"));
		registerCommand(new GroupInheritanceRemove("groupremi", "Base command for removing group inheritance.", "mess.group.add.inheritance", "", aliases4));
		ArrayList<String> aliases5 = new ArrayList<>(Arrays.asList("glp", "glistp"));
		registerCommand(new GroupPermissionList("grouplistp", "Base command for listing group permissions.", "mess.group.list.permissions", "", aliases5));
		ArrayList<String> aliases6 = new ArrayList<>(Arrays.asList("gm", "gmake"));
		registerCommand(new GroupMake("groupmake", "Base command for creating groups.", "mess.group.create", "", aliases6));
		ArrayList<String> aliases7 = new ArrayList<>(Arrays.asList("uap", "uaddp"));
		registerCommand(new UserPermissionAdd("useraddp", "Base command for giving user permissions.", "mess.user.add.permission", "", aliases7));
		ArrayList<String> aliases8 = new ArrayList<>(Arrays.asList("urp", "uremp"));
		registerCommand(new UserPermissionRemove("userremp", "Base command for removing user permissions.", "mess.user.remove.permission", "", aliases8));
		ArrayList<String> aliases9 = new ArrayList<>(Arrays.asList("usg", "usetg"));
		registerCommand(new UserSetGroup("usersetg", "Base command for setting a users primary group.", "mess.user.set.group", "", aliases9));
		ArrayList<String> aliases10 = new ArrayList<>(Arrays.asList("uasg", "uaddsg"));
		registerCommand(new UserSubGroupAdd("useraddsg", "Base command for adding sub-groups to users.", "mess.user.add.group", "", aliases10));
		ArrayList<String> aliases11 = new ArrayList<>(Arrays.asList("ursg", "uremsg"));
		registerCommand(new UserSubGroupRemove("userremsg", "Base command for removing sub-groups from users.", "mess.user.remove.group", "", aliases11));
		ArrayList<String> aliases12 = new ArrayList<>(Arrays.asList("gl", "glist"));
		registerCommand(new GroupList("grouplist", "Base command for listing all groups within a world.", "mess.group.list", "", aliases12));
		ArrayList<String> aliases13 = new ArrayList<>(Collections.singletonList("gload"));
		registerCommand(new GroupLoad("groupload", "Base command for reloading group files.", "mess.group.reload", "", aliases13));
		ArrayList<String> aliases14 = new ArrayList<>(Collections.singletonList("uload"));
		registerCommand(new UserLoad("userload", "Base command for reloading user files.", "mess.user.reload", "", aliases14));
		ArrayList<String> aliases15 = new ArrayList<>(Arrays.asList("gd", "gdel"));
		registerCommand(new GroupDelete("groupdelete", "Base command for deleting groups.", "mess.group.delete", "", aliases15));
		ArrayList<String> aliases16 = new ArrayList<>(Arrays.asList("ulp", "ulistp"));
		registerCommand(new UserPermissionList("userlistp", "Base command for listing user permissions.", "mess.user.list.permissions", "", aliases16));
		ArrayList<String> aliases17 = new ArrayList<>(Arrays.asList("gsw", "gsetw"));
		registerCommand(new GroupSetWeight("groupsetweight", "Base command for setting group weight.", "mess.group.setweight", "", aliases17));
	}

	public VaultPermissions getPerms() {
		return perms;
	}
}
