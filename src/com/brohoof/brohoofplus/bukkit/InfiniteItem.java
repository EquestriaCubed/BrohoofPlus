package com.brohoof.brohoofplus.bukkit;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.UUID;

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
                if (item == null) {
                    player.sendMessage("Select an item to make it an infinite stack.");
                    return false;
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
                    }
                }

            } else if (strings.length == 1 && "clear".equalsIgnoreCase(strings[0])) {
                // remove all items
                infiniteItems.removeAll(player.getUniqueId());

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
            Collection<ItemStack> itemStacks = infiniteItems.get(event.getPlayer().getUniqueId());
            for (ItemStack item : itemStacks) {
                if (item.isSimilar(event.getItemInHand())) {
                    ItemStack stack = item.clone();
                    stack.setAmount(1);
                    event.getPlayer().getInventory().addItem(stack);
                    return;
                }
            }
        }
    }
}
