package com.niccholaspage.FakeMessage;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class FakeMessage extends JavaPlugin
{
  //private final FakeMessagePlayerListener playerListener = new FakeMessagePlayerListener(this);
  public static PermissionHandler Permissions = null;
  static String messageFormat;
  static String privateMessageFormat;
  static String joinGame;
  static String leftGame;

  public void onDisable()
  {
    System.out.println("FakeMessage Disabled");
  }

  public void readConfig() {
	  File file = new File("plugins/FakeMessage/");
	  if (!(file.exists())){
		  file.mkdir();
	  }
	  file = new File("plugins/FakeMessage/config.yml");
  	if (!file.exists()){
	      try{
	    	    // Create file 
	    	    FileWriter fstream = new FileWriter("plugins/FakeMessage/config.yml");
	    	    BufferedWriter out = new BufferedWriter(fstream);
	    	    out.write("FakeMessage:\n");
	    	    out.write("    messageformat: '<+name> +message'\n");
	    	    out.write("    privatemessageformat: '(MSG) <+name> +message'\n");
	    	    out.write("    joingame: '&e+name has joined the game.'\n");
	    	    out.write("    leavegame: '&e+name has left the game.'\n");
	    	    //Close the output stream
	    	    out.close();
	    	    }catch (Exception e){//Catch exception if any
	    	      System.out.println("FakeMessage could not write the default config file.");
	    	    }
	}
	  Configuration _config = new Configuration(new File("plugins/FakeMessage/config.yml"));

	  _config.load();

	  messageFormat = _config.getString("FakeMessage.messageformat", "<+name> +message");
	  privateMessageFormat = _config.getString("FakeMessage.privatemessageformat", "(MSG) <+name> +message");
	  joinGame = _config.getString("FakeMessage.joingame", "&e+name has joined the game.");
	  leftGame = _config.getString("FakeMessage.leavegame", "&e+name has left the game.");
	  messageFormat = messageFormat.replaceAll("&", "¤");
	  privateMessageFormat = privateMessageFormat.replaceAll("&", "¤");
	  joinGame = joinGame.replaceAll("&", "¤");
	  leftGame = leftGame.replaceAll("&", "¤");
  }
  public void setupPermissions() {
    Plugin test = getServer().getPluginManager().getPlugin("Permissions");

    if (Permissions == null)
      if (test != null) {
        Permissions = ((Permissions)test).getHandler();
      } else {
        System.out.println("Permission system not enabled. FakeMessage commands will only work for OPs.");
      }
  }
  
  public String arrayListToString(ArrayList<String> list, String sep){
	  String ret = "";
	  for (int i = 0; i < list.size(); i++){
		  ret+= list.get(i) + sep;
	  }
	  return ret;
  }
  
  public String formatMessage(String str, String name, String message){
	  return str.replace("+name", name).replace("+message", message).replace("&", "¤");
  }
  
  public Boolean hasPermission(Player player, String node){
      if (!(Permissions == null)){
          if (FakeMessage.Permissions.has(player, node)) {
            return true;
          }
      }else if (player.isOp() == true){
    	  return true;
      }
      return false;
  }
  
  public Player getPlayerStartsWith(String startsWith){
  	if (getServer().getOnlinePlayers().length == 0){
  		return null;
  	}
  	for (int i = 0; i < getServer().getOnlinePlayers().length; i++){
  		if (getServer().getOnlinePlayers()[i].getName().toLowerCase().startsWith(startsWith)){
  			return getServer().getOnlinePlayers()[i];
  		}
  	}
  	return null;
  }

  public void onEnable()
  {
    //PluginManager pm = getServer().getPluginManager();

    //pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Normal, this);

    PluginDescriptionFile pdfFile = getDescription();

    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    readConfig();
    setupPermissions();
  }
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	  if (sender instanceof Player){
		  Player player = (Player) sender;
		  ArrayList<String> Args = new ArrayList<String>();
		  for (int i = 0; i < args.length; i++){
			  Args.add(args[i]);
		  }
		  if (cmd.getName().equalsIgnoreCase("fsay")){
			  if (!(hasPermission(player, "fakemessage.say"))) return true;
			  if (args.length < 2) return false;
			  String name = Args.get(0);
			  Args.remove(0);
			  getServer().broadcastMessage(formatMessage(messageFormat, name, arrayListToString(Args, " ")));
		  }else if ((cmd.getName().equalsIgnoreCase("fjoin")) || (cmd.getName().equalsIgnoreCase("fj"))){
			  if (!(hasPermission(player, "fakemessage.join"))) return true;
			  //TODO: Without any arguments, make the player who called it join
			  if (args.length < 1) return false;
			  getServer().broadcastMessage(formatMessage(joinGame, args[0], ""));
		  }else if ((cmd.getName().equalsIgnoreCase("fleave")) || (cmd.getName().equalsIgnoreCase("fl"))){
			  if (!(hasPermission(player, "fakemessage.leave"))) return true;
			  //TODO: Without any arguments, make the player who called it leave
			  if (args.length < 1) return false;
			  getServer().broadcastMessage(formatMessage(leftGame, args[0], ""));
		  }else if (cmd.getName().equalsIgnoreCase("fmsg")){
			  if (!(hasPermission(player, "fakemessage.msg"))) return true;
			  if (args.length < 3) return false;
			  Args.remove(0);
			  if (!(getPlayerStartsWith(args[0]) == null)) getPlayerStartsWith(args[0]).sendMessage(formatMessage(privateMessageFormat, args[1], arrayListToString(Args, " "))); else player.sendMessage(ChatColor.RED + "That user doesn't exist!");
		  }
	  }
	  return true;
  }
}