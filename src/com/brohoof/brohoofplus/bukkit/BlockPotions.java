package com.brohoof.brohoofplus.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockPotions implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent p) {
        p.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionUse(PlayerInteractEvent p) {
        Material mat = p.getMaterial();
        // All of the potion types
        if(mat == Material.POTION || mat == Material.LINGERING_POTION || mat == Material.SPLASH_POTION || mat == Material.EXP_BOTTLE) {
            p.setCancelled(true);
            p.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, potions are disallowed.");
        }
    }
}
