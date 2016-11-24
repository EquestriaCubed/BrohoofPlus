package com.brohoof.brohoofplus.bukkit;

import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BrohoofPlusPlugin extends JavaPlugin {
    private final String[] lores = { "lore_add", "lore_name", "lore_reset", "lore_repair", "lore_color" };
    private final String[] gifts = { "giftitem", "giftunique", "copyitem" };
    private final String[] races = { "racemode" };
    private final String[] mounts = { "mount", "unmount", "mounttoggle" };
    private final String[] junks = { "fly", "enchant", "exploit" };
    private final String[] lights = { "lightcreate", "lightdelete", "lightcreatelevel", "lightregion", "lightdebug" };

    @Override
    public void onEnable() {
        getCommand("BrohoofPlus").setExecutor(new BrohoofPlus(this));
        registerCommands(lores, "lore", new Lore(this));
        registerCommands(gifts, "giftitem", new GiftItem(this));
        registerEvent("itemnope", new ItemNope(this));
        registerEvent("xpblocker", new XPBlocker());
        registerCommands(races, "racemode", new RaceMode(this));
        registerCommands(mounts, "mounting", new Mount(this));
        registerCommands(lights, "light", new Light(this));
        registerCommands(junks, "misccommands", new Junk(this));
        registerEvent("cancelledchat", new CancelledChat(this));
        registerEvent("firstjoined", new FirstJoined(this));
        registerEvent("blockpotions", new BlockPotions());
    }

    private void registerCommands(String[] commands, String option, CommandExecutor executor) {
        if (getConfig().getBoolean("modules." + option + ".enabled")) {
            for (final String command : commands) {
                getCommand(command).setExecutor(executor);
            }
        }
    }

    private void registerEvent(String option, Listener listener) {
        if (getConfig().getBoolean("modules." + option + ".enabled"))
            getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Gets a {@link FileConfiguration} for this plugin, read through
     * the argument.
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
        if (pArgs.length >= 1)
            return true;
        return false;
    }
}
