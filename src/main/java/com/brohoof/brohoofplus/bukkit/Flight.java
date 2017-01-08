package com.brohoof.brohoofplus.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Flight extends Module {

	public Flight(BrohoofPlusPlugin p) {
		super(p, "fly");
	}

	@Override
	protected Listener createListener() {
		return new FlightListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			final Player p = (Player) sender;
			p.setAllowFlight(!p.getAllowFlight());
			p.sendMessage(p.getAllowFlight() ? BrohoofPlusPlugin.BHP + "You are now flying again." : BrohoofPlusPlugin.BHP + "You are no longer flying.");
			return true;
		}
		sender.sendMessage("Only player command, bad console admin.");
		return true;
	}
	
	public class FlightListener implements Listener {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        public void playerJoin(PlayerJoinEvent p) {
            p.getPlayer().setAllowFlight(true);
        }
    }
}
