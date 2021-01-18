package com.youtube.hempfest.permissions;

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
import com.youtube.hempfest.permissions.commands.user.User;
import com.youtube.hempfest.permissions.commands.user.UserLoad;
import com.youtube.hempfest.permissions.commands.user.UserPermissionAdd;
import com.youtube.hempfest.permissions.commands.user.UserPermissionRemove;
import com.youtube.hempfest.permissions.commands.user.UserSetGroup;
import com.youtube.hempfest.permissions.commands.user.UserSubGroupAdd;
import com.youtube.hempfest.permissions.commands.user.UserSubGroupRemove;
import com.youtube.hempfest.permissions.util.BukkitListener;
import com.youtube.hempfest.permissions.util.UtilityManager;
import com.youtube.hempfest.permissions.util.layout.PermissionHook;
import com.youtube.hempfest.permissions.util.layout.PermissionRefresh;
import com.youtube.hempfest.permissions.util.vault.VaultPermissions;
import com.youtube.hempfest.permissions.util.vault.VaultSetup;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class HempfestPermissions extends JavaPlugin {

	//Instance
	private static HempfestPermissions instance;
	public VaultPermissions perms;
	private final Logger log = Logger.getLogger("Minecraft");
	PluginManager pm = getServer().getPluginManager();
	public HashMap<UUID, PermissionAttachment> playerPermissions = new HashMap<>();

	//Start server
	public void onEnable() {
		setInstance(this);
		registerCommands();
		registerEvents();
		UtilityManager um = new UtilityManager();
		if (um.runningVault()) {
			this.perms = new VaultPermissions();
			VaultSetup listener = new VaultSetup(this);
			listener.hook();
		}
		log.info("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		log.info("  oooo        ooooooooo.                                                ");
		log.info("  `888        `888   `Y88.                                              ");
		log.info("   888 .oo.    888   .d88'  .ooooo.  oooo d8b ooo. .oo.  .oo.    .oooo.o");
		log.info("   888P'Y88b   888ooo88P'  d88' `88b `888''8P `888P'Y88bP'Y88b  d88(  '8");
		log.info("   888   888   888         888ooo888  888      888   888   888  `'Y88b. ");
		log.info("   888   888   888         888    .o  888      888   888   888  o.  )88b");
		log.info("  o888o o888o o888o        `Y8bod8P' d888b    o888o o888o o888o 8''888P'");
		log.info("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		log.info(String.format("[%s] - Loading worlds: " + Arrays.toString(um.getWorlds()), getDescription().getName()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, um::generateWorlds, 5);
		runSuperPerms();
		refreshClients();
	}

	private void refreshClients() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			UtilityManager um = new UtilityManager(p);
			hookPermissions(p);
		}
	}


	private final PermissionHook listener = new PermissionHook();
	public void hookPermissions(Player p) {
		PermissionAttachment attachment = p.addAttachment(HempfestPermissions.getInstance());
		for (String perms : listener.playerPermissions(p, p.getWorld().getName())) {
			attachment.setPermission(perms, true);
		}
		String world = p.getWorld().getName();
		for (String perm : listener.groupPermissions(listener.getGroup(p, world), world)) {
			attachment.setPermission(perm, true);
		}
		p.updateCommands();
		HempfestPermissions.getInstance().playerPermissions.put(p.getUniqueId(), attachment);
	}

	public void onDisable() {
		log.info(String.format("[%s] - Goodbye friends...", getDescription().getName()));
		UtilityManager um = new UtilityManager();
		if (um.runningVault()) {
			VaultSetup listener = new VaultSetup(this);
			listener.unhook();
		}
		playerPermissions.clear();
		um.playerStringMap.clear();
	}

 	private void runSuperPerms() {
		PermissionRefresh syncd = new PermissionRefresh();
		syncd.runTaskTimer(this, 1, 10L);
	}
	

	public static HempfestPermissions getInstance() {
		return instance;
	}

	private void setInstance(HempfestPermissions instance) {
		HempfestPermissions.instance = instance;
	}

	public void registerCommand(BukkitCommand command) {
		try {

			final Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(getServer());
			commandMap.register(command.getLabel(), command);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void registerEvents() {
		pm.registerEvents(new BukkitListener(), this);

	}

	private void registerCommands() {
		registerCommand(new Group());
		registerCommand(new User());
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.addAll(Arrays.asList("gap", "gaddp"));
		registerCommand(new GroupPermissionAdd("groupaddp", "Base command for giving group permissions.", "hpermissions.group.add.permissions", "", aliases));
		ArrayList<String> aliases2 = new ArrayList<String>();
		aliases2.addAll(Arrays.asList("grp", "gremp"));
		registerCommand(new GroupPermissionRemove("groupremp", "Base command for removing group permissions.", "hpermissions.group.remove.permissions", "", aliases2));
		ArrayList<String> aliases3 = new ArrayList<String>();
		aliases3.addAll(Arrays.asList("gai", "gaddi"));
		registerCommand(new GroupInheritanceAdd("groupaddi", "Base command for adding group inheritance.", "hpermissions.group.add.inheritance", "", aliases3));
		ArrayList<String> aliases4 = new ArrayList<String>();
		aliases4.addAll(Arrays.asList("gri", "gremi"));
		registerCommand(new GroupInheritanceRemove("groupremi", "Base command for removing group inheritance.", "hpermissions.group.add.inheritance", "", aliases4));
		ArrayList<String> aliases5 = new ArrayList<String>();
		aliases5.addAll(Arrays.asList("glp", "glistp"));
		registerCommand(new GroupPermissionList("grouplistp", "Base command for listing group permissions.", "hpermissions.group.list.permissions", "", aliases5));
		ArrayList<String> aliases6 = new ArrayList<String>();
		aliases6.addAll(Arrays.asList("gm", "gmake"));
		registerCommand(new GroupMake("groupmake", "Base command for creating groups.", "hpermissions.group.create", "", aliases6));
		ArrayList<String> aliases7 = new ArrayList<String>();
		aliases7.addAll(Arrays.asList("uap", "uaddp"));
		registerCommand(new UserPermissionAdd("useraddp", "Base command for giving user permissions.", "hpermissions.user.add.permission", "", aliases7));
		ArrayList<String> aliases8 = new ArrayList<String>();
		aliases8.addAll(Arrays.asList("urp", "uremp"));
		registerCommand(new UserPermissionRemove("userremp", "Base command for removing user permissions.", "hpermissions.user.remove.permission", "", aliases8));
		ArrayList<String> aliases9 = new ArrayList<String>();
		aliases9.addAll(Arrays.asList("usg", "usetg"));
		registerCommand(new UserSetGroup("usersetg", "Base command for setting a users primary group.", "hpermissions.user.set.group", "", aliases9));
		ArrayList<String> aliases10 = new ArrayList<String>();
		aliases10.addAll(Arrays.asList("uasg", "uaddsg"));
		registerCommand(new UserSubGroupAdd("useraddsg", "Base command for adding sub-groups to users.", "hpermissions.user.add.group", "", aliases10));
		ArrayList<String> aliases11 = new ArrayList<String>();
		aliases11.addAll(Arrays.asList("ursg", "uremsg"));
		registerCommand(new UserSubGroupRemove("userremsg", "Base command for removing sub-groups from users.", "hpermissions.user.remove.group", "", aliases11));
		ArrayList<String> aliases12 = new ArrayList<String>();
		aliases12.addAll(Arrays.asList("gl", "glist"));
		registerCommand(new GroupList("grouplist", "Base command for listing all groups within a world.", "hpermissions.group.list", "", aliases12));
		ArrayList<String> aliases13 = new ArrayList<String>();
		aliases13.addAll(Arrays.asList("gload"));
		registerCommand(new GroupLoad("groupload", "Base command for reloading group files.", "hpermissions.group.reload", "", aliases13));
		ArrayList<String> aliases14 = new ArrayList<String>();
		aliases14.addAll(Arrays.asList("uload"));
		registerCommand(new UserLoad("userload", "Base command for reloading user files.", "hpermissions.user.reload", "", aliases14));
		ArrayList<String> aliases15 = new ArrayList<String>();
		aliases15.addAll(Arrays.asList("gd", "gdel"));
		registerCommand(new GroupDelete("groupdelete", "Base command for deleting groups.", "hpermissions.group.delete", "", aliases15));
	}
	

}
