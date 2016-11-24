package com.brohoof.brohoofplus.bukkit;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.Herochat;
import com.dthielke.api.event.ChannelChatEvent;

class HerochatFancyname implements Listener {
    private final HashMap<Player, String> enabledUsers = new HashMap<Player, String>(256);

    void add(final Player p, final String nick) {
        enabledUsers.put(p, nick);
    }

    void remove(final Player p) {
        enabledUsers.remove(p);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    void onHeroChatMessage(final ChannelChatEvent event) {
        final Player player = event.getChatter().getPlayer();
        final String nick = enabledUsers.get(player);
        if (enabledUsers.containsKey(player)) {
            if (nick != null && !nick.isEmpty()) {
                if (!event.getFormat().equalsIgnoreCase("{default}")) {
                    final String format = event.getFormat().replace("{sender}", nick);
                    event.setFormat(format);
                    return;
                }
                final String format = Herochat.getInstance().getConfig().getString("format.default").replace("{sender}", nick);
                event.setFormat(format);
                return;
            }
            return;
        }
        return;
    }
}
