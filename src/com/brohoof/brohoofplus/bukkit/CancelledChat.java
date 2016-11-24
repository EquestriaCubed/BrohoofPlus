package com.brohoof.brohoofplus.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.Herochat;
import com.dthielke.api.ChatResult;
import com.dthielke.api.event.ChannelChatEvent;

class CancelledChat implements Listener {
    private final BrohoofPlusPlugin p;

    CancelledChat(final BrohoofPlusPlugin plugin) {
        p = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onAsyncPlayerChatEvent(final ChannelChatEvent event) {
        final Player player = event.getChatter().getPlayer();
        final String world = player.getWorld().getName();
        if (world.equals("quiz") && event.getResult().equals(ChatResult.NO_PERMISSION)) {
            p.getLogger().info("[cancelled_chat@quiz] <<" + player.getName() + ">> " + event.getMessage());
            try {
                final String playerName = player.getName();
                final String message = event.getMessage();
                if (event.isAsynchronous())
                    p.getServer().getScheduler().scheduleSyncDelayedTask(p, () -> Herochat.getChannelManager().getChannel("sys").announce("<<" + playerName + ">> " + message), 0);
                else
                    Herochat.getChannelManager().getChannel("sys").announce("<<" + playerName + ">> " + message);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
