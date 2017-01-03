package com.brohoof.brohoofplus.bukkit;

import com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class Lore extends Module {

    public Lore(final BrohoofPlusPlugin p) {
    	super(p, "lore_add", "lore_name", "lore_reset", "lore_repair", "lore_color");
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (!(pSender instanceof Player)) {
            pSender.sendMessage(pSender.getClass().getSimpleName() + " does not have an inventory, so they cannot use this command.");
            return true;
        }
        String argo = "";
        final ItemStack item = ((Player) pSender).getInventory().getItemInMainHand();
        if (item == null || item.getType().equals(Material.AIR)) {
            pSender.sendMessage("You must be holding an item!");
            return true;
        }
        ItemMeta meta = null;
        if (pArgs.length > 0)
            argo = ChatColor.translateAlternateColorCodes('&', Joiner.on(' ').join(pArgs));
        switch (command.getName().toLowerCase()) {
            case "lore_add": {
                meta = ensureMeta(item);
                final List<String> lore = meta.hasLore() ? new ArrayList<String>(meta.getLore()) : new ArrayList<String>();
                lore.add(argo);
                meta.setLore(lore);
                item.setItemMeta(meta);
                return true;
            }
            case "lore_name": {
                meta = ensureMeta(item);
                meta.setDisplayName(argo);
                item.setItemMeta(meta);
                return true;
            }
            case "lore_reset": {
                meta = ensureMeta(item);
                meta.setLore(new ArrayList<String>());
                item.setItemMeta(meta);
                return true;
            }
            case "lore_repair": {
                item.setDurability(new ItemStack(item.getType()).getDurability());
                return true;
            }
            case "lore_color": {
                try {
                    int colorLong = 0;
                    if (pArgs.length == 1) {
                        String colorHexString = pArgs[0];
                        if (!pArgs[0].startsWith("#") && !pArgs[0].startsWith("0x") && !pArgs[0].startsWith("0X"))
                            colorHexString = "#" + colorHexString;
                        colorLong = Integer.decode(colorHexString);
                    } else if (pArgs.length == 3)
                        colorLong = Integer.parseInt(pArgs[0]) << 16 | Integer.parseInt(pArgs[1]) << 8 | Integer.parseInt(pArgs[2]);
                    else
                        throw new IllegalArgumentException();
                    meta = ensureMeta(item);
                    if (meta instanceof LeatherArmorMeta) {
                        final LeatherArmorMeta lmeta = (LeatherArmorMeta) meta;
                        lmeta.setColor(Color.fromRGB(colorLong));
                    }
                    item.setItemMeta(meta);
                    return true;
                } catch (final Exception e) {
                    pSender.sendMessage("Wrong format! (" + e.getClass().getCanonicalName() + ":" + e.getMessage() + ")");
                    return true;
                }
            }
        }
        return false;
    }

    private ItemMeta ensureMeta(final ItemStack item) {
        return item.hasItemMeta() ? item.getItemMeta() : plugin.getServer().getItemFactory().getItemMeta(item.getType());
    }
}
