package com.brohoof.brohoofplus.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemNope extends Module {
	private final List<String> projectile_names;
	private final List<String> tracer;

	public ItemNope(final BrohoofPlusPlugin p) {
		super(p);
		projectile_names = new ArrayList<>();
		tracer = new ArrayList<>();
		for (final String name : p.getConfig().getStringList("modules.itemnope.projectile.names"))
			projectile_names.add(name.toLowerCase());
	}

	@Override
	public Listener createListener() {
		return new ItemNopeListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	public class ItemNopeListener implements Listener {
		@SuppressWarnings("deprecation")
		@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
		public void projectileLaunchEvent(final ProjectileLaunchEvent event) {
			if (event.getEntityType() == null)
				return;
			final String itemName = event.getEntityType().toString().toLowerCase();
			// If entity is in the restriction list
			if (projectile_names.contains(itemName)) {
				LivingEntity shooter = null;
				if (event.getEntity().getShooter() instanceof LivingEntity)
					shooter = (LivingEntity) event.getEntity().getShooter();
				// Worldspawn (Dispenser or something that is not living)
				if (shooter == null) {
					if (!plugin.getConfig().getBoolean("modules.itemnope.projectile.worldspawn_bypasses_restriction"))
						event.setCancelled(true);
				}
				// NPC
				else if (!(shooter instanceof Player)) {
					if (!plugin.getConfig().getBoolean("modules.itemnope.projectile.npc_bypasses_restriction"))
						event.setCancelled(true);
				}
				// Player
				else if (shooter instanceof Player) {
					final Player ply = (Player) shooter;
					if (!ply.hasPermission("itemnope.itemrestrict.bypass"))
						event.setCancelled(true);
				}
			}
			if (!tracer.contains(itemName)) {
				tracer.add(itemName);
				LivingEntity shooter = null;
				if (event.getEntity().getShooter() instanceof LivingEntity)
					shooter = (LivingEntity) event.getEntity().getShooter();
				String responsible = "UNKNOWN";
				if (shooter == null)
					responsible = "WORLDSPAWN";
				else if (!(shooter instanceof Player))
					responsible = "(" + shooter.getClass().getName() + ") " + shooter.getType().getName();
				else if (shooter instanceof Player)
					responsible = "(Player) " + ((Player) shooter).getName();
				else
					responsible = "(" + shooter.getClass().getName() + ")";
				String extra = "";
				if (event.isCancelled())
					extra = "(CANCELLED) ";
				plugin.getLogger().info(extra + itemName + " has been thrown by " + responsible);
			}
		}
	}
}
