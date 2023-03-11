package dev.DunnoConz.TownyRecords;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.TownyChat.Chat;
import com.palmergames.bukkit.TownyChat.channels.Channel;

public class Main extends JavaPlugin implements Listener {
	Logger log = Bukkit.getServer().getLogger();
	private static HashMap<Channel, HashMap<Number, String>> Recorder;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
		Recorder = new HashMap<Channel, HashMap<Number, String>>();
	}

	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			final Channel channel = Chat.getTownyChat().getPlayerChannel(player);
			
			if (label.equalsIgnoreCase("recorder") && args[0].equalsIgnoreCase("start")) {
				Recorder.put(channel, new HashMap<Number, String>());
				Recorder.get(channel).put(0, "START OF LOG");
				player.sendMessage("Channel is being recorded!");
				
				return true;
			} else if (label.equalsIgnoreCase("recorder") && args[0].equalsIgnoreCase("end")) {
				
				if (Recorder.get(channel) != null) {
					HashMap<Number, String> recorder = Recorder.get(channel);
					
					for (Map.Entry<Number, String> message : recorder.entrySet()) {
						player.sendMessage("[" + message.getKey().toString() + "] " + message.getValue());
					}
					
					Recorder.put(channel, null);
				} else {
					player.sendMessage("Channel is not currently being recorded...");
					return false;
				}
				
				return true;
				
			}
		}
		return false;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		final Channel channel = Chat.getTownyChat().getPlayerChannel(event.getPlayer());
		log.warning(event.getPlayer().getDisplayName() + " just chatted in " + channel);
		
		if (Recorder.get(channel) != null) {
			HashMap<Number, String> recorder = Recorder.get(channel);
			
			recorder.put(System.currentTimeMillis(), event.getPlayer().getDisplayName() + " said " + event.getMessage());
		}
	}
}
