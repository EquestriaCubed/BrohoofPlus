package com.brohoof.brohoofplus.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

class FirstJoined implements Listener {
    private BrohoofPlusPlugin p;

    FirstJoined(final BrohoofPlusPlugin plugin) {
        p = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void playerWorldChange(PlayerTeleportEvent e) {
        if (e.getFrom().getWorld().getName().equals(p.getConfig().getString("modules.firstjoined.from")) && e.getTo().getWorld().getName().equals(p.getConfig().getString("modules.firstjoined.to"))) {
            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + e.getPlayer().getName() + " has joined for the first time!");
        }
    }
}
