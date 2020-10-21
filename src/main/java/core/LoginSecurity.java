package core;

import com.github.dec4234.decsloginsecurity2.DecsLoginSecurity2Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class LoginSecurity {

	private static ArrayList<UUID> loggedInUsers = new ArrayList<>();
	private static FileConfiguration config = DecsLoginSecurity2Main.getInstance().ymlConfig;

	public static boolean hasPlayerLoggedInBefore(UUID uuid) {
		return config.contains(uuid.toString());
	}

	public static boolean isUserLoggedIn(UUID uuid) {
		return loggedInUsers.contains(uuid);
	}

	public void setLoggedInUsers(UUID uuid, boolean condition) {
		if(condition) {
			loggedInUsers.add(uuid);
		} else {
			loggedInUsers.remove(uuid);
		}
	}

	public static ArrayList<UUID> getLoggedInUsers() {
		return LoginSecurity.loggedInUsers;
	}
}
