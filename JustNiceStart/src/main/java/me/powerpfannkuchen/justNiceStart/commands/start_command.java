package me.powerpfannkuchen.justNiceStart.commands;

import me.powerpfannkuchen.justNiceStart.Main;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class start_command implements TabExecutor {

    private final JavaPlugin plugin;

    private Integer globalTaskId = null;
    private BossBar globalBossBar = null;

    public start_command(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private String formatTime(int seconds) {
        int days = seconds / 86400; // Berechne Tage
        int hours = (seconds % 86400) / 3600; // Berechne Stunden
        int minutes = (seconds % 3600) / 60; // Berechne Minuten
        int remainingSeconds = seconds % 60; // Berechne verbleibende Sekunden

        StringBuilder sb = new StringBuilder();

        // Wenn es Tage gibt
        if (days > 1) {
            sb.append(String.format("%02d", days)).append(" Tagen");
        } else if (days == 1) {
            sb.append("1 Tag");
        }
        // Wenn keine Tage mehr, aber Stunden übrig sind
        else if (hours > 0) {
            sb.append(String.format("%02d", hours)).append(":").append(String.format("%02d", minutes));
            sb.append(hours > 1 ? " Stunden" : " Stunde");
        }
        // Wenn keine Stunden mehr, aber Minuten übrig sind
        else if (minutes > 0) {
            sb.append(String.format("%02d", minutes)).append(":").append(String.format("%02d", remainingSeconds));
            sb.append(minutes > 1 ? " Minuten" : " Minute");
        }
        // Wenn keine Minuten mehr, sondern nur noch Sekunden
        else {
            sb.append(String.format("%02d", remainingSeconds));
            sb.append(remainingSeconds > 1 ? " Sekunden" : " Sekunde");
        }

        return sb.toString();
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        // Überprüfen, ob der Sender ein Spieler ist
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl benutzen.");
            return true;
        }

        // Überprüfen, ob der Spieler ein Admin (Operator) ist
        if (!player.isOp()) {
            player.sendMessage("§cDu musst ein Admin sein, um diesen Befehl zu verwenden.");
            return true;
        }

        // /start
        if (command.getName().equalsIgnoreCase("start")) {

            if (globalTaskId != null) {
                player.sendMessage("§cEs läuft bereits ein Countdown!");
                return true;
            }

            int sekunden = 10;
            if (args.length > 0) {
                try {
                    sekunden = Integer.parseInt(args[0]);
                    if (sekunden <= 0) {
                        player.sendMessage("§cBitte gib eine positive Zahl an.");
                        return true;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage("§cUngültige Zahl: " + args[0]);
                    return true;
                }
            }

            final int sekundenFinal = sekunden;
            final int totalTicks = sekundenFinal * 20;

            BossBar bossBar = Bukkit.createBossBar("§6§lStartet in " + sekundenFinal + " Sekunden...", BarColor.BLUE, BarStyle.SOLID);
            globalBossBar = bossBar;
            Main.setBossBar(bossBar);

            for (Player p : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(p);
            }

            bossBar.setProgress(0.0);

            BukkitRunnable task = new BukkitRunnable() {
                int currentTick = 0;

                @Override
                public void run() {
                    currentTick++;
                    double progress = Math.max(0.0, Math.min(currentTick / (double) totalTicks, 1.0));
                    bossBar.setProgress(progress);
                    int remaining = sekundenFinal - (currentTick / 20);
                    bossBar.setTitle("§6§lStartet in " + formatTime(remaining) + "...");

                    // Wenn die letzten 30 Sekunden erreicht sind, färbe die BossBar rot
                    if (remaining <= 30) {
                        bossBar.setColor(BarColor.RED);
                    }

                    if (currentTick >= totalTicks) {
                        this.cancel();
                        bossBar.setTitle("§6§lLos geht's!");
                        bossBar.setProgress(1.0);
                        Bukkit.getScheduler().runTaskLater(plugin, bossBar::removeAll, 40L);
                        globalTaskId = null;
                        globalBossBar = null;

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10f, 1f);
                            p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation(), 100, 1, 1, 1, 0.05);
                            p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().add(0, 1, 0), 50, 0.5, 1, 0.5, 0.1);
                            p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(0, 1, 0), 80, 0.5, 0.5, 0.5, 0.02);
                            p.sendMessage("§6§lEs geht los!");
                        }

                        if (plugin.getConfig().getBoolean("runCustomCommandOnFinish")) {
                            List<String> commands = plugin.getConfig().getStringList("commands");
                            for (String cmd : commands) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                            }
                        }
                    }
                }
            };

            task.runTaskTimer(plugin, 0L, 1L);
            globalTaskId = task.getTaskId();

            player.sendMessage("§aCountdown gestartet.");
            return true;
        }

        // /cancel
        if (command.getName().equalsIgnoreCase("cancel")) {
            if (globalTaskId == null) {
                player.sendMessage("§cEs läuft kein Countdown.");
                return true;
            }

            Bukkit.getScheduler().cancelTask(globalTaskId);
            globalTaskId = null;

            if (globalBossBar != null) {
                globalBossBar.removeAll();
                globalBossBar = null;
            }

            player.sendMessage("§eCountdown abgebrochen.");
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("10", "30", "60", "120");
        }
        return List.of();
    }
}
