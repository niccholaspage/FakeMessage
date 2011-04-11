package com.niccholaspage.FakeMessage;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class FakeMessage extends JavaPlugin
{
  public static PermissionHandler Permissions = null;
  public FakeMessagePlayerListener playerListener = new FakeMessagePlayerListener(this);
  public CommandHandler commandHandler = new CommandHandler(this);
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
	  Player player;
	  if (getServer().getPlayer(name) == null){
		  CraftServer cServer = (CraftServer)getServer();
		  CraftWorld cWorld = (CraftWorld)getServer().getWorlds().get(0);
		  EntityPlayer fakeEntityPlayer = new EntityPlayer(cServer.getHandle().c,cWorld.getHandle(),name,new ItemInWorldManager(cWorld.getHandle()));
		  player = (Player)fakeEntityPlayer.getBukkitEntity();
	  }else {
		  player = getServer().getPlayer(name);
	  }
	  String out = str.replace("+name", name).replace("+message", message);
	  if (!(Permissions == null)){
		  String world = player.getWorld().getName();
		  String group = Permissions.getGroup(world, player.getName());
		  String prefix = Permissions.getGroupPrefix(world, group);
		  String suffix = Permissions.getGroupSuffix(world, group);
		  String userPrefix = Permissions.getPermissionString(world, player.getName(), "prefix");
		  String userSuffix = Permissions.getPermissionString(world, player.getName(), "suffix");
		  if (userPrefix != null) prefix = userPrefix;
		  if (userSuffix != null) suffix = userSuffix;
		  if (prefix == null) prefix = "";
		  if (suffix == null) suffix = "";
		  out = out.replace("+group", group);
		  out = out.replace("+prefix", prefix);
		  out = out.replace("+suffix", suffix);
		  out = out.replace("+world", world);
	  }
	  out = out.replace("&", "¤");
	  if (display) System.out.println(out);
	  return out;
  }
  
  public Boolean hasPermission(CommandSender sender, String node){
	  if (sender instanceof Player){
		  Player player = (Player) sender;
      if (!(Permissions == null)){
          if (FakeMessage.Permissions.has(player, node)) return true;
      }else if (player.isOp() == true) return true;
      return false;
	  }else return true;
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
    
    getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Monitor, this);

    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    readConfig();
    setupPermissions();
    registerCommands();
  }
  private void registerCommands(){
	  getCommand("fsay").setExecutor(commandHandler);
	  getCommand("fjoin").setExecutor(commandHandler);
	  getCommand("fleave").setExecutor(commandHandler);
	  getCommand("fj").setExecutor(commandHandler);
	  getCommand("fl").setExecutor(commandHandler);
	  getCommand("fmsg").setExecutor(commandHandler);
	  getCommand("fswitch").setExecutor(commandHandler);
  }
}

class FakeMessagePlayerListener extends PlayerListener {
	public static FakeMessage pl;
	public FakeMessagePlayerListener(FakeMessage instance) {
		pl = instance;
	}
	public void onPlayerChat(PlayerChatEvent event){
		pl.commandHandler.onPlayerChat(event);
	}
}