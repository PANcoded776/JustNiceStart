package me.powerpfannkuchen.justNiceStart.events;

import me.powerpfannkuchen.justNiceStart.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.boss.BossBar;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        BossBar bossBar = Main.getBossBar();
        if (bossBar != null) {
            bossBar.addPlayer(player);
        }
    }
}
