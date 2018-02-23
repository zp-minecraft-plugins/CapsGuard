package me.Zackpollard.CapsGuard;
     
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;
 
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CapsGuard extends JavaPlugin {
    static String dir = "plugins/CapsGuard";
    static File CGFile = new File(dir + File.separator + "BlockedAndIgnoredWords.txt");
    static Properties prop = new Properties();
    ArrayList<String> ignored = new ArrayList<String>();
    final String defaultIgnored = ":P,:D";
    ArrayList<String> Blocked = new ArrayList<String>();
    final String defaultBlocked = "IP,Crap";
    private static final Logger log = Logger.getLogger("Minecraft");
    public void onEnable() {
    	new CGPlayerListener(this);
		final FileConfiguration config = this.getConfig();
        config.options().header("Sets what is blocked ingame");
        config.addDefault("CapsGuard.Percentage", Integer.valueOf(60));
        config.addDefault("CapsGuard.DontCheckIfShorterThan", Integer.valueOf(5));
        config.addDefault("CapsGuard.MessageOnCaps", "YOU SEEM TO HAVE FORGOT YOUR CAPSLOCK WAS ON");
        config.addDefault("CapsGuard.MessageOnBlocked", "One Of The Words You Said Has Been Disallowed");
        config.addDefault("CapsGuard.ToLowerCase", true);
        config.options().copyDefaults(true);
        saveConfig();
        load();
        log.info("CapsGuard Version 2.0 Enabled");
    }
    public void onDisable() {
        log.info("CapsGuard Version 2.0 Disabled");
    }
    //Function for taking a string and separating it into substrings based
        //on the given delimiter.
        public ArrayList<String> parseArray (String in, char delimiter) {
        	ArrayList<String> parsed = new ArrayList<String>();
            String build = "";
            int count = 0;
            int len = in.length();
            while (count<len){
                while (count<len && in.charAt(count) != delimiter){
                    build += in.charAt(count);
                    count++;
                }
                parsed.add(build);
                build = "";
                count++;
            }
            return parsed;        
        }
    public void load(){
    	new File(dir).mkdir();
    	if(!CGFile.exists()){
    	    //create file if it doesn't exist.
    		try {
		        CGFile.createNewFile();
		        log.info("Generated CapsGuard IgnoredAndBlockedWords File");
	            FileOutputStream out = new FileOutputStream(CGFile);
	            prop.put("IgnoredWords", defaultIgnored);
	            prop.put("BlockedWords", defaultBlocked);
	            ignored = parseArray(defaultIgnored, ',');
	            Blocked = parseArray(defaultBlocked, ',');
	            //Remember To Change The Message Below
	            prop.store(out, "CapsGuard Blocked And Ignored Words List");
	            out.flush();
	            out.close();
	        } catch (IOException ex) {
	        	ex.printStackTrace();
	        }
    	} else {
    		//properties file exists - load
    		try {
    			FileInputStream in = new FileInputStream(CGFile);
    			prop.load(in);
    			ignored = parseArray(prop.getProperty("IgnoredWords"), ',');
    			Blocked = parseArray(prop.getProperty("BlockedWords"), ',');
    			in.close();
    			log.info("Loaded CapsGuard Config");
    		} catch (FileNotFoundException e) {
    			log.info("File exists but wasn't found - what did you do?!");
    		} catch (IOException e) {
    			log.info("IO Exception on load of properties");
    			e.printStackTrace();
    		}
    	}
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	boolean op = false;
    	if(sender instanceof Player){
    		Player player = (Player) sender;
    		op = player.isOp();
    	}
    	if(label.equalsIgnoreCase("cg")){
    		if(op || sender instanceof ConsoleCommandSender){
    			if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
    				load();
    				this.reloadConfig();
    				log.info("Reloaded Config File");
    				if(op){
    					sender.sendMessage(ChatColor.GOLD + "CapsGuard Config Reloaded");
    				}
    				return true;         
    			}
    		}
    	}
    	return false;
    }
}