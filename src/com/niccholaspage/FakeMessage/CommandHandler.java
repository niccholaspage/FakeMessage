package com.niccholaspage.FakeMessage;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

public class CommandHandler implements CommandExecutor {
	public static FakeMessage pl;
	public CommandHandler(FakeMessage instance) {
		pl = instance;
	}
	HashMap<String, String> players = new HashMap<String, String>();
	  public boolean onCommand(CommandSender player, Command cmd, String commandLabel, String[] args) {
		  ArrayList<String> Args = new ArrayList<String>();
		  for (int i = 0; i < args.length; i++){
			  Args.add(args[i]);
		  }
		  if (cmd.getName().equalsIgnoreCase("fsay")){
			  if (!(pl.hasPermission(player, "fakemessage.say"))) return true;
			  if (args.length < 2) return false;
			  Args.remove(0);
			  pl.getServer().broadcastMessage(pl.formatMessage(pl.messageFormat, args[0], pl.arrayListToString(Args, " "),true));
		  }else if ((cmd.getName().equalsIgnoreCase("fjoin")) || (cmd.getName().equalsIgnoreCase("fj"))){
			  if (!(pl.hasPermission(player, "fakemessage.join"))) return true;
			  //TODO: Without any arguments, make the player who called it join
			  if (args.length < 1) return false;
			  pl.getServer().broadcastMessage(pl.formatMessage(pl.joinGame, args[0], "", true));
		  }else if ((cmd.getName().equalsIgnoreCase("fleave")) || (cmd.getName().equalsIgnoreCase("fl"))){
			  if (!(pl.hasPermission(player, "fakemessage.leave"))) return true;
			  //TODO: Without any arguments, make the player who called it leave
			  if (args.length < 1) return false;
			  pl.getServer().broadcastMessage(pl.formatMessage(pl.leftGame, args[0], "", true));
		  }else if (cmd.getName().equalsIgnoreCase("fmsg")){
			  if (!(pl.hasPermission(player, "fakemessage.message"))) return true;
			  if (args.length < 3) return false;
			  Args = new ArrayList<String>(Args.subList(2, Args.size()));
			  if (!(pl.getPlayerStartsWith(args[0]) == null)) pl.getPlayerStartsWith(args[0].toLowerCase()).sendMessage(pl.formatMessage(pl.privateMessageFormat, args[1], pl.arrayListToString(Args, " "), false)); else player.sendMessage(ChatColor.RED + "That user doesn't exist!");
		  }else if (cmd.getName().equalsIgnoreCase("fswitch")){
			  if (!(pl.hasPermission(player, "fakemessage.switch"))) return true;
			  if (!(player instanceof Player)){
				  player.sendMessage("Only players are supported right now.");
				  return true;
			  }
			  if (args.length < 1){
				  if (players.containsKey(((Player)player).getName())){
					  players.remove(((Player)player).getName());
					  player.sendMessage(ChatColor.GREEN + "You are now not faking anybody.");
					  return true;
				  }else {
					  return false;
				  }
			  }
			  if (players.containsKey(((Player)player).getName())) players.remove(((Player)player).getName());
			  players.put(((Player)player).getName(), args[0]);
			  player.sendMessage(ChatColor.GREEN + "You are now faking the player '" + args[0] + "'.");
		  }
	  return true;
  }
	  public void onPlayerChat(PlayerChatEvent event){
		  if (!(players.containsKey(event.getPlayer().getName()))) return;
		  event.setFormat(pl.formatMessage(pl.messageFormat, players.get(event.getPlayer().getName()), event.getMessage(), true));
	  }
}
