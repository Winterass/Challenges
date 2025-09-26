package de.eternalcode.challenges.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Listener to handle inventory click events for the custom inventories.
 */
public class InventoryClickListener implements Listener {
    private static final Map<UUID, InventoryBuilder> openInventories = new HashMap<>();

    public static void register(InventoryBuilder inventoryBuilder) {
        Player player = Bukkit.getServer().getOnlinePlayers().stream()
                .filter(p -> p.getOpenInventory().getTopInventory().equals(inventoryBuilder.getInventory()))
                .findFirst()
                .orElse(null);

        if (player != null) {
            openInventories.put(player.getUniqueId(), inventoryBuilder);
        } else if (inventoryBuilder.getCurrentPlayer() != null) {
            openInventories.put(inventoryBuilder.getCurrentPlayer(), inventoryBuilder);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        Inventory topInventory = player.getOpenInventory().getTopInventory();
        InventoryBuilder builder = openInventories.get(player.getUniqueId());

        if (builder != null && topInventory.equals(builder.getInventory())) {
            event.setCancelled(true);

            if (event.getClickedInventory() != null && event.getClickedInventory().equals(topInventory)) {
                builder.handleClick(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        InventoryBuilder builder = openInventories.get(player.getUniqueId());
        if (builder != null && event.getInventory().equals(builder.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        openInventories.remove(player.getUniqueId());
    }
}
