package me.powerpfannkuchen.justNiceStart;

import me.powerpfannkuchen.justNiceStart.commands.reload;
import me.powerpfannkuchen.justNiceStart.commands.start_command;
import me.powerpfannkuchen.justNiceStart.events.JoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static BossBar bossBar;
    public static Map<UUID, Integer> playerTasks = new HashMap<>();


    @Override
    public void onEnable() {
        saveDefaultConfig();

        start_command startCmd = new start_command(this);
        getCommand("start").setExecutor(startCmd);
        getCommand("start").setTabCompleter(startCmd);
        getCommand("cancel").setExecutor(startCmd);

        getCommand("jnsreload").setExecutor(new reload(this));
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);

        // Start-Log
        Bukkit.getConsoleSender().sendMessage("§6Plugin geladen: JustNiceStart v4.0");
    }

    public static void setBossBar(BossBar bar) {
        bossBar = bar;
    }

    public static BossBar getBossBar() {
        return bossBar;
    }
}
