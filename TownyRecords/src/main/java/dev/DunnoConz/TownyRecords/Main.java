package dev.DunnoConz.TownyRecords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.TownyChat.Chat;
import com.palmergames.bukkit.TownyChat.channels.Channel;

public class Main extends JavaPlugin implements Listener {
	private Map<Player, Recorder> PlayerRecorders;
	private Map<Channel, ArrayList<Recorder>> RecorderList;

	
	public void StartRecord(Player player) {
		final Channel channel = Chat.getTownyChat().getPlayerChannel(player);
		Recorder recorder = new Recorder();
		
		PlayerRecorders.put(player, recorder);
		
		if (RecorderList.get(channel) == null){
			ArrayList<Recorder> newArray = new ArrayList<Recorder>();
			RecorderList.put(channel, newArray);
		}
		RecorderList.get(channel).add(recorder);
		
		return;
	}
	
	public void EndRecord(Player player) {
		final Channel channel = Chat.getTownyChat().getPlayerChannel(player);
		
		Recorder recorder = PlayerRecorders.get(player);
		
		RecorderList.get(channel).remove(recorder);
		PlayerRecorders.remove(player);
		
		// temp stuff until upload is possible
		ArrayList<String> finalData = recorder.Complete(true);
		
		for (String message: finalData) {
			player.sendMessage(message);
		}
	}
	
	private void Broadcast(Channel channel, String message) {
		if (RecorderList.get(channel) == null){
			ArrayList<Recorder> newArray = new ArrayList<Recorder>();
			RecorderList.put(channel, newArray);
		}
		
		for (Recorder recorder: RecorderList.get(channel)) {
			recorder.Log(message);
		}
	}
	
	@Override
	public void onEnable() {
		PlayerRecorders = new HashMap<Player, Recorder>();
		RecorderList = new HashMap<Channel, ArrayList<Recorder>>();
		
		Bukkit.getPluginManager().registerEvents( this, this);
	}

	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("recorder")) {
			
			if (args[0].equalsIgnoreCase("start")) {
				StartRecord((Player) sender);
				return true;
			} else if (args[0].equalsIgnoreCase("end")) {
				EndRecord((Player) sender);
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		final Channel channel = Chat.getTownyChat().getPlayerChannel(event.getPlayer());
		event.getPlayer().sendMessage(event.getMessage());
		
		if (RecorderList.get(channel) != null && RecorderList.get(channel).size() != 0) {
			Broadcast(channel, "[" + System.currentTimeMillis() + "] " + event.getPlayer().getDisplayName() + " said " + event.getMessage());
		}
		return;
	}
	
	@EventHandler 
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		final Channel channel = Chat.getTownyChat().getPlayerChannel(player);
		Recorder recorder = PlayerRecorders.get(player);
		
		if (recorder != null) {
			RecorderList.get(channel).remove(recorder);
			PlayerRecorders.remove(player);
		}
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		
		final Channel channel = Chat.getTownyChat().getPlayerChannel(player);
		Recorder recorder = PlayerRecorders.get(player);
		
		if (recorder != null) {
			RecorderList.get(channel).remove(recorder);
			PlayerRecorders.remove(player);
		}
	}
}
