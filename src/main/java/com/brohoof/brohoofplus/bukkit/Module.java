package com.brohoof.brohoofplus.bukkit;

import javax.annotation.Nullable;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module implements CommandExecutor {

    protected BrohoofPlusPlugin plugin;

    public Module(final BrohoofPlusPlugin plugin, final String... commands) {
        this.plugin = plugin;
        registerCommands(commands);
        final Listener listener = createListener();
        if (listener != null)
            registerEvents(listener, plugin);
    }

    private void registerCommands(final String[] commands) {
        for (final String command : commands)
            plugin.getCommand(command).setExecutor(this);
    }

    private void registerEvents(final Listener listener, final JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Nullable
    protected Listener createListener() {
        return null;
    }

}
