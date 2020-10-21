package utils;

import com.github.dec4234.decsloginsecurity2.DecsLoginSecurity2Main;

public class NMSUtils {

	public static Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server." + DecsLoginSecurity2Main.getInstance().getVersion() + "." + name);
		} catch (ClassNotFoundException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
}
