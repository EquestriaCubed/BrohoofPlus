package com.brohoof.brohoofplus.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;

class XPBlocker implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    void onBlockExp(final BlockExpEvent event) {
        event.setExpToDrop(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onEntityDeath(final EntityDeathEvent event) {
        event.setDroppedExp(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onExpBottle(final ExpBottleEvent event) {
        event.setExperience(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerExpChangeEvent(final PlayerExpChangeEvent pEvent) {
        pEvent.setAmount(0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerFish(final PlayerFishEvent event) {
        event.setExpToDrop(0);
    }
}
