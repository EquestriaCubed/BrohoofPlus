package com.brohoof.brohoofplus.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class Mount extends Module {

	public Mount(final BrohoofPlusPlugin brohoofPlusPlugin) {
		super(brohoofPlusPlugin, "mount", "unmount", "mounttoggle");
	}

    @Override
    public Listener createListener() {
        return new MountEvent();
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (command.getName().equalsIgnoreCase("mount")) {
            if (pSender instanceof Player) {
                final Player p = (Player) pSender;
                p.getInventory().addItem(new ItemStack(Material.SADDLE, 1));
                p.sendMessage(BrohoofPlusPlugin.BHP + "Right click an entity with this saddle to mount it.");
                return true;
            }
            pSender.sendMessage(BrohoofPlusPlugin.BHP + "Players only.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("mounttoggle")) {
            try {
                File mountFile = new File(plugin.getDataFolder(), "mountpref.yml");
                if (!mountFile.exists()) {
                    mountFile.createNewFile();
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(mountFile);
                // mounttoggle
                if (pSender instanceof Player) {
                    final Player sender = (Player) pSender;
                    if (pArgs.length == 0) {
                        sender.sendMessage(BrohoofPlusPlugin.BHP + "/mounttoggle allow | deny");
                        return true;
                    }
                    if (pArgs[0].equalsIgnoreCase("allow")) {
                        config.set("mountpref." + sender.getUniqueId().toString() + ".status", Status.ALLOWED.getID());
                        config.save(mountFile);
                        sender.sendMessage(BrohoofPlusPlugin.BHP + "You can now be mounted.");
                        return true;
                    }
                    if (pArgs[0].equalsIgnoreCase("deny")) {
                        config.set("mountpref." + sender.getUniqueId().toString() + ".status", Status.REJECTED.getID());
                        config.save(mountFile);
                        sender.eject();
                        sender.sendMessage(BrohoofPlusPlugin.BHP + "You can no longer be mounted.");
                        return true;
                    }
                }
                pSender.sendMessage("Players only.");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                pSender.sendMessage(BrohoofPlusPlugin.BHP + "�cAn internal error occured. Please contact an admin.");
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("unmount")) {
            // unmount
            if (pSender instanceof Player) {
                final Player sender = (Player) pSender;
                sender.eject();
                final Entity vehicle = sender.getVehicle();
                if (vehicle != null)
                    vehicle.eject();
                return true;
            }
            pSender.sendMessage("Players only.");
            return true;
        }
        return false;
    }

    public class MountEvent implements Listener {
        @SuppressWarnings("deprecation")
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        public void onMountEvent(final PlayerInteractEntityEvent pEvent) {
            final Player clicker = pEvent.getPlayer();
            if (clicker.getInventory().getItemInMainHand() == null)
                return;
            final Entity clicked = pEvent.getRightClicked();
            switch (clicked.getType()) {
                case HORSE:
                case PIG:
                case LLAMA:
                case MULE:
                case SKELETON_HORSE:
                case UNKNOWN:
                case ZOMBIE_HORSE:
                    return;
                default:
                    break;
            }
            if (clicker.getInventory().getItemInMainHand().getType().equals(Material.SADDLE)) {
                final Status targetStatus = Status.fromInt(plugin.getConfig("mountpref.yml").getInt("mountpref." + clicked.getUniqueId().toString() + ".status"));
                if (targetStatus == null || targetStatus == Status.ALLOWED) {
                    if (clicked.getPassenger() == null) {
                        if (clicked instanceof Player)
                            plugin.getLogger().info(clicker.getName() + " mounted " + ((Player) clicked).getName());
                        else
                            plugin.getLogger().info(clicker.getName() + " mounted a " + clicked.getType().getName());
                        // clicked.setPassenger(clicker);
                        boolean success = false;
                        String errorPrefix = BrohoofPlusPlugin.BHP + ChatColor.RED;
                        try {
                            success = clicked.setPassenger(clicker);
                        } catch (Exception e) {
                            clicker.sendMessage(errorPrefix + "An error occured while mounting " + clicked.getName() + "!");
                            if (e instanceof IllegalStateException)
                                clicker.sendMessage(errorPrefix + "This error occured due to circular entity riding. Whatever you were trying to mount is actually riding you! You cannot see this entity due to a bug in Minecraft, and cannot be fixed on the Server.");
                            else
                                e.printStackTrace();
                        }
                        if (!success)
                            clicker.sendMessage(BrohoofPlusPlugin.BHP + ChatColor.RED + "An error occured while mounting " + clicked.getName() + "!");
                        return;
                    }
                    clicker.sendMessage(BrohoofPlusPlugin.BHP + "That Entity is already being mounted!");
                    return;
                }
                clicker.sendMessage(BrohoofPlusPlugin.BHP + "That Entity cannot be mounted.");
                return;
            }
        }

        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        public void onPlayerLeave(PlayerQuitEvent event) {
            final Player p = event.getPlayer();
            p.eject();
            final Entity vehicle = p.getVehicle();
            if (vehicle != null)
                vehicle.eject();
        }
    }

    public enum Status {
        ALLOWED(0), REJECTED(2), TOGGLE(1);
        public static Status fromInt(final int i) {
            switch (i) {
                case 0:
                    return ALLOWED;
                case 1:
                    return TOGGLE;
                case 2:
                    return REJECTED;
            }
            throw new IllegalArgumentException("Integer is not 1, 2, or 3");
        }

        private final int id;

        private Status(final int id) {
            this.id = id;
        }

        public int getID() {
            return id;
        }

        @Override
        public String toString() {
            switch (this) {
                case ALLOWED:
                    return "ALLOWED";
                case TOGGLE:
                    return "TOGGLE";
                case REJECTED:
                    return "REJECTED";
                default:
                    return "";
            }
        }
    }
}
