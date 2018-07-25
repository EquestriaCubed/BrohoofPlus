package com.brohoof.brohoofplus.bukkit;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;

public class Enchant extends Module {

    public Enchant(final BrohoofPlusPlugin p) {
        super(p, "enchant");
    }

    @Override
    public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
        if (pArgs.length != 3)
            return false;
        Player target = null;
        Enchantment enchant = null;
        try {
            enchant = getEnchant(Joiner.on(' ').join(ArrayUtils.subarray(pArgs, 1, pArgs.length - 1)));
        } catch (final Throwable e) {
            pSender.sendMessage(BrohoofPlusPlugin.BHP + "Not a valid enchantment.");
        }
        ItemStack item;
        int level;
        if (enchant == null) {
            pSender.sendMessage(BrohoofPlusPlugin.BHP + "Not a valid enchantment.");
            return true;
        }
        try {
            level = Integer.parseInt(pArgs[pArgs.length - 1]);
        } catch (final NumberFormatException e) {
            pSender.sendMessage(BrohoofPlusPlugin.BHP + "Your level is not a number.");
            return true;
        }
        for (final Player p : Bukkit.getOnlinePlayers())
            if (p.getName().toLowerCase().startsWith(pArgs[0].toLowerCase()))
                target = p;
        if (target == null) {
            pSender.sendMessage(BrohoofPlusPlugin.BHP + "Target player not found.");
            return true;
        }

        item = target.getInventory().getItemInMainHand();
        if (item == null) {
            pSender.sendMessage(BrohoofPlusPlugin.BHP + "Target player not holding an item.");
            return true;
        }
        item.addUnsafeEnchantment(enchant, level);
        pSender.sendMessage(BrohoofPlusPlugin.BHP + "Enchant successful.");
        return true;
    }

    @SuppressWarnings("deprecation")
    private Enchantment getEnchant(final String enchant) {
        // Try getting the id first.
        try {
            return Enchantment.getById(Integer.parseInt(enchant));
        } catch (final NumberFormatException e) {
            if (enchant.equalsIgnoreCase("ARROW_DAMAGE") || enchant.equalsIgnoreCase("Power"))
                return Enchantment.ARROW_DAMAGE;
            if (enchant.equalsIgnoreCase("ARROW_FIRE") || enchant.equalsIgnoreCase("Flame"))
                return Enchantment.ARROW_FIRE;
            if (enchant.equalsIgnoreCase("ARROW_INFINITE") || enchant.equalsIgnoreCase("Infinity"))
                return Enchantment.ARROW_INFINITE;
            if (enchant.equalsIgnoreCase("ARROW_KNOCKBACK") || enchant.equalsIgnoreCase("Punch"))
                return Enchantment.ARROW_KNOCKBACK;
            if (enchant.equalsIgnoreCase("BINDING_CURSE") || enchant.equalsIgnoreCase("Curse of Binding") || enchant.equalsIgnoreCase("curse_of_binding"))
                return Enchantment.BINDING_CURSE;
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
            if (enchant.equalsIgnoreCase("FROST_WALKER") || enchant.equalsIgnoreCase("Frost Walker"))
                return Enchantment.FROST_WALKER;
            if (enchant.equalsIgnoreCase("KNOCKBACK"))
                return Enchantment.KNOCKBACK;
            if (enchant.equalsIgnoreCase("LOOT_BONUS_BLOCKS") || enchant.equalsIgnoreCase("Fortune"))
                return Enchantment.LOOT_BONUS_BLOCKS;
            if (enchant.equalsIgnoreCase("LOOT_BONUS_MOBS") || enchant.equalsIgnoreCase("Looting"))
                return Enchantment.LOOT_BONUS_MOBS;
            if (enchant.equalsIgnoreCase("LUCK") || enchant.equalsIgnoreCase("Luck of the Sea") || enchant.equalsIgnoreCase("luck_of_the_sea"))
                return Enchantment.LUCK;
            if (enchant.equalsIgnoreCase("MENDING"))
                return Enchantment.MENDING;
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

            if (enchant.equalsIgnoreCase("VANISHING_CURSE") || enchant.equalsIgnoreCase("Curse of Vanishing") || enchant.equalsIgnoreCase("curse_of_vanishing"))
                return Enchantment.VANISHING_CURSE;
            return null;
        }
    }
}
