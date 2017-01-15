package com.brohoof.brohoofplus.bukkit;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiftItem extends Module {

    public GiftItem(final BrohoofPlusPlugin p) {
        super(p, "giftitem", "giftunique", "copyitem");
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (!(pSender instanceof Player)) {
            pSender.sendMessage(pSender.getClass().getSimpleName() + " does not have an inventory, so they cannot use this command.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("copyitem")) {
            int count;
            final Player target = (Player) pSender;
            if (pArgs.length == 0)
                return false;
            try {
                count = Integer.parseInt(pArgs[0]);
            } catch (final NumberFormatException e) {
                pSender.sendMessage("That is not a number.");
                return true;
            }
            final ItemStack item = target.getInventory().getItemInMainHand();
            for (int i = 0; i < count; i++)
                target.getInventory().addItem(item);
            return true;
        }
        String targetPlayerName = null;
        boolean quiet = false;
        boolean anonymous = false;
        switch (pArgs.length) {
            case 0: {
                pSender.sendMessage("Not enough arguments.");
                return false;
            }
            case 1: {
                targetPlayerName = pArgs[0];
                quiet = false;
                anonymous = false;
                break;
            }
            case 2: {
                targetPlayerName = pArgs[0];
                switch (pArgs[1]) {
                    case "anonymous": {
                        anonymous = true;
                        break;
                    }

                    case "quiet": {
                        quiet = true;
                        break;
                    }
                }
                break;
            }
            case 3: {
                targetPlayerName = pArgs[0];
                anonymous = true;
                quiet = true;
                break;
            }
        }
        final ItemStack item = ((Player) pSender).getInventory().getItemInMainHand();
        if (item == null)
            return true;
        final Collection<Player> receipients = new ArrayList<Player>();
        if (targetPlayerName.equals("*")) {
            receipients.addAll(Bukkit.getOnlinePlayers());
            receipients.remove(pSender);
            pSender.sendMessage("Sending gift to all players...");
            plugin.getLogger().info(pSender.getName() + " is sending a gift to all players (" + item.getType().toString() + ")");
        } else
            for (final Player p : Bukkit.getOnlinePlayers())
                if (p.getName().toLowerCase().equalsIgnoreCase(targetPlayerName)) {
                    receipients.add(p);
                    pSender.sendMessage("Sending gift to " + p.getName() + "...");
                    plugin.getLogger().info(pSender.getName() + " is sending a gift to " + p.getName() + " (" + item.getType().toString() + ")");
                    break;
                }
        if (receipients.size() == 0) {
            pSender.sendMessage("Player " + targetPlayerName + " not found.");
            return true;
        }
        if (quiet) {
            pSender.sendMessage("Sending silently...");
            plugin.getLogger().info("This gift is sent silently (no message)");
        } else if (anonymous) {
            pSender.sendMessage("Sending anonymously...");
            plugin.getLogger().info("This gift is sent anonymously (no sender name)");
        } else
            pSender.sendMessage("Sending with your name...");
        final boolean unique = command.getName().equalsIgnoreCase("giftunique");
        for (final Player receiver : receipients) {
            boolean receives = true;
            if (unique)
                if (receiver.getInventory().containsAtLeast(item, 1))
                    receives = false;
            if (receives) {
                receiver.getInventory().addItem(item);
                if (!quiet)
                    if (anonymous)
                        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig().getString("modules.giftitem.messages.anonymous"))));
                    else
                        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig().getString("modules.giftitem.messages.normal"), pSender.getName())));
            }
        }
        return true;
    }
}
