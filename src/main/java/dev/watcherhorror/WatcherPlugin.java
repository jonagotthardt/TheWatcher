package dev.watcherhorror;

import dev.watcherhorror.nms.WatcherEntityNMS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WatcherPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        new WatcherBossBar(this);
        new WatcherBlockQuitListener(this);
        getLogger().info("Watcher Horror Plugin mit zufälligen Sprüchen aktiviert.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawnwatcher") && sender instanceof Player p) {
            WatcherEntityNMS.spawn(p.getLocation());
            return true;
        }
        return false;
    }
}
