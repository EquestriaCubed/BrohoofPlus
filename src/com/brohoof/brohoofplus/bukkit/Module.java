package com.brohoof.brohoofplus.bukkit;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

public abstract class Module implements CommandExecutor {
	protected BrohoofPlusPlugin p;
	private String[] commands;
	protected ModuleListener listener;

	public Module(BrohoofPlusPlugin p, String... commands) {
		this.p = p;
		this.commands = commands;
		registerCommands();
	}

	private void registerCommands() {
		for (final String command : commands) 
			p.getCommand(command).setExecutor(this);
	}

	public abstract class ModuleListener implements Listener {

		public ModuleListener() {
			p.getServer().getPluginManager().registerEvents(this, p);

		}
	}
}
