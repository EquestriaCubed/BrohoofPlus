package com.brohoof.brohoofplus.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FirstJoined extends Module {

	public FirstJoined(final BrohoofPlusPlugin plugin) {
		super(plugin);
		listener = new FirstJoinedListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	public class FirstJoinedListener extends ModuleListener {
		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
		public void playerWorldChange(PlayerTeleportEvent e) {
			if (e.getFrom().getWorld().getName().equals(p.getConfig().getString("modules.firstjoined.from")) && e.getTo().getWorld().getName().equals(p.getConfig().getString("modules.firstjoined.to"))) {
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + e.getPlayer().getName() + " has joined for the first time!");
			}
		}
	}

}
