package me.Zackpollard.CapsGuard;
     
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
     
public class CGPlayerListener implements Listener {
	public static CapsGuard cg;
    public CGPlayerListener(CapsGuard instance){
    	cg = instance;
        Bukkit.getServer().getPluginManager().registerEvents(this,instance);
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent pce){
    	Player player = pce.getPlayer();
    	String message = pce.getMessage();
    	double upper = 0;
        double lower = 0;
        for (int index = 0;index<message.length(); index++){
        	char character = message.charAt(index);
            if(Character.isLetter(character)){
            	if(Character.isUpperCase(character)){
            		upper++;
                } else {
                    lower++;
                }
            }    
    	}
        for(String word : cg.Blocked){
        	if(message.toLowerCase().contains(" ")){
	        	if(message.toLowerCase().contains(word.toLowerCase()+" ") || (message.toLowerCase().contains(" " + word.toLowerCase()))){
	        		for(String ignored : cg.ignored){ 
	        			if(message.toLowerCase().contains(ignored.toLowerCase())){
	        				return;
	        			}
	        		}
	        		if(pce.getPlayer().hasPermission("capsguard.bypass.blocked")){
	        			return;
	        		}
	       			pce.getPlayer().sendMessage(ChatColor.GOLD + cg.getConfig().getString("CapsGuard.MessageOnBlocked"));
	       			pce.setCancelled(true);
	       			for (Player p : pce.getPlayer().getServer().getOnlinePlayers()){
	       				if(p.hasPermission("capsguard.alert.blocked")){
	       					p.sendMessage(ChatColor.RED+ "[CapsGuard] " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + " Tried To Say " + word);
	       					return;
	       				}
	       			}	
	        	}
	        } else {
	        	if(!message.toLowerCase().contains(" ")){
	        		if(message.toLowerCase().equals(word.toLowerCase())){
		        		if(pce.getPlayer().hasPermission("capsguard.bypass.blocked")){
		        			return;
		        		}
		       			pce.getPlayer().sendMessage(ChatColor.GOLD + cg.getConfig().getString("CapsGuard.MessageOnBlocked"));
		       			pce.setCancelled(true);
		       			for (Player p : pce.getPlayer().getServer().getOnlinePlayers()){
		       				if(p.hasPermission("capsguard.alert.blocked")){
		       					p.sendMessage(ChatColor.RED+ "[CapsGuard] " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + " Tried To Say " + word);
		       					return;
		       				}
		       			}
	        		}
	        	}
	        }
        }
        if(cg.getConfig().getBoolean("CapsGuard.ToLowerCase")){
        	if(message.length() < cg.getConfig().getLong("CapsGuard.DontCheckIfShorterThan")) return;
            for(String ignored : cg.ignored){ 
            	if(message.toLowerCase().contains(ignored.toLowerCase())){
            		return;
            	}
            }
        	if(pce.getPlayer().hasPermission("capsguard.bypass.caps"))return;
        	double test = (upper / (upper + lower)) * 100;
        	if (test > cg.getConfig().getLong("CapsGuard.Percentage")) {
        		pce.setMessage(pce.getMessage().toLowerCase());
        		return;
        	}
        }
        if(pce.getPlayer().hasPermission("capsguard.bypass.caps"))return;
        if(message.length() < cg.getConfig().getLong("CapsGuard.DontCheckIfShorterThan"))return;
        for(String ignored : cg.ignored){ 
        	if(message.toLowerCase().contains(ignored.toLowerCase())){
        		return;
        	}
        }
        if(cg.getConfig().getBoolean("CapsGuard.ToLowerCase"))return;
        double test = (upper / (upper + lower)) * 100;
        if (test > cg.getConfig().getLong("CapsGuard.Percentage")) {
            pce.setCancelled(true);
            pce.getPlayer().sendMessage(ChatColor.GOLD + cg.getConfig().getString("CapsGuard.MessageOnCaps"));
            for (Player p : pce.getPlayer().getServer().getOnlinePlayers()){
				if(p.hasPermission("capsguard.alert.caps")){
					p.sendMessage(ChatColor.RED+ "[CapsGuard] " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + " Tried To Use CAPS");
				}
            }
        }
    }
    public String getActualMessage(String msg){
    	int index = msg.indexOf("> ");
        return msg.substring(index);
    }
}