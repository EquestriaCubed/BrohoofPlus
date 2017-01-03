package com.brohoof.brohoofplus.bukkit;

import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module implements CommandExecutor {

	protected BrohoofPlusPlugin plugin;

	public Module(BrohoofPlusPlugin plugin, String... commands) {
		this.plugin = plugin;
		registerCommands(commands);
		Listener listener = createListener();
		if (listener != null)
			registerEvents(listener, plugin);
	}

	private void registerCommands(String[] commands) {
		for (final String command : commands) 
			plugin.getCommand(command).setExecutor(this);
	}

	private void registerEvents(Listener listener, JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}

	@Nullable
	protected Listener createListener() {
		return null;
	}

}
