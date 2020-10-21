package customEvents;

import com.github.dec4234.decsloginsecurity2.DecsLoginSecurity2Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DLSPlayerLoginEvent extends Event {

	private Player player;
	private static final HandlerList handlers = new HandlerList();

	public DLSPlayerLoginEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
