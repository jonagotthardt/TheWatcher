package dev.watcherhorror;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class WatcherBossBar {
    private final BossBar bar;

    public WatcherBossBar(Plugin plugin) {
        this.bar = Bukkit.createBossBar("§4✦ He watches your soul ✦", BarColor.PURPLE, BarStyle.SEGMENTED_20);
        this.bar.setVisible(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    bar.addPlayer(p);
                }
            }
        }.runTaskTimer(plugin, 0L, 60L);
    }
}
