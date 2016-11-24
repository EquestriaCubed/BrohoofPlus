package com.brohoof.brohoofplus.bukkit;

import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

class BanUUIDs implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender pSender, final Command pCommand, final String pLabel, final String[] pArgs) {
        if (pArgs.length == 0) {
            pSender.sendMessage("Not enough arguments.");
            return false;
        }
        switch (pCommand.getName().toLowerCase()) {
            case "ban": {
                UUID uuid;
                final String reason = Joiner.on(' ').join(ArrayUtils.subarray(pArgs, 1, pArgs.length));;
                OfflinePlayer target = null;
                try {
                    uuid = UUID.fromString(pArgs[0]);
                } catch (final IllegalArgumentException e) {
                    for (final Player p : Bukkit.getOnlinePlayers())
                        if (p.getName().equalsIgnoreCase(pArgs[0]))
                            target = p;
                    if (target == null) {
                        Bukkit.getServer().dispatchCommand(pSender, "commandbook:ban -eo " + pArgs[0] + " " + reason);
                        Bukkit.getServer().dispatchCommand(pSender, "note " + pArgs[0] + " Banned " + reason);
                        Bukkit.getServer().broadcastMessage("§eBanned " + pArgs[0] + " for " + reason);
                        return true;
                    }
                    Bukkit.getServer().dispatchCommand(pSender, "commandbook:ban -eo " + target.getName() + " " + reason);
                    Bukkit.getServer().dispatchCommand(pSender, "note " + target.getUniqueId().toString() + " Banned " + reason);
                    Bukkit.getServer().broadcastMessage("§eBanned " + target.getName() + " for " + reason);
                    return true;
                }
                target = Bukkit.getOfflinePlayer(uuid);
                Bukkit.getServer().dispatchCommand(pSender, "commandbook:ban -eo " + target.getName() + " " + reason);
                Bukkit.getServer().dispatchCommand(pSender, "note " + uuid.toString() + " Banned " + reason);
                Bukkit.getServer().broadcastMessage("§eBanned " + target.getName() + " for " + reason);
                return true;
            }
            case "isbanned": {
                UUID uuid;
                OfflinePlayer target;
                try {
                    uuid = UUID.fromString(pArgs[0]);
                } catch (final IllegalArgumentException e) {
                    Bukkit.getServer().dispatchCommand(pSender, "commandbook:isbanned " + pArgs[0]);
                    return true;
                }
                target = Bukkit.getOfflinePlayer(uuid);
                Bukkit.getServer().dispatchCommand(pSender, "commandbook:isbanned " + target.getName());
                return true;
            }
            case "baninfo": {
                UUID uuid;
                OfflinePlayer target;
                try {
                    uuid = UUID.fromString(pArgs[0]);
                } catch (final IllegalArgumentException e) {
                    Bukkit.getServer().dispatchCommand(pSender, "commandbook:baninfo " + pArgs[0]);
                    return true;
                }
                target = Bukkit.getOfflinePlayer(uuid);
                Bukkit.getServer().dispatchCommand(pSender, "commandbook:baninfo " + target.getName());
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
