package com.brohoof.brohoofplus.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockEventsBeingFiredTwice extends Module {

    public BlockEventsBeingFiredTwice(BrohoofPlusPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // No commands
        return false;
    }

    @Override
    protected Listener createListener() {
        return new BlockEventsBeingFiredTwiceListener();
    }

    public class BlockEventsBeingFiredTwiceListener implements Listener {
        private long lastTimeEntity = 0;
        private long lastTimeInteract = 0;

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
        public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent pEvent) {
            if (shouldCancel(lastTimeEntity, System.currentTimeMillis()))
                pEvent.setCancelled(true);
            lastTimeEntity = System.currentTimeMillis();
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        public void onPlayerInteractEvent(final PlayerInteractEvent pEvent) {
            if (shouldCancel(lastTimeInteract, System.currentTimeMillis()))
                pEvent.setCancelled(true);
            lastTimeInteract = System.currentTimeMillis();
        }

        private boolean shouldCancel(long oldTime, long newTime) {
            return newTime - oldTime < 3;
        }
    }
}
