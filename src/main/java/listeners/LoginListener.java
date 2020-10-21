package listeners;

import com.github.dec4234.decsloginsecurity2.DecsLoginSecurity2Main;
import core.LoginSecurity;
import customEvents.DLSPlayerLoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import utils.Notifiers;

public class LoginListener implements Listener {

	LoginSecurity ls = new LoginSecurity();

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2100000000, 25));

		initTimer(p);
	}

	@EventHandler
	public void onLogin(DLSPlayerLoginEvent event) {
		Player p = event.getPlayer();
		p.removePotionEffect(PotionEffectType.BLINDNESS);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		ls.setLoggedInUsers(p.getUniqueId(), false);
	}

	public static void initTimer(Player p) {
		Long time = System.currentTimeMillis();
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2100000000, 25));
		new BukkitRunnable() {

			@Override
			public void run() {
				if (LoginSecurity.hasPlayerLoggedInBefore(p.getUniqueId())) {
					if (!LoginSecurity.isUserLoggedIn(p.getUniqueId())) {
						Notifiers.sendActionBarMessage(p, "§6§lPlease login using §a/login <password>");
					} else {
						Notifiers.sendActionBarMessage(p, " ");
						this.cancel();
					}
				} else {
					Notifiers.sendActionBarMessage(p, "§6§lPlease register a password §d/register <password>");
				}
				if (System.currentTimeMillis() - time > 60 * 1000 && !LoginSecurity.isUserLoggedIn(p.getUniqueId())) {
					p.kickPlayer("§7[§dDecsLoginSecurity§7]\n§6Timed out\n§aIf you need your password reset join our discord\n§9https://discord.gg/dyTzxeW");
					this.cancel();
				}
			}
		}.runTaskTimer(DecsLoginSecurity2Main.getInstance(), 10, 20);
	}
}
