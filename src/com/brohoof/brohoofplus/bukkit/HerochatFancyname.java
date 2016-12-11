package com.brohoof.brohoofplus.bukkit;

import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.dthielke.Herochat;
import com.dthielke.api.event.ChannelChatEvent;

public class HerochatFancyname extends Module {
	private final HashMap<Player, String> enabledUsers = new HashMap<Player, String>(256);

	public HerochatFancyname(final BrohoofPlusPlugin p) {
		super(p, "fancyname");
		listener = new HerochatFancynameListener();
	}

	private void add(final Player p, final String nick) {
		enabledUsers.put(p, nick);
	}

	private void remove(final Player p) {
		enabledUsers.remove(p);
	}

	@Override
	public boolean onCommand(final CommandSender pSender, final Command command, final String label, final String[] pArgs) {
		if (pArgs.length == 0)
			return false;
		if (pSender instanceof Player) {
			final Player p = (Player) pSender;
			if (pArgs.length == 1) {
				remove(p);
				pSender.sendMessage(BrohoofPlusPlugin.BHP + "Your name is now normal.");
				return true;
			}
			for (String str : (String[]) ArrayUtils.subarray(pArgs, 1, pArgs.length))
				if (str.toCharArray()[0] != '&' || str.length() != 2) {
					pSender.sendMessage(BrohoofPlusPlugin.BHP + "It doesn't look like " + str + " is a colour code, so we won't use it.");
					str = "";
					return true;
				}
			String nameToDisplay = "";
			final char[] letters = p.getName().toCharArray();
			int j = 0;
			while (j < letters.length)
				for (int i = 1; i <= pArgs.length - 1; i++) {
					// Sometimes an ArrayIndexOutOfBoundsException is thrown if we don't do this.
					if (j >= letters.length)
						break;
					final char letter = letters[j];
					final String colourToAppend = ChatColor.translateAlternateColorCodes('&', pArgs[i]);
					nameToDisplay += colourToAppend + letter;
					j++;
				}
			add(p, nameToDisplay);
			pSender.sendMessage(BrohoofPlusPlugin.BHP + "Your name is now displayed as " + nameToDisplay);
			return true;
		}
		pSender.sendMessage(BrohoofPlusPlugin.BHP + "Sorry, this command can only be used by players.");
		return true;

	}

	public class HerochatFancynameListener extends ModuleListener {
		@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
		public void onHeroChatMessage(final ChannelChatEvent event) {
			final Player player = event.getChatter().getPlayer();
			final String nick = enabledUsers.get(player);
			if (enabledUsers.containsKey(player)) {
				if (nick != null && !nick.isEmpty()) {
					if (!event.getFormat().equalsIgnoreCase("{default}")) {
						final String format = event.getFormat().replace("{sender}", nick);
						event.setFormat(format);
						return;
					}
					final String format = Herochat.getInstance().getConfig().getString("format.default").replace("{sender}", nick);
					event.setFormat(format);
					return;
				}
				return;
			}
			return;
		}
	}
}
