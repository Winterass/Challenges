package de.eternalcode.challenges.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A utility class for building ItemStack objects with a fluent interface.
 */
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;
    private final List<Component> lore = new ArrayList<>();

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder name(Component name) {
        if (meta != null) {
            meta.displayName(name);
        }
        return this;
    }

    public ItemBuilder addLore(Component line) {
        lore.add(line);
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        this.lore.clear();
        this.lore.addAll(lore);
        return this;
    }

    public ItemBuilder hideAttributes() {
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        }
        return this;
    }

    public ItemBuilder unbreakable(boolean value) {
        if (meta != null) {
            meta.setUnbreakable(value);
        }
        return this;
    }

    public ItemBuilder head(UUID playerUUID) {
        if (item.getType() == Material.PLAYER_HEAD && meta instanceof SkullMeta skullMeta) {
            PlayerProfile profile = Bukkit.createProfile(playerUUID);
            skullMeta.setOwnerProfile(profile);
        }
        return this;
    }

    public ItemStack build() {
        if (meta != null) {
            meta.lore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int amount) {
        if (meta != null && enchantment != null) {
            meta.addEnchant(enchantment, amount, true);
        }
        return this;
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }
}


