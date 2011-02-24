//The Package
package com.niccholaspage.FakeMessage;
//All the imports
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import com.nijiko.permissions.PermissionHandler;
//import org.bukkit.event.server.ServerEvent;
//Starts the class FakeMessagePlayer listener
public class FakeMessagePlayerListener extends PlayerListener{
	 public static FakeMessage plugin;
	 public static PermissionHandler Permissions = null;
	  public FakeMessagePlayerListener(FakeMessage instance) {
	        plugin = instance;
	    }
	  public static String arrayToString(String[] a, String separator) {
		    StringBuffer result = new StringBuffer();
		    if (a.length > 0) {
		        result.append(a[0]);
		        for (int i=1; i<a.length; i++) {
		        	if (!(a[i].equals(""))){
			            result.append(separator);
			            result.append(a[i]);
		        	}
		        }
		    }
		    return result.toString();
		}
	  private static String messageFormat;
	  private static String joingame;
	  private static String leavegame;
	  private static String dgroup;

	  public static void setMessages(String mf, String jg, String lg, String dg) {
	   messageFormat = mf;
	   joingame = jg;
	   leavegame = lg;
	   dgroup = dg;
	  }
	  public static String removeCharAt(String s, int pos) {
		   StringBuffer buf = new StringBuffer( s.length() - 1 );
		   buf.append( s.substring(0,pos) ).append( s.substring(pos+1) );
		   return buf.toString();
		}
	  //This method is called whenever a player uses a command.
	  public void onPlayerCommand(PlayerChatEvent event) {
		  //Make the message a string.
			String[] split = event.getMessage().split(" ");
			//Get the player that talked.
			//Player player = event.getPlayer();
			//If the first part of the string is /FakeMessage or /b then do this.
			if ((split[0].equalsIgnoreCase("/fakemessage"))
					|| (split[0].equalsIgnoreCase("/say"))) {
				Player player = event.getPlayer();
				if (!plugin.Permissions.has(player, "fakemessage.say")) {
				    return;
				}
				split[0] = "";
				String theguy = split[1];
				split[1] = "";
				String message = arrayToString(split, " ");
				message = removeCharAt(message, 0);
				String text = messageFormat;
				text = text.replaceAll("name",theguy);
				text = text.replaceAll("message", message);
				text = text.replaceAll("group", dgroup);
				//plugin.getServer().broadcastMessage("[Default] " + theguy + ": " + message);
				plugin.getServer().broadcastMessage(text);
				event.setCancelled(false);
			}
			if ((split[0].equalsIgnoreCase("/groupfakemessage"))
					|| (split[0].equalsIgnoreCase("/gsay"))) {
				Player player = event.getPlayer();
				if (!plugin.Permissions.has(player, "fakemessage.gsay")) {
				    return;
				}
				split[0] = "";
				String thegroup = split[1];
				split[1] = "";
				String theguy = split[2];
				split[2] = "";
				String message = arrayToString(split," ");
				message = removeCharAt(message, 0);
				String text = messageFormat;
				text = text.replaceAll("name",theguy);
				text = text.replaceAll("message", message);
				text = text.replaceAll("group", thegroup);
				plugin.getServer().broadcastMessage(text);
				event.setCancelled(false);
			}
			if ((split[0].equalsIgnoreCase("/completefakemessage"))
					|| (split[0].equalsIgnoreCase("/csay"))) {
				Player player = event.getPlayer();
				if (!plugin.Permissions.has(player, "fakemessage.csay")) {
				    return;
				}
				split[0] = "";
				String message = arrayToString(split, " ");
				message = removeCharAt(message, 0);
				message = message.replaceAll("~","¤");
				plugin.getServer().broadcastMessage(message);
			}
			if ((split[0].equalsIgnoreCase("/join"))
					|| (split[0].equalsIgnoreCase("/j"))) {
				Player player = event.getPlayer();
				if (!plugin.Permissions.has(player, "fakemessage.join")) {
				    return;
				}
				String theguy = split[1];
				String text = joingame;
				text = text.replaceAll("name", theguy);
				plugin.getServer().broadcastMessage(text);
				event.setCancelled(false);
			}
			if ((split[0].equalsIgnoreCase("/leave"))
					|| (split[0].equalsIgnoreCase("/l"))) {
				Player player = event.getPlayer();
				if (!plugin.Permissions.has(player, "fakemessage.leave")) {
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
