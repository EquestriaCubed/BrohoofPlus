package com.brohoof.brohoofplus.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class XPBlocker extends Module {
	public XPBlocker(BrohoofPlusPlugin p) {
		super(p);
	}

	@Override
	public Listener createListener() {
		return new XPBlockerEvents();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	public class XPBlockerEvents implements Listener {
		@EventHandler(priority = EventPriority.LOWEST)
		public void onBlockExp(final BlockExpEvent event) {
			event.setExpToDrop(0);
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onEntityDeath(final EntityDeathEvent event) {
			event.setDroppedExp(0);
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onExpBottle(final ExpBottleEvent event) {
			event.setExperience(0);
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onPlayerExpChangeEvent(final PlayerExpChangeEvent pEvent) {
			pEvent.setAmount(0);
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onPlayerFish(final PlayerFishEvent event) {
			event.setExpToDrop(0);
		}
	}

}
