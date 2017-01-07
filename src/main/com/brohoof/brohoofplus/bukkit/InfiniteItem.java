package com.brohoof.brohoofplus.bukkit;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author killjoy1221
 */
public class InfiniteItem extends Module {

    private Multimap<UUID, ItemStack> infiniteItems = ArrayListMultimap.create();

    public InfiniteItem(BrohoofPlusPlugin p) {
        super(p, "inf");
    }

    @Override
    protected Listener createListener() {
        return new ItemListener();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 0) {
                // add or remove the current item
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item == null || item.getType() == Material.AIR) {
                    player.sendMessage("Select an item to make it an infinite stack.");
                } else if (!item.getType().isBlock()) {
                    player.sendMessage("Only blocks can be infinite. Select a block.");
                } else if (item.getMaxStackSize() == 1) {
                    player.sendMessage("This item is not stackable. You cannot have an infinite stack of it.");
                } else {
                    // try to find the itemstack first
                    ItemStack toRemove = null;
                    for (ItemStack stack : infiniteItems.get(player.getUniqueId())) {
                        if (item.isSimilar(stack)) {
                            toRemove = stack;
                        }
                    }
                    if (toRemove == null) {
                        // add if not there
                        infiniteItems.put(player.getUniqueId(), item.clone());
                        player.sendMessage("You now have an infinite stack");
                    } else {
                        // remove if is
                        infiniteItems.remove(player.getUniqueId(), toRemove);
                        player.sendMessage("Infinite stack removed");
                    }
                }

            } else if (strings.length == 1 && "clear".equalsIgnoreCase(strings[0])) {
                // remove all items
                infiniteItems.removeAll(player.getUniqueId());
                player.sendMessage("All infinite stacks removed");

            } else {
                return false;
            }
        } else {
            commandSender.sendMessage("Console cannot use this command.");
            return false;
        }
        return true;
    }

    private class ItemListener implements Listener {

        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void replenishItems(BlockPlaceEvent event) {
            final Player player = event.getPlayer();
            for (ItemStack item : infiniteItems.get(player.getUniqueId())) {
                if (item.isSimilar(event.getItemInHand())) {
                    final ItemStack stack = item.clone();
                    stack.setAmount(1);
                    // run later so it doesn't mess up the event
                    Bukkit.getScheduler().runTask(plugin, () -> player.getInventory().addItem(stack));
                    return;
                }
            }
        }
    }
}
