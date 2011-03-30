package com.niccholaspage.FakeMessage;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class FakeMessage extends JavaPlugin
{
  public static PermissionHandler Permissions = null;
  String messageFormat;
  String privateMessageFormat;
  String joinGame;
  String leftGame;

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
  
  public String formatMessage(String str, String name, String message, boolean display){
	  String out = str.replace("+name", name).replace("+message", message).replace("&", "¤");
	  if (display) System.out.println(out);
	  return out;
  }
  
  public Boolean hasPermission(CommandSender sender, String node){
	  if (sender instanceof Player){
		  Player player = (Player) sender;
      if (!(Permissions == null)){
          if (FakeMessage.Permissions.has(player, node)) {
            return true;
          }
      }else if (player.isOp() == true){
    	  return true;
      }
      return false;
	  }else {
		  return true;
	  }
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

  public void onEnable(){
    PluginDescriptionFile pdfFile = getDescription();

    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    readConfig();
    setupPermissions();
    registerCommands();
  }
  private void registerCommands(){
	  CommandHandler commandHandler = new CommandHandler(this);
	  getCommand("fsay").setExecutor(commandHandler);
	  getCommand("fjoin").setExecutor(commandHandler);
	  getCommand("fleave").setExecutor(commandHandler);
	  getCommand("fj").setExecutor(commandHandler);
	  getCommand("fl").setExecutor(commandHandler);
	  getCommand("fmsg").setExecutor(commandHandler);
  }
}
