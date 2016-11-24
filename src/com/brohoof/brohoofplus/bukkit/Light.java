package com.brohoof.brohoofplus.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
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
class Light implements CommandExecutor {
    Light(final BrohoofPlusPlugin plugin) {
        try {
            LightAPI.getInstance().isEnabled();
        } catch (NoClassDefFoundError e) {
            throw new UnsupportedOperationException("Cannot enable LightAPI module, as LightAPI is not installed, or is currently using an unsupported version.", e);
        }
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (command.getName().equalsIgnoreCase("lightcreate")) {
            if (pArgs.length == 0)
                return false;
            if (!(pSender instanceof Player)) {
                if (pArgs.length == 4) {
                	LightAPI.createLight(new Location(Bukkit.getWorld(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2]), Double.parseDouble(pArgs[3])), 15, true);
                    return true;
                }
                pSender.sendMessage("§cAs you are console, you must first specifiy world.");
                pSender.sendMessage("§6lightcreate world x y z");
                return true;
            }
            if (pArgs.length == 3) {
                if (pSender.hasPermission("light.use")) {
                	LightAPI.createLight(new Location(((Player) pSender).getWorld(), Double.parseDouble(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2])), 15, true);
                    return true;
                }
                pSender.sendMessage("§cYou do not have permision");
                return true;
            }
            return false;
        }
        if (command.getName().equalsIgnoreCase("lightdelete")) {
            if (pArgs.length == 0)
                return false;
            if (!(pSender instanceof Player)) {
                if (pArgs.length == 4) {
                	LightAPI.deleteLight(new Location(Bukkit.getWorld(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2]), Double.parseDouble(pArgs[3])), true);
                    return true;
                }
                pSender.sendMessage("§cAs you are console, you must first specifiy world.");
                pSender.sendMessage("§6lightdelete world x y z");
                return true;
            }
            if (pArgs.length == 3) {
                if (pSender.hasPermission("light.use")) {
                	LightAPI.deleteLight(new Location(((Player) pSender).getWorld(), Double.parseDouble(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2])), true);
                    return true;
                }
                pSender.sendMessage("§cYou do not have permision");
                return true;
            }
            return false;
        }
        if (command.getName().equalsIgnoreCase("lightcreatelevel")) {
            if (pArgs.length == 0)
                return false;
            if (!(pSender instanceof Player)) {
                if (pArgs.length == 5) {
                	LightAPI.createLight(new Location(Bukkit.getWorld(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2]), Double.parseDouble(pArgs[3])), Integer.parseInt(pArgs[4]), true);
                    return true;
                }
                pSender.sendMessage("§cAs you are console, you must first specifiy world.");
                pSender.sendMessage("§6lightcreatelevel world x y z level");
                return true;
            }
            if (pArgs.length == 4) {
                if (pSender.hasPermission("light.use")) {
                	LightAPI.createLight(new Location(((Player) pSender).getWorld(), Double.parseDouble(pArgs[0]), Double.parseDouble(pArgs[1]), Double.parseDouble(pArgs[2])), Integer.parseInt(pArgs[3]), true);
                    return true;
                }
                pSender.sendMessage("§cYou do not have permision");
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
                            pSender.sendMessage("§cYou must have a WorldEdit selection.");
                            return true;
                        }
                        if (pArgs[0].equalsIgnoreCase("create")) {
                            if (pArgs.length == 2) {
                                final int level = Integer.parseInt(pArgs[1]);
                                for (final BlockVector bv : re) {
                                    final Location bLocation = new Location(player.getWorld(), bv.getX(), bv.getY(), bv.getZ());
                                    if (bLocation.getBlock().isEmpty())
                                    	LightAPI.createLight(bLocation, level, true);
                                }
                                return true;
                            }
                            return false;
                        }
                        if (pArgs[0].equalsIgnoreCase("delete")) {
                            for (final BlockVector bv : re)
                            	LightAPI.deleteLight(new Location(player.getWorld(), bv.getX(), bv.getY(), bv.getZ()), true);
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                pSender.sendMessage("§cYou do not have permision");
                return true;
            }
            pSender.sendMessage("§cSorry, you must be a player.");
            return true;
        }
        return false;
    }
}
