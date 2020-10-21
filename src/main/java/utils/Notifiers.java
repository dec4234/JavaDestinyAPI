package utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Notifiers {

	public static void sendActionBarMessage(Player player, String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

	public static void sendActionbar(Player player, String message) {
		try {
			Constructor<?> constructor = NMSUtils.getNMSClass("PacketPlayOutChat").getConstructor(NMSUtils.getNMSClass("IChatBaseComponent"), byte.class);

			Object icbc = NMSUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");
			Object packet = constructor.newInstance(icbc, (byte) 2);
			Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

			playerConnection.getClass().getMethod("sendPacket", NMSUtils.getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | InstantiationException e) {
			e.printStackTrace();
		}
	}
}
