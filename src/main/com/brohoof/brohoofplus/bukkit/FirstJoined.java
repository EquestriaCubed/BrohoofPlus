package com.brohoof.brohoofplus.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FirstJoined extends Module {

	public FirstJoined(final BrohoofPlusPlugin plugin) {
		super(plugin);
	}

	@Override
	protected Listener createListener() {
		return new FirstJoinedListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	public class FirstJoinedListener implements Listener {
		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
		public void playerWorldChange(PlayerTeleportEvent e) {
			if (e.getFrom().getWorld().getName().equals(plugin.getConfig().getString("modules.firstjoined.from")) && e.getTo().getWorld().getName().equals(plugin.getConfig().getString("modules.firstjoined.to"))) {
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + e.getPlayer().getName() + " has joined for the first time!");
			}
		}
	}

}
