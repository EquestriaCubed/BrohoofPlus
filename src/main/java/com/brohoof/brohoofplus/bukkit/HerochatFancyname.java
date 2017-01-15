package com.brohoof.brohoofplus.bukkit;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.Herochat;
import com.dthielke.api.event.ChannelChatEvent;

public class HerochatFancyname extends Module {
    private final HashMap<Player, String> enabledUsers = new HashMap<Player, String>(0);

    public HerochatFancyname(final BrohoofPlusPlugin p) {
        super(p, "fancyname");
    }

    @Override
    protected Listener createListener() {
        return new HerochatFancynameListener();
    }

    private void add(final Player p, final String nick) {
        enabledUsers.put(p, nick);
    }

    private void remove(final Player p) {
        enabledUsers.remove(p);
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (pSender instanceof Player) {
            final Player p = (Player) pSender;
            if (pArgs.length == 0) {
                remove(p);
                pSender.sendMessage(BrohoofPlusPlugin.BHP + "Your name is now normal.");
                return true;
            }
            if (pArgs[0].equalsIgnoreCase("rainbow")) {
                final String nameToDisplay = BrohoofPlusPlugin.rainbowify(p.getName());
                add(p, nameToDisplay);
                pSender.sendMessage(BrohoofPlusPlugin.BHP + "Your name is now displayed as " + nameToDisplay);
                return true;
            }
            final ArrayList<ChatColor> colors = new ArrayList<ChatColor>(0);
            for (final String str : pArgs) {
                if (str.toCharArray()[0] != '&' || str.length() != 2) {
                    pSender.sendMessage(BrohoofPlusPlugin.BHP + "It doesn't look like " + str + " is a colour code, so we won't use it.");
                    continue;
                }
                colors.add(ChatColor.getByChar(str.charAt(1)));
            }
            final String nameToDisplay = BrohoofPlusPlugin.colorize(p.getName(), colors.toArray(new ChatColor[0]));
            add(p, nameToDisplay);
            pSender.sendMessage(BrohoofPlusPlugin.BHP + "Your name is now displayed as " + nameToDisplay);
            return true;
        }
        pSender.sendMessage(BrohoofPlusPlugin.BHP + "Sorry, this command can only be used by players.");
        return true;

    }

    public class HerochatFancynameListener implements Listener {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        public void onHeroChatMessage(final ChannelChatEvent event) {
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
}
