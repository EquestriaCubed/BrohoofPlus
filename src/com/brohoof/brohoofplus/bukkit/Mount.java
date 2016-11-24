package com.brohoof.brohoofplus.bukkit;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

class Mount implements CommandExecutor {
    private final String BHP = "§c[§6B§er§ao§3h§9o§5o§cf§6P§el§au§3s§9] §f";
    private final BrohoofPlusPlugin plugin;

    public Mount(final BrohoofPlusPlugin brohoofPlusPlugin) {
        plugin = brohoofPlusPlugin;
        plugin.getServer().getPluginManager().registerEvents(new MountEvent(), brohoofPlusPlugin);
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (command.getName().equalsIgnoreCase("mount")) {
            if (pSender instanceof Player) {
                final Player p = (Player) pSender;
                p.getInventory().addItem(new ItemStack(Material.SADDLE, 1));
                p.sendMessage(BHP + "Right click an entity with this saddle to mount it.");
                return true;
            }
            pSender.sendMessage(BHP + "Players only.");
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
                        sender.sendMessage(BHP + "/mounttoggle allow | deny");
                        return true;
                    }
                    if (pArgs[0].equalsIgnoreCase("allow")) {
                        config.set("mountpref." + sender.getUniqueId().toString() + ".status", Status.ALLOWED.getID());
                        config.save(mountFile);
                        sender.sendMessage(BHP + "You can now be mounted.");
                        return true;
                    }
                    if (pArgs[0].equalsIgnoreCase("deny")) {
                        config.set("mountpref." + sender.getUniqueId().toString() + ".status", Status.REJECTED.getID());
                        config.save(mountFile);
                        sender.eject();
                        sender.sendMessage(BHP + "You can no longer be mounted.");
                        return true;
                    }
                }
                pSender.sendMessage("Players only.");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                pSender.sendMessage(BHP + "§cAn internal error occured. Please contact an admin.");
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
    private enum Status {
        ALLOWED(0), REJECTED(2), TOGGLE(1);
        static Status fromInt(final int i) {
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

        private int getID() {
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
    private class MountEvent implements Listener {
        @SuppressWarnings("deprecation")
		@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        private void onMountEvent(final PlayerInteractEntityEvent pEvent) {
            final Player clicker = pEvent.getPlayer();
            if (clicker.getInventory().getItemInMainHand() == null)
                return;
            final Entity clicked = pEvent.getRightClicked();
            switch (clicked.getType()) {
                case HORSE:
                case PIG:
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
                        //clicked.setPassenger(clicker);
                        boolean success = clicked.setPassenger(clicker);
                        if(!success)
                        	clicker.sendMessage("An error occured while mounting " + clicked.getName() + "!");
                        return;
                    }
                    clicker.sendMessage(BHP + "That Entity is already being mounted!");
                    return;
                }
                clicker.sendMessage(BHP + "That Entity cannot be mounted.");
                return;
            }
        }
    }
}
