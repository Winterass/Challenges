package de.eternalcode.challenges.utils;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class InventoryBuilder {

    public enum LayoutType {
        THREE_ROWS(3),
        FIVE_ROWS(5),
        SIX_ROWS(6);

        final int rows;

        LayoutType(int rows) {
            this.rows = rows;
        }

        public int getSize() {
            return rows * 9;
        }
    }

    private static final Map<LayoutType, int[]> green = Map.of(
            LayoutType.SIX_ROWS, new int[]{0, 2, 4, 6, 8, 45, 47, 49, 51, 53},
            LayoutType.FIVE_ROWS, new int[]{0, 2, 4, 6, 8, 36, 38, 40, 42, 44}
    );

    private static final Map<LayoutType, int[]> lime = Map.of(
            LayoutType.SIX_ROWS, new int[]{1, 3, 5, 7, 9, 17, 36, 44, 46, 48, 50, 52},
            LayoutType.FIVE_ROWS, new int[]{1, 3, 5, 7, 9, 17, 27, 35, 37, 39, 41, 43},
            LayoutType.THREE_ROWS, new int[]{0, 8, 18, 26}
    );

    private static final Map<LayoutType, int[]> white = Map.of(
            LayoutType.SIX_ROWS, new int[]{18, 27, 26, 35},
            LayoutType.FIVE_ROWS, new int[]{18, 26},
            LayoutType.THREE_ROWS, new int[]{1, 7, 19, 25}
    );

    @Getter
    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> clickEvents = new HashMap<>();
    private UUID currentPlayer;

    private Consumer<InventoryClickEvent> defaultClickHandler = event -> event.setCancelled(true);

    private InventoryBuilder(Component title, LayoutType layout) {
        this.inventory = Bukkit.createInventory(null, layout.getSize(), title);
        applyDefaultLayout(layout);
        protectEntireInventory();
    }

    public static InventoryBuilder create(Component title, LayoutType layout) {
        return new InventoryBuilder(title, layout);
    }

    private void applyDefaultLayout(LayoutType type) {
        ItemStack greenPane = ItemBuilder.of(Material.GREEN_STAINED_GLASS_PANE).name(Component.text(" ")).build();
        ItemStack limePane = ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE).name(Component.text(" ")).build();
        ItemStack whitePane = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).name(Component.text(" ")).build();

        if (green.containsKey(type)) {
            for (int i : green.get(type)) {
                setDecorativeItem(i, greenPane);
            }
        }
        if (lime.containsKey(type)) {
            for (int i : lime.get(type)) {
                setDecorativeItem(i, limePane);
            }
        }
        if (white.containsKey(type)) {
            for (int i : white.get(type)) {
                setDecorativeItem(i, whitePane);
            }
        }
    }

    public InventoryBuilder setDecorativeItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        clickEvents.put(slot, event -> event.setCancelled(true));
        return this;
    }

    public InventoryBuilder setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> onClick) {
        if (onClick == null) {
            onClick = defaultClickHandler;
        }

        inventory.setItem(slot, item);
        clickEvents.put(slot, onClick);
        return this;
    }

    public InventoryBuilder setDefaultClickHandler(Consumer<InventoryClickEvent> handler) {
        this.defaultClickHandler = handler != null ? handler : event -> event.setCancelled(true);
        return this;
    }

    public Inventory open(Player player) {
        player.openInventory(inventory);
        this.currentPlayer = player.getUniqueId();
        InventoryClickListener.register(this);
        return inventory;
    }

    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        Consumer<InventoryClickEvent> handler = clickEvents.getOrDefault(slot, defaultClickHandler);

        if (handler != null) {
            event.setCancelled(true);
            handler.accept(event);
        } else {
            event.setCancelled(true);
        }
    }

    public UUID getCurrentPlayer() {
        return currentPlayer;
    }

    private void protectEntireInventory() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!clickEvents.containsKey(i)) {
                clickEvents.put(i, defaultClickHandler);
            }
        }
    }

    public InventoryBuilder protectSlots(int... slots) {
        for (int slot : slots) {
            if (slot >= 0 && slot < inventory.getSize()) {
                clickEvents.put(slot, defaultClickHandler);
            }
        }
        return this;
    }

    public static void registerExistingInventory(Player player, Inventory inventory) {
        if (player != null && inventory != null) {
            int rows = Math.max(1, Math.min(6, inventory.getSize() / 9));
            LayoutType layoutType = rows == 3 ? LayoutType.THREE_ROWS :
                    rows == 5 ? LayoutType.FIVE_ROWS :
                            LayoutType.SIX_ROWS;

            InventoryBuilder tempBuilder = new InventoryBuilder(Component.text("GeschÃ¼tztes Inventar"), layoutType);
            tempBuilder.inventory.setContents(inventory.getContents());
            tempBuilder.currentPlayer = player.getUniqueId();
            InventoryClickListener.register(tempBuilder);
        }
    }
}