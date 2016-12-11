package com.brohoof.brohoofplus.bukkit;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import com.google.common.base.Joiner;

class Junk implements CommandExecutor {
    @SuppressWarnings("unused")
	private final String BHP = "§c[§6B§er§ao§3h§9o§5o§cf§6P§el§au§3s§9] §f";
    @SuppressWarnings("unused")
	private static BrohoofPlusPlugin p;

    @SuppressWarnings("static-access")
    Junk(final BrohoofPlusPlugin p) {
        this.p = p;
        p.getServer().getPluginManager().registerEvents(new FlightListener(), p);
    }
    private class FlightListener implements Listener {
        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        void playerJoin(PlayerJoinEvent p) {
            p.getPlayer().setAllowFlight(true);
        }
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (command.getName().equalsIgnoreCase("fly")) {
            if (pSender instanceof Player) {
                final Player p = (Player) pSender;
                p.setAllowFlight(!p.getAllowFlight());
                p.sendMessage(p.getAllowFlight() ? "You are now flying again." : "You are no longer flying.");
                return true;
            }
            pSender.sendMessage("Only player command, bad console admin.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("enchant")) {
            if (pArgs.length != 3)
                return false;
            Player target = null;
            final Enchantment enchant = getEnchant(Joiner.on(' ').join(ArrayUtils.subarray(pArgs, 1, pArgs.length - 1)));
            ItemStack item;
            int level;
            try {
                level = Integer.parseInt(pArgs[pArgs.length - 1]);
            } catch (final NumberFormatException e) {
                pSender.sendMessage("§cYour level is not a number.");
                return true;
            }
            for (final Player p : Bukkit.getOnlinePlayers())
                if (p.getName().toLowerCase().startsWith(pArgs[0].toLowerCase()))
                    target = p;
            if (target == null) {
                pSender.sendMessage("§cTarget player not found.");
                return true;
            }
            if (enchant == null) {
                pSender.sendMessage("§cNot a valid enchantment.");
                return true;
            }
            item = target.getInventory().getItemInMainHand();
            if (item == null) {
                pSender.sendMessage("§cTarget player not holding an item.");
                return true;
            }
            item.addUnsafeEnchantment(enchant, level);
            pSender.sendMessage("§aEnchant successful.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("exploit")) {
            pSender.sendMessage("§cDo not exploit.");
            return true;
        }
        return false;
    }

    private Enchantment getEnchant(final String enchant) {
        // Try getting the id first.
        try {
            switch (Integer.parseInt(enchant)) {
                case 0:
                    return Enchantment.PROTECTION_ENVIRONMENTAL;
                case 1:
                    return Enchantment.PROTECTION_FIRE;
                case 2:
                    return Enchantment.PROTECTION_FALL;
                case 3:
                    return Enchantment.PROTECTION_EXPLOSIONS;
                case 4:
                    return Enchantment.PROTECTION_PROJECTILE;
                case 5:
                    return Enchantment.OXYGEN;
                case 6:
                    return Enchantment.WATER_WORKER;
                case 7:
                    return Enchantment.THORNS;
                case 8:
                    return Enchantment.DEPTH_STRIDER;
                case 16:
                    return Enchantment.DAMAGE_ALL;
                case 17:
                    return Enchantment.DAMAGE_UNDEAD;
                case 18:
                    return Enchantment.DAMAGE_ARTHROPODS;
                case 19:
                    return Enchantment.KNOCKBACK;
                case 20:
                    return Enchantment.FIRE_ASPECT;
                case 21:
                    return Enchantment.LOOT_BONUS_MOBS;
                case 32:
                    return Enchantment.DIG_SPEED;
                case 33:
                    return Enchantment.SILK_TOUCH;
                case 34:
                    return Enchantment.DURABILITY;
                case 35:
                    return Enchantment.LOOT_BONUS_BLOCKS;
                case 48:
                    return Enchantment.ARROW_DAMAGE;
                case 49:
                    return Enchantment.ARROW_KNOCKBACK;
                case 50:
                    return Enchantment.ARROW_FIRE;
                case 51:
                    return Enchantment.ARROW_INFINITE;
                case 61:
                    return Enchantment.LUCK;
                case 62:
                    return Enchantment.LURE;
                default:
                    return null;
            }
        } catch (final NumberFormatException e) {
            if (enchant.equalsIgnoreCase("ARROW_DAMAGE") || enchant.equalsIgnoreCase("Power"))
                return Enchantment.ARROW_DAMAGE;
            if (enchant.equalsIgnoreCase("ARROW_FIRE") || enchant.equalsIgnoreCase("Flame"))
                return Enchantment.ARROW_FIRE;
            if (enchant.equalsIgnoreCase("ARROW_INFINITE") || enchant.equalsIgnoreCase("Infinity"))
                return Enchantment.ARROW_INFINITE;
            if (enchant.equalsIgnoreCase("ARROW_KNOCKBACK") || enchant.equalsIgnoreCase("Punch"))
                return Enchantment.ARROW_KNOCKBACK;
            if (enchant.equalsIgnoreCase("DAMAGE_ALL") || enchant.equalsIgnoreCase("Sharpness"))
                return Enchantment.DAMAGE_ALL;
            if (enchant.equalsIgnoreCase("DAMAGE_ARTHROPODS") || enchant.equalsIgnoreCase("Bane of Arthropods") || enchant.equalsIgnoreCase("bane_of_arthropods"))
                return Enchantment.DAMAGE_ARTHROPODS;
            if (enchant.equalsIgnoreCase("DAMAGE_UNDEAD") || enchant.equalsIgnoreCase("Smite"))
                return Enchantment.DAMAGE_UNDEAD;
            if (enchant.equalsIgnoreCase("DEPTH_STRIDER") || enchant.equalsIgnoreCase("Depth Strider"))
                return Enchantment.DEPTH_STRIDER;
            if (enchant.equalsIgnoreCase("DIG_SPEED") || enchant.equalsIgnoreCase("Efficiency"))
                return Enchantment.DIG_SPEED;
            if (enchant.equalsIgnoreCase("DURABILITY") || enchant.equalsIgnoreCase("Unbreaking"))
                return Enchantment.DURABILITY;
            if (enchant.equalsIgnoreCase("FIRE_ASPECT") || enchant.equalsIgnoreCase("Fire Aspect"))
                return Enchantment.FIRE_ASPECT;
            if (enchant.equalsIgnoreCase("KNOCKBACK"))
                return Enchantment.KNOCKBACK;
            if (enchant.equalsIgnoreCase("LOOT_BONUS_BLOCKS") || enchant.equalsIgnoreCase("Fortune"))
                return Enchantment.LOOT_BONUS_BLOCKS;
            if (enchant.equalsIgnoreCase("LOOT_BONUS_MOBS") || enchant.equalsIgnoreCase("Looting"))
                return Enchantment.LOOT_BONUS_MOBS;
            if (enchant.equalsIgnoreCase("LUCK") || enchant.equalsIgnoreCase("Luck of the Sea") || enchant.equalsIgnoreCase("luck_of_the_sea"))
                return Enchantment.LUCK;
            if (enchant.equalsIgnoreCase("OXYGEN") || enchant.equalsIgnoreCase("Respiration"))
                return Enchantment.OXYGEN;
            if (enchant.equalsIgnoreCase("PROTECTION_ENVIRONMENTAL") || enchant.equalsIgnoreCase("Protection"))
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            if (enchant.equalsIgnoreCase("PROTECTION_EXPLOSIONS") || enchant.equalsIgnoreCase("Blast Protection") || enchant.equalsIgnoreCase("blast_protection"))
                return Enchantment.PROTECTION_EXPLOSIONS;
            if (enchant.equalsIgnoreCase("PROTECTION_FALL") || enchant.equalsIgnoreCase("Feather Falling") || enchant.equalsIgnoreCase("feather_falling"))
                return Enchantment.PROTECTION_FALL;
            if (enchant.equalsIgnoreCase("PROTECTION_FIRE") || enchant.equalsIgnoreCase("Fire Protection") || enchant.equalsIgnoreCase("fire_protection"))
                return Enchantment.PROTECTION_FIRE;
            if (enchant.equalsIgnoreCase("PROTECTION_PROJECTILE") || enchant.equalsIgnoreCase("Projectile Protection") || enchant.equalsIgnoreCase("projectile_protection"))
                return Enchantment.PROTECTION_PROJECTILE;
            if (enchant.equalsIgnoreCase("SILK_TOUCH") || enchant.equalsIgnoreCase("Silk Touch"))
                return Enchantment.SILK_TOUCH;
            if (enchant.equalsIgnoreCase("THORNS"))
                return Enchantment.THORNS;
            if (enchant.equalsIgnoreCase("WATER_WORKER") || enchant.equalsIgnoreCase("Aqua Affinity") || enchant.equalsIgnoreCase("aqua_affinity"))
                return Enchantment.WATER_WORKER;
            return null;
        }
    }
}
