package com.brohoof.brohoofplus.bukkit;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class BrohoofPlus implements CommandExecutor {
    private final String BHP = "§c[§6B§er§ao§3h§9o§5o§cf§6P§el§au§3s§9] §f";
    private final BrohoofPlusPlugin plugin;
    private HerochatFancyname hero;

    BrohoofPlus(final BrohoofPlusPlugin p) {
        plugin = p;
        if (plugin.getConfig().getBoolean("modules.fancyname.enabled")) {
            hero = new HerochatFancyname();
            p.getServer().getPluginManager().registerEvents(hero, p);
        }
        else
            hero = null;
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (pArgs.length == 0)
            return false;
        switch (pArgs[0].toLowerCase()) {
            case "fancyname": {
                if (!plugin.getConfig().getBoolean("modules.fancyname.enabled")) {
                    pSender.sendMessage(BHP + "This module is not enabled.");
                    return false;
                }
                if (pSender instanceof Player) {
                    final Player p = (Player) pSender;
                    if (pArgs.length == 1) {
                        hero.remove(p);
                        pSender.sendMessage(BHP + "Your name is now normal.");
                        return true;
                    }
                    for (String str : (String[]) ArrayUtils.subarray(pArgs, 1, pArgs.length))
                        if (str.toCharArray()[0] != '&' || str.length() != 2) {
                            pSender.sendMessage(BHP + "It doesn't look like " + str + " is a colour code, so we won't use it.");
                            str = "";
                            return true;
                        }
                    String nameToDisplay = "";
                    final char[] letters = p.getName().toCharArray();
                    int j = 0;
                    while (j < letters.length)
                        for (int i = 1; i <= pArgs.length - 1; i++) {
                            // Sometimes an ArrayIndexOutOfBoundsException is thrown if we don't do this.
                            if (j >= letters.length)
                                break;
                            final char letter = letters[j];
                            final String colourToAppend = ChatColor.translateAlternateColorCodes('&', pArgs[i]);
                            nameToDisplay += colourToAppend + letter;
                            j++;
                        }
                    hero.add(p, nameToDisplay);
                    pSender.sendMessage(BHP + "Your name is now displayed as " + nameToDisplay);
                    return true;
                }
                pSender.sendMessage(BHP + "Sorry, this command can only be used by players.");
                return true;
            }
            case "reload": {
                Bukkit.getPluginManager().disablePlugin(plugin);
                Bukkit.getPluginManager().enablePlugin(plugin);
                pSender.sendMessage(BHP + "Reloaded settings!");
                return true;
            }
        }
        return false;
    }
}
