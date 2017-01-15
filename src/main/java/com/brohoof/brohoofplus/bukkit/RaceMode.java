package com.brohoof.brohoofplus.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RaceMode extends Module {
    private boolean isActive;
    private World activeWorld;
    private int maxLevel;
    private int levelPerSecond;
    private float regenPerSecond;
    private int penaltyTime;
    private int penaltyStrength;
    private int taskID;
    private final Map<Player, Boolean> jumpFlags;
    private boolean showMessageOnPenalty;
    private String messageOnPenalty;
    private boolean debugMode;

    public RaceMode(final BrohoofPlusPlugin p) {
        super(p, "racemode");
        jumpFlags = new HashMap<>();
    }

    @Override
    protected Listener createListener() {
        return new RaceEvents();
    }

    private void setDebugMode(final boolean enabled) {
        debugMode = enabled;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1)
            return false;
        final String comm = args[0];
        if (comm.equals("start")) {
            if (sender instanceof ConsoleCommandSender) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.GOLD + "As the console, you need to specify the world name.");
                    return true;
                }
                final String worldName = args[1];
                final World world = Bukkit.getServer().getWorld(worldName);
                if (world != null)
                    activate(world);
                else
                    sender.sendMessage(ChatColor.GOLD + "This world does not exist!");
            } else
                activate(((Player) sender).getWorld());
        } else if (comm.equals("stop")) {
            deactivate();
            sender.sendMessage(ChatColor.GOLD + "Stopped!");
            return true;
        } else if (comm.equals("debug"))
            setDebugMode(!isDebugEnabled());
        return true;
    }

    private boolean isDebugEnabled() {
        return debugMode;
    }

    private void deactivate() {
        if (!isActive)
            return;
        plugin.getServer().getScheduler().cancelTask(taskID);
        for (final Player ply : plugin.getServer().getOnlinePlayers()) {
            ply.setExp(0f);
            ply.setLevel(0);
            ply.removePotionEffect(PotionEffectType.SLOW);
            ply.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Race mode: Stopped!");
        }
        isActive = false;
    }

    private void activate(final World world) {
        if (isActive)
            return;
        activeWorld = world;
        isActive = true;
        jumpFlags.clear();
        maxLevel = plugin.getConfig().getInt("modules.racemode.maxlevel");
        // Keep those instruction grouped in order!
        regenPerSecond = (float) plugin.getConfig().getDouble("modules.racemode.regenpersecond");
        levelPerSecond = (int) Math.floor(regenPerSecond);
        regenPerSecond = regenPerSecond - levelPerSecond;
        //
        penaltyTime = plugin.getConfig().getInt("modules.racemode.penaltytime");
        penaltyStrength = plugin.getConfig().getInt("modules.racemode.penaltystrength");
        showMessageOnPenalty = plugin.getConfig().getBoolean("modules.racemode.sendmessageonpenalty");
        messageOnPenalty = plugin.getConfig().getString("modules.racemode.messageonpenalty").replace('&', ChatColor.COLOR_CHAR);
        for (final Player ply : plugin.getServer().getOnlinePlayers()) {
            ply.setExp(0f);
            ply.setLevel(maxLevel);
        }
        plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Race mode: Started! (World:" + activeWorld.getName() + ")");
        // getServer().broadcastMessage(ChatColor.ITALIC + " Jumping decreases XP by 1 level (full bar).");
        plugin.getServer().broadcastMessage(ChatColor.ITALIC + "  When you jump, you lose 1 XP level.");
        plugin.getServer().broadcastMessage(ChatColor.ITALIC + "  XP regenerates at " + String.format("%.2f", regenPerSecond) + " level per second to a maximum of " + maxLevel + ".");
        plugin.getServer().broadcastMessage(ChatColor.ITALIC + "  When you jump while your XP level is 0, you gain Slowness (" + (penaltyStrength + 1) + ") for " + penaltyTime + " seconds, and your XP level is reset to " + maxLevel + ".");
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!isActive)
                return;
            for (final Player ply : activeWorld.getPlayers())
                if (ply.getLevel() < maxLevel) {
                    final float potentialExperience = ply.getExp() + regenPerSecond;
                    // Keep the ordering of those instructions...
                    int addedLevel = (int) Math.floor(potentialExperience);
                    final float nextLevelExperience = potentialExperience - addedLevel;
                    addedLevel = addedLevel + levelPerSecond;
                    //
                    // If we're going to add 1 or more levels
                    if (addedLevel > 0) {
                        int newLevel = ply.getLevel() + addedLevel;
                        ply.setLevel(newLevel);
                        // If it's going to be or excess the limit
                        if (newLevel >= maxLevel) {
                            newLevel = maxLevel;
                            ply.setExp(0);
                        } else
                            ply.setExp(nextLevelExperience);
                    } else
                        ply.setExp(potentialExperience);
                } else if (ply.getLevel() == maxLevel && ply.getExp() > 0f)
                    ply.setExp(0f);
                else if (ply.getLevel() > maxLevel) {
                    ply.setLevel(maxLevel);
                    ply.setExp(0f);
                }
        }, 20L, 20L);
        isActive = true;
    }

    public class RaceEvents implements Listener {
        @EventHandler
        public void onPlayerMove(final PlayerMoveEvent e) {
            if (!isActive)
                return;
            final Player ply = e.getPlayer();
            if (ply.getWorld() != activeWorld)
                return;
            if (!jumpFlags.containsKey(ply))
                jumpFlags.put(ply, false);
            if (e.getFrom().getY() < e.getTo().getY()) {
                if (ply.getLocation().getBlock().isLiquid()) {
                    if (isDebugEnabled())
                        plugin.getLogger().info("Player " + ply.getName() + " -> in liquid");
                } else if (e.getTo().getY() - e.getFrom().getY() == 0.5d) {
                    if (isDebugEnabled())
                        plugin.getLogger().info("Player " + ply.getName() + " -> half slab");
                } else if (jumpFlags.get(ply) == false) {
                    playerJumpEvent(e, false);
                    jumpFlags.put(ply, true);
                }
            } else if (jumpFlags.get(ply) == true)
                jumpFlags.put(ply, false);
        }

        private void playerJumpEvent(final PlayerMoveEvent e, final boolean dummy) {
            final Player ply = e.getPlayer();
            if (ply.getGameMode() == GameMode.CREATIVE)
                return;
            if (isDebugEnabled())
                plugin.getLogger().info("Player " + ply.getName() + " -> jump");
            final int level = ply.getLevel();
            if (level <= 0)
                triggerPenalty(ply);
            else
                ply.setLevel(level - 1);
        }

        private void triggerPenalty(final Player ply) {
            plugin.getLogger().info(ply.getName() + " got a penalty");
            ply.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, penaltyTime * 20, penaltyStrength));
            ply.setExp(0f);
            ply.setLevel(maxLevel);
            if (showMessageOnPenalty)
                ply.sendMessage(String.format(messageOnPenalty, penaltyTime, penaltyStrength + 1));
        }
    }
}
