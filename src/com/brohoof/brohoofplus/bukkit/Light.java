package com.brohoof.brohoofplus.bukkit;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;

import ru.beykerykt.lightapi.LightAPI;

/**
 * Great plugin, but the asshole keeps changing the API.
 */
public class Light extends Module {
	private ArrayList<CommandSender> debugers;

	public Light(final BrohoofPlusPlugin plugin) {
		super(plugin, "lightcreate", "lightdelete", "lightcreatelevel", "lightregion", "lightdebug");
		try {
			JavaPlugin.getPlugin(LightAPI.class);
		} catch (Throwable e) {
			throw new UnsupportedOperationException("Cannot enable LightAPI module, as LightAPI is not installed, or is currently using an unsupported version.", e);
		}
		debugers = new ArrayList<CommandSender>(0);
	}

	@Override
	public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
		if (command.getName().equalsIgnoreCase("lightdebug")) {
			if (debugers.contains(pSender)) {
				debugers.remove(pSender);
				pSender.sendMessage(ChatColor.GREEN + "Removed from debug.");
				return true;
			}
			debugers.add(pSender);
			pSender.sendMessage(ChatColor.GREEN + "Added to debug.");
			return true;
		}
		if (command.getName().equalsIgnoreCase("lightcreate")) {
			if (pArgs.length == 0)
				return false;
			if (!(pSender instanceof Player)) {
				if (pArgs.length == 4) {
					LightAPI.createLight(new Location(Bukkit.getWorld(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2]), Double.parseDouble(pArgs[3])), 15, false);
					if (debugers.contains(pSender))
						pSender.sendMessage(ChatColor.GREEN + "Task complete.");
					return true;
				}
				pSender.sendMessage(ChatColor.RED + "As you are console, you must first specifiy world.");
				pSender.sendMessage(ChatColor.GOLD + "lightcreate world x y z");
				return true;
			}
			if (pArgs.length == 3) {
				if (pSender.hasPermission("light.use")) {
					LightAPI.createLight(new Location(((Player) pSender).getWorld(), Double.parseDouble(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2])), 15, false);
					if (debugers.contains(pSender))
						pSender.sendMessage(ChatColor.GREEN + "Task complete.");
					return true;
				}
				pSender.sendMessage(ChatColor.RED + "You do not have permision");
				return true;
			}
			return false;
		}
		if (command.getName().equalsIgnoreCase("lightdelete")) {
			if (pArgs.length == 0)
				return false;
			if (!(pSender instanceof Player)) {
				if (pArgs.length == 4) {
					LightAPI.deleteLight(new Location(Bukkit.getWorld(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2]), Double.parseDouble(pArgs[3])), false);
					if (debugers.contains(pSender))
						pSender.sendMessage(ChatColor.GREEN + "Task complete.");
					return true;
				}
				pSender.sendMessage(ChatColor.RED + "As you are console, you must first specifiy world.");
				pSender.sendMessage(ChatColor.GOLD + "lightdelete world x y z");
				return true;
			}
			if (pArgs.length == 3) {
				if (pSender.hasPermission("light.use")) {
					LightAPI.deleteLight(new Location(((Player) pSender).getWorld(), Double.parseDouble(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2])), false);
					if (debugers.contains(pSender))
						pSender.sendMessage(ChatColor.GREEN + "Task complete.");
					return true;
				}
				pSender.sendMessage(ChatColor.RED + "You do not have permision");
				return true;
			}
			return false;
		}
		if (command.getName().equalsIgnoreCase("lightcreatelevel")) {
			if (pArgs.length == 0)
				return false;
			if (!(pSender instanceof Player)) {
				if (pArgs.length == 5) {
					LightAPI.createLight(new Location(Bukkit.getWorld(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2]), Double.parseDouble(pArgs[3])), Integer.parseInt(pArgs[4]), false);
					if (debugers.contains(pSender))
						pSender.sendMessage(ChatColor.GREEN + "Task complete.");
					return true;
				}
				pSender.sendMessage(ChatColor.RED + "As you are console, you must first specifiy world.");
				pSender.sendMessage(ChatColor.GOLD + "lightcreatelevel world x y z level");
				return true;
			}
			if (pArgs.length == 4) {
				if (pSender.hasPermission("light.use")) {
					LightAPI.createLight(new Location(((Player) pSender).getWorld(), Double.parseDouble(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2])), Integer.parseInt(pArgs[3]), false);
					if (debugers.contains(pSender))
						pSender.sendMessage(ChatColor.GREEN + "Task complete.");
					return true;
				}
				pSender.sendMessage(ChatColor.RED + "You do not have permision");
				return true;
			}
			return false;
		}
		if (command.getName().equalsIgnoreCase("lightregion")) {
			if (pArgs.length == 0)
				return false;
			if (pSender instanceof Player) {
				final Player player = (Player) pSender;
				if (pSender.hasPermission("light.use")) {
					if (pArgs.length == 1 || pArgs.length == 2) {
						Region re = null;
						try {
							final WorldEditPlugin we = JavaPlugin.getPlugin(WorldEditPlugin.class);
							re = we.getWorldEdit().getSessionManager().getIfPresent(new BukkitPlayer(we, we.getServerInterface(), player)).getRegionSelector((World) BukkitUtil.getLocalWorld(player.getWorld())).getRegion();
						} catch (final IncompleteRegionException e) {
							pSender.sendMessage(ChatColor.RED + "You must have a WorldEdit selection.");
							return true;
						}
						if (pArgs[0].equalsIgnoreCase("create")) {
							if (pArgs.length == 2) {
								final int level = Integer.parseInt(pArgs[1]);
								for (final BlockVector bv : re) {
									final Location bLocation = new Location(player.getWorld(), bv.getX(), bv.getY(), bv.getZ());
									if (bLocation.getBlock().isEmpty())
										LightAPI.createLight(bLocation, level, false);
								}
								if (debugers.contains(pSender))
									pSender.sendMessage(ChatColor.GREEN + "Task complete.");
								return true;
							}
							return false;
						}
						if (pArgs[0].equalsIgnoreCase("delete")) {
							for (final BlockVector bv : re)
								LightAPI.deleteLight(new Location(player.getWorld(), bv.getX(), bv.getY(), bv.getZ()), false);
							if (debugers.contains(pSender))
								pSender.sendMessage(ChatColor.GREEN + "Task complete.");
							return true;
						}
						return false;
					}
					return false;
				}
				pSender.sendMessage(ChatColor.RED + "You do not have permision");
				return true;
			}
			pSender.sendMessage(ChatColor.RED + "Sorry, you must be a player.");
			return true;
		}
		return false;
	}

}
