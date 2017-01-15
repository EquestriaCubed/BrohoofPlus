package com.brohoof.brohoofplus.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockPotionsAndArrows extends Module {
    public BlockPotionsAndArrows(final BrohoofPlusPlugin p) {
        super(p);
    }

    @Override
    protected Listener createListener() {
        return new BlockPotionsndArrowsListener();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        // There are no commands for this class.
        return false;
    }

    public class BlockPotionsndArrowsListener implements Listener {

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
        public void onPotionSplashEvent(final PotionSplashEvent e) {
            e.setCancelled(true);
            e.getEntity().sendMessage(ChatColor.DARK_RED + "Sorry, this action is disallowed.");
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
        public void onBannedItemUse(final PlayerInteractEvent e) {
            if (e.getPlayer().hasPermission("brohoofplus.bypasspotions"))
                return;
            switch (e.getMaterial()) {
                // To ban, or not to ban?
                // case SPECTRAL_ARROW:
                case TIPPED_ARROW:
                case POTION:
                case LINGERING_POTION:
                case SPLASH_POTION:
                case EXP_BOTTLE: {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, this item is disallowed.");
                    break;
                }
                default:
                    break;
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
        public void onLingeringPotionSplashEvent(final LingeringPotionSplashEvent e) {
            e.setCancelled(true);
            e.getEntity().sendMessage(ChatColor.DARK_RED + "Sorry, this action is disallowed.");
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
        public void onEntityShootBowEvent(final EntityShootBowEvent e) {
            if (e.getProjectile() instanceof TippedArrow) {
                e.setCancelled(true);
                e.getEntity().sendMessage(ChatColor.DARK_RED + "Sorry, this action is disallowed.");
            }
        }
    }

}
