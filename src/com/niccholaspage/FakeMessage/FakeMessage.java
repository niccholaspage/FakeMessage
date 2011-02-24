//The Package
package com.niccholaspage.FakeMessage;
//All the imports
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

/**
 * FakeMessage for Bukkit
 *
 * @author niccholaspage
 */
//Starts the class
public class FakeMessage extends JavaPlugin{
	//Links the FakeMessagePlayerListener
	private final FakeMessagePlayerListener playerListener = new FakeMessagePlayerListener(this);
    public static PermissionHandler Permissions = null;
    
   
	public FakeMessage(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader)
    {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
      }

	@Override
	//When the plugin is disabled this method is called.
	public void onDisable() {
		//Print "FakeMessage Disabled" on the log.
		System.out.println("FakeMessage Disabled");
		
	}
    public void readConfig() {
    	Configuration _config = new Configuration(new File("plugins/FakeMessage.yml"));

    	_config.load();
    	 //Check if file exists
    	File file=new File("plugins/FakeMessage.yml");
    	if (!file.exists()){
    	      try{
    	    	    // Create file 
    	    	    FileWriter fstream = new FileWriter("plugins/FakeMessage.yml");
    	    	        BufferedWriter out = new BufferedWriter(fstream);
    	    	    out.write("FakeMessage:\n");
    	    	    out.write("    messageformat: '<name> message'\n");
    	    	    out.write("    joingame: '&ename has joined the game.'\n");
    	    	    out.write("    leavegame: '&ename has left the game.'\n");
    	    	    out.write("    defaultgroupname: 'Default'\n");
    	    	    //Close the output stream
    	    	    out.close();
    	    	    }catch (Exception e){//Catch exception if any
    	    	      System.out.println("FakeMessage could not write default config file.");
    	    	    }
    	}
    	// Reading from yml file
    	String messageformat = _config.getString("FakeMessage.messageformat", "<name> message");
    	String joingame = _config.getString("FakeMessage.joingame", "&ename has joined the game.");
    	String leftgame = _config.getString("FakeMessage.leavegame", "&ename has left the game.");
    	String dgroup = _config.getString("FakeMessage.defaultgroupname","Default");
    	messageformat = messageformat.replaceAll("&", "¤");
    	joingame = joingame.replaceAll("&", "¤");
    	leftgame = leftgame.replaceAll("&", "¤");
    	FakeMessagePlayerListener.setMessages(messageformat, joingame, leftgame, dgroup);
        }
    public void setupPermissions() {
    	Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");


    	if(this.Permissions == null) {
    	    if(test != null) {
    		this.Permissions = ((Permissions)test).getHandler();
    	    } else {
    		System.out.println("Permission system not enabled. Disabling FakeMessage.");
    		this.getServer().getPluginManager().disablePlugin(this);
    	    }
    	}
        }

	@Override
	//When the plugin is enabled this method is called.
	public void onEnable() {
		//Create the pluginmanage pm.
		PluginManager pm = getServer().getPluginManager();
		//Create PlayerCommand listener
	    pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener, Event.Priority.Normal, this);
       //Get the infomation from the yml file.
        PluginDescriptionFile pdfFile = this.getDescription();
        //Print that the plugin has been enabled!
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
        readConfig();
        setupPermissions();
	}
}
