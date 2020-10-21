package listeners;

import core.LoginSecurity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class LoginCancelListener implements Listener {

	LoginSecurity ls = new LoginSecurity();

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if(ls.getLoggedInUsers().size() == Bukkit.getOnlinePlayers().size()) return;

		Player p = event.getPlayer();
		if(!ls.getLoggedInUsers().contains(p.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(ls.getLoggedInUsers().size() == Bukkit.getOnlinePlayers().size()) return;

		Player p = event.getPlayer();
		if(!ls.getLoggedInUsers().contains(p.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent event) {
		if(ls.getLoggedInUsers().size() == Bukkit.getOnlinePlayers().size()) return;

		Player p = event.getPlayer();
		if(!ls.getLoggedInUsers().contains(p.getUniqueId())) {
			if(event.getMessage().startsWith("/login") || event.getMessage().startsWith("/register")) {
				return;
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if(ls.getLoggedInUsers().size() == Bukkit.getOnlinePlayers().size()) return;

		Player p = event.getPlayer();
		if(!ls.getLoggedInUsers().contains(p.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(ls.getLoggedInUsers().size() == Bukkit.getOnlinePlayers().size()) return;

		Player p = event.getPlayer();
		if(!ls.getLoggedInUsers().contains(p.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		if(ls.getLoggedInUsers().size() == Bukkit.getOnlinePlayers().size()) return;

		Player p = event.getPlayer();
		if(!ls.getLoggedInUsers().contains(p.getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
