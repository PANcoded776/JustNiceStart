package me.powerpfannkuchen.justNiceStart.commands;

import me.powerpfannkuchen.justNiceStart.Main;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class reload implements CommandExecutor {

    private final JavaPlugin plugin;

    public reload(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

        // /reload Befehl
        if (command.getName().equalsIgnoreCase("jnsreload")) {
            plugin.reloadConfig();
            player.sendMessage("§aKonfiguration wurde neu geladen.");
            return true;
        }

        return false;
    }
}
