package com.github.dec4234.decsloginsecurity2;

import core.LoginCommands;
import listeners.LoginCancelListener;
import listeners.LoginListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class DecsLoginSecurity2Main extends JavaPlugin {

	private static DecsLoginSecurity2Main instance;
	public File ymlFile = new File(getDataFolder() + "/playerLoginInfo.yml");
	public FileConfiguration ymlConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.ymlFile);
	public FileConfiguration config = getConfig();

	@Override
	public void onEnable() {
		instance = this;
		register();

		for(Player p : Bukkit.getOnlinePlayers()) {
			LoginListener.initTimer(p);
		}
	}

	public void saveYml() {
		try {
			this.ymlConfig.save(this.ymlFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void register() {
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new LoginListener(), this);
		pm.registerEvents(new LoginCancelListener(), this);

		getCommand("register").setExecutor(new LoginCommands());
		getCommand("login").setExecutor(new LoginCommands());
		getCommand("resetpassword").setExecutor(new LoginCommands());
	}

	public String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	public static DecsLoginSecurity2Main getInstance() { return instance; }
}
