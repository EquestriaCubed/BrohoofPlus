package com.brohoof.brohoofplus.bukkit;

import java.io.File;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BrohoofPlusPlugin extends JavaPlugin {
	public static final String BHP = ChatColor.RED + "[" + ChatColor.GOLD + "B" + ChatColor.YELLOW + "r" + ChatColor.GREEN + "o" + ChatColor.DARK_AQUA + "h" + ChatColor.BLUE + "o" + ChatColor.DARK_PURPLE + "o" + ChatColor.RED + "f" + ChatColor.GOLD + "P" + ChatColor.YELLOW + "l" + ChatColor.GREEN + "u" + ChatColor.DARK_AQUA + "s" + ChatColor.BLUE + "] " + ChatColor.WHITE;

	private final HashSet<Class<? extends Module>> modules = new HashSet<Class<? extends Module>>(0);

	@Override
	public void onEnable() {
		modules.add(BlockPotionsAndArrows.class);
		modules.add(HerochatFancyname.class);
		modules.add(CancelledChat.class);
		modules.add(FirstJoined.class);
		modules.add(GiftItem.class);
		modules.add(HerochatFancyname.class);
		modules.add(ItemNope.class);
		modules.add(Light.class);
		modules.add(Lore.class);
		modules.add(Mount.class);
		modules.add(RaceMode.class);
		modules.add(XPBlocker.class);
		modules.add(Flight.class);
		modules.add(Enchant.class);
		try {
			for (Class<? extends Module> module : modules) {
				if (getConfig().getBoolean("modules." + module.getSimpleName().toLowerCase() + ".enabled"))
					module.getConstructor(BrohoofPlusPlugin.class).newInstance(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a {@link FileConfiguration} for this plugin, read through the argument.
	 * <p>
	 *
	 * @return Plugin configuration
	 * @param name
	 *            The file name to read.
	 */
	FileConfiguration getConfig(final String name) {
		return YamlConfiguration.loadConfiguration(new File(getDataFolder(), name));
	}

	@Override
	public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
		if (pArgs.length == 0)
			return false;
		// Handle all /BrohoofPlus commands
		if (command.getName().equalsIgnoreCase("BrohoofPlus")) {
			switch (pArgs[0].toLowerCase()) {
				case "reload": {
					Bukkit.getPluginManager().disablePlugin(this);
					Bukkit.getPluginManager().enablePlugin(this);
					pSender.sendMessage(BHP + "Reloaded settings!");
					return true;
				}
			}
		}
		if (pArgs.length >= 1)
			return true;
		return false;
	}
}
