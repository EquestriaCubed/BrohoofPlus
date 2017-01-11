package com.brohoof.brohoofplus.bukkit;

import com.google.common.collect.Iterators;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Iterator;

import static org.bukkit.ChatColor.*;

public class BrohoofPlusPlugin extends JavaPlugin {

	public static final String BHP = rainbowify("[BrohoofPlus]");

	@Override
	public void onEnable() {
		try {
			for (Modules module : Modules.values()) {
				if (getConfig().getBoolean("modules." + module + ".enabled"))
					module.getConstructor().apply(this);
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

	public static String rainbowify(String string) {
        return colorize(string, RED, GOLD, YELLOW, GREEN, DARK_AQUA, BLUE, DARK_PURPLE);
    }
	
	public static String colorize(String string, ChatColor... color) {
        Iterator<ChatColor> cycle = Iterators.cycle(color);
        StringBuilder sb = new StringBuilder();
        for (char c : string.toCharArray()) {
            sb.append(cycle.next()).append(c);
        }
        return sb.append(WHITE).toString();
	}
}
