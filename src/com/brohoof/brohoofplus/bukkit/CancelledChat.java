package com.brohoof.brohoofplus.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import com.dthielke.Herochat;
import com.dthielke.api.ChatResult;
import com.dthielke.api.event.ChannelChatEvent;

public class CancelledChat extends Module {

	public CancelledChat(final BrohoofPlusPlugin plugin) {
		super(plugin);
		listener = new CancelledChatListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	public class CancelledChatListener extends ModuleListener {
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
}
