package com.dnyferguson.mineablespawners.listeners;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerPlaceListener implements Listener {

    private boolean compatibility;

    public SpawnerPlaceListener(MineableSpawners plugin) {
        compatibility = plugin.getConfig().getBoolean("enable-compatibility");
    }

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER) {
            return;
        }

        ItemStack placed = e.getItemInHand();
        ItemMeta meta = placed.getItemMeta();

        boolean found = false;
        EntityType entity = null;
        if (compatibility) {
            try {
                entity = EntityType.valueOf(meta.getLore().toString().split(": §7")[1].split("]")[0].toUpperCase());
                found = true;
            } catch (NullPointerException ex) {}
        }

        try {
            String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
            if (!found) {
                entity = EntityType.valueOf(entityName);
            }
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setSpawnedType(entity);
            spawner.update();
        } catch (NullPointerException ex) {
            System.out.println("[MineableSpawners] An error occured while placing a spawner. Please contact the author!");
        }
    }
}
