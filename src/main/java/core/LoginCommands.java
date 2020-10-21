package core;

import com.github.dec4234.decsloginsecurity2.DecsLoginSecurity2Main;
import customEvents.DLSPlayerLoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LoginCommands implements CommandExecutor {

	FileConfiguration config = DecsLoginSecurity2Main.getInstance().ymlConfig;
	LoginSecurity ls = new LoginSecurity();

	@Override
	public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
		if(!(commandSender instanceof Player)) return true;
		Player p = (Player) commandSender;

		if(args.length == 0 || args.length > 1) {
			return false;
		}

		if(cmd.getName().equalsIgnoreCase("login")) {
			if(config.contains(p.getUniqueId().toString())) {
				config.set(p.getUniqueId().toString() + ".name", p.getName());
				if(args[0].equalsIgnoreCase(config.getString(p.getUniqueId().toString() + ".password"))) {
					ls.setLoggedInUsers(p.getUniqueId(), true);
					Bukkit.getPluginManager().callEvent(new DLSPlayerLoginEvent(p));
					p.sendMessage("§dLogged in");
				}
				DecsLoginSecurity2Main.getInstance().saveYml();
			}

		} else if (cmd.getName().equalsIgnoreCase("register")) {
			if(!config.contains(p.getUniqueId().toString())) {
				config.set(p.getUniqueId().toString() + ".name", p.getName());
				config.set(p.getUniqueId().toString() + ".password", args[0]);
				ls.setLoggedInUsers(p.getUniqueId(), true);
				Bukkit.getPluginManager().callEvent(new DLSPlayerLoginEvent(p));
				DecsLoginSecurity2Main.getInstance().saveYml();
				p.sendMessage("§dLogged in with password §6§k" + args[0]);
			}
		} else if (cmd.getName().equalsIgnoreCase("resetpassword") && p.isOp()) {
			String s = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
			if(s == null) {
				p.sendMessage("§d" + args[0] + " §cis not a known player");
				return true;
			}
			config.set(s, null);
			DecsLoginSecurity2Main.getInstance().saveYml();
			p.sendMessage("§cPassword reset for §6" + args[0]);
		}

		return true;
	}
}
