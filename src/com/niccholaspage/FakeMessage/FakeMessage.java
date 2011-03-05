package com.niccholaspage.FakeMessage;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.File;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class FakeMessage extends JavaPlugin
{
  private final FakeMessagePlayerListener playerListener = new FakeMessagePlayerListener(this);
  public static PermissionHandler Permissions = null;

  public void onDisable()
  {
    System.out.println("FakeMessage Disabled");
  }

  public void readConfig() {
    Configuration _config = new Configuration(new File("plugins/FakeMessage.yml"));

    _config.load();

    String messageformat = _config.getString("FakeMessage.messageformat", "<name> message");
    String joingame = _config.getString("FakeMessage.joingame", "&ename has joined the game.");
    String leftgame = _config.getString("FakeMessage.leavegame", "&ename has left the game.");
    messageformat = messageformat.replaceAll("&", "¤");
    joingame = joingame.replaceAll("&", "¤");
    leftgame = leftgame.replaceAll("&", "¤");
    FakeMessagePlayerListener.setMessages(messageformat, joingame, leftgame);
  }
  public void setupPermissions() {
    Plugin test = getServer().getPluginManager().getPlugin("Permissions");

    if (Permissions == null)
      if (test != null) {
        Permissions = ((Permissions)test).getHandler();
      } else {
        System.out.println("Permission system not enabled. Disabling FakeMessage.");
        getServer().getPluginManager().disablePlugin(this);
      }
  }

  public void onEnable()
  {
    PluginManager pm = getServer().getPluginManager();

    pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Normal, this);

    PluginDescriptionFile pdfFile = getDescription();

    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    readConfig();
    setupPermissions();
  }
}