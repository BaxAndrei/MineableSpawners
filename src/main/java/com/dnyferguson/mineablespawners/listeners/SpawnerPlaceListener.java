package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerPlaceListener implements Listener {
    private MineableSpawners plugin;

    public SpawnerPlaceListener(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSpawnerPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER) {
            return;
        }

        Player player = e.getPlayer();

        if (plugin.getConfigurationHandler().getList("placing", "blacklisted-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(plugin.getConfigurationHandler().getMessage("placing", "blacklisted"));
            return;
        }

        ItemStack placed = e.getItemInHand();

        try {
            EntityType type = plugin.getNmsHandler().getType(placed);
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setSpawnedType(type);
            spawner.update();

            if (plugin.getConfigurationHandler().getBoolean("placing", "log")) {
                Location loc = block.getLocation();
                System.out.println("[MineableSpawners] Player " + e.getPlayer().getName() + " placed a " + type.name().toLowerCase() + " spawner at x:" + loc.getX() + ", y:" + loc.getY() + ", z:" + loc.getZ() + " (" + loc.getWorld().getName() + ")");
            }
        } catch (NullPointerException|IllegalArgumentException ignored) {}
    }
}
