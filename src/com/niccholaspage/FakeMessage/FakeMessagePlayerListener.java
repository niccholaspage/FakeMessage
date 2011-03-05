package com.niccholaspage.FakeMessage;

import com.nijiko.permissions.PermissionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class FakeMessagePlayerListener extends PlayerListener
{
  public static FakeMessage plugin;
  public static PermissionHandler Permissions = null;
  private static String messageFormat;
  private static String joingame;
  private static String leavegame;

  public FakeMessagePlayerListener(FakeMessage instance)
  {
    plugin = instance;
  }
  public static String arrayToString(String[] a, String separator) {
    StringBuffer result = new StringBuffer();
    if (a.length > 0) {
      result.append(a[0]);
      for (int i = 1; i < a.length; i++) {
        if (a[i].equals("")) {
          continue;
        }
        result.append(separator);
        result.append(a[i]);
      }
    }
    return result.toString();
  }

  public static void setMessages(String mf, String jg, String lg)
  {
    messageFormat = mf;
    joingame = jg;
    leavegame = lg;
  }
  public static String removeCharAt(String s, int pos) {
    StringBuffer buf = new StringBuffer(s.length() - 1);
    buf.append(s.substring(0, pos)).append(s.substring(pos + 1));
    return buf.toString();
  }

  public void onPlayerCommandPreprocess(PlayerChatEvent event)
  {
    String[] split = event.getMessage().split(" ");

    if ((split[0].equalsIgnoreCase("/fakemessage")) || 
      (split[0].equalsIgnoreCase("/say"))) {
      Player player = event.getPlayer();
      if (!FakeMessage.Permissions.has(player, "fakemessage.say")) {
        return;
      }
      split[0] = "";
      String theguy = split[1];
      split[1] = "";
      String message = arrayToString(split, " ");
      message = removeCharAt(message, 0);
      String text = messageFormat;
      text = text.replaceAll("name", theguy);
      text = text.replaceAll("message", message);

      plugin.getServer().broadcastMessage(text);
      event.setCancelled(false);
    }
    if ((split[0].equalsIgnoreCase("/join")) || 
      (split[0].equalsIgnoreCase("/j"))) {
      Player player = event.getPlayer();
      if (!FakeMessage.Permissions.has(player, "fakemessage.join")) {
        return;
      }
      String theguy = split[1];
      String text = joingame;
      text = text.replaceAll("name", theguy);
      plugin.getServer().broadcastMessage(text);
      event.setCancelled(false);
    }
    if ((split[0].equalsIgnoreCase("/leave")) || 
      (split[0].equalsIgnoreCase("/l"))) {
      Player player = event.getPlayer();
      if (!FakeMessage.Permissions.has(player, "fakemessage.leave")) {
        return;
      }
      String theguy = split[1];
      String text = leavegame;
      text = text.replaceAll("name", theguy);
      plugin.getServer().broadcastMessage(text);
      event.setCancelled(false);
    }
  }
}