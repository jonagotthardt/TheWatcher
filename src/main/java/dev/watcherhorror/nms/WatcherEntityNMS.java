package dev.watcherhorror.nms;

import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.monster.EntityEnderman;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.List;

public class WatcherEntityNMS {

    public static void spawn(Location loc) {
        WorldServer nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        WatcherEnderman watcher = new WatcherEnderman(nmsWorld, loc);
        watcher.setPosition(loc.getX(), loc.getY(), loc.getZ());
        nmsWorld.addFreshEntity(watcher, SpawnReason.CUSTOM);
    }

    public static class WatcherEnderman extends EntityEnderman {
        private final Location bukkitLoc;

        public WatcherEnderman(World world, Location bukkitLoc) {
            super(EntityTypes.ENDERMAN, world);
            this.bukkitLoc = bukkitLoc;
            this.setCustomName(new ChatComponentText("§5The Watcher"));
            this.setSilent(true);
            this.setHealth(60f);
            this.bP = new PathfinderGoalSelector(this.level().getProfilerSupplier());
            this.bQ = new PathfinderGoalSelector(this.level().getProfilerSupplier());
            this.bP.a(0, new PathfinderGoalFloat(this));
        }

        @Override
        public void tick() {
            super.tick();

            List<EntityHuman> players = this.level().a(EntityHuman.class, this.getBoundingBox().grow(20.0D));
            for (EntityHuman player : players) {
                Player bp = (Player) player.getBukkitEntity();

                if (!player.hasLineOfSight(this)) {
                    double dx = player.locX() - this.locX();
                    double dz = player.locZ() - this.locZ();
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > 2.5D) {
                        this.getNavigation().a(player.locX(), player.locY(), player.locZ(), 1.3D);
                    } else {
                        player.damageEntity(DamageSource.mobAttack(this), 8.0F);
                        Location loc = new Location(Bukkit.getWorld(this.G().getWorld().getWorld().getName()), this.dl(), this.dn(), this.dr());
                        loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 20, 0.5, 1, 0.5, 0.2, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
                        loc.getWorld().playSound(loc, "watcher.scream", 1.0f, 1.0f);
                        bp.sendMessage(ChatColor.DARK_PURPLE + "§lLEAVE THIS WORLD.");
                    }
                } else {
                    this.getNavigation().o();
                    Location loc = new Location(Bukkit.getWorld(this.G().getWorld().getWorld().getName()), this.dl(), this.dn(), this.dr());
                    loc.getWorld().playSound(loc, "watcher.whisper", 1.0f, 0.8f);
                    bp.sendTitle("§5You should not look at him", "", 10, 40, 10);
                }

                // Random chat logic
                if (this.tickCount % 100 == 0) {
                    int roll = this.random.nextInt(8);
                    switch (roll) {
                        case 0 -> bp.sendMessage(ChatColor.DARK_PURPLE + "§lWhy are you still here?");
                        case 1 -> bp.sendMessage(ChatColor.DARK_GRAY + "§oYou don't belong here...");
                        case 2 -> bp.sendMessage(ChatColor.RED + "§lHe is coming.");
                        case 3 -> bp.sendMessage(ChatColor.GRAY + "§oThere is no escape.");
                        case 4 -> bp.sendMessage(ChatColor.DARK_PURPLE + "§oThere are things in the dark you should not see...");
                        case 5 -> bp.sendMessage(ChatColor.GRAY + "§7You are not alone.");
                        case 6 -> bp.sendMessage(ChatColor.RED + "§cThe Watcher remembers you.");
                        case 7 -> bp.sendMessage(ChatColor.LIGHT_PURPLE + "§5He’s closer than you think.");
                    }
                    bp.playSound(bp.getLocation(), "watcher.heartbeat", 1.0f, 0.9f);
                }
            }
        }
    }
}
