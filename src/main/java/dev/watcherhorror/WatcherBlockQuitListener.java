package dev.watcherhorror;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class WatcherBlockQuitListener implements Listener {
    private final Plugin plugin;

    public WatcherBlockQuitListener(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        boolean watcherNearby = player.getWorld().getEntities().stream().anyMatch(e ->
            e.getCustomName() != null && e.getCustomName().contains("The Watcher"));

        if (watcherNearby) {
            event.setQuitMessage(null);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage("§4You can't leave...");
                player.playSound(player.getLocation(), "watcher.heartbeat", 1f, 0.8f);
                Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§7Someone tried to escape..."));
            }, 1L);
        }
    }
}
