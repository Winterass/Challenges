package de.eternalcode.challenges.challenge;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class EnderDragonChallenge extends ParentChallenge implements Listener {

    public EnderDragonChallenge(Plugin plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public Component getName() {
        return Component.text("Ender Dragon Challenge", NamedTextColor.GREEN);
    }

    @Override
    public Component getDescription() {
        return Component.text("Defeat the Ender Dragon to complete the challenge!", NamedTextColor.GREEN);
    }

    @Override
    public Material getIconMaterial() {
        return Material.DRAGON_EGG;
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (!isRunning()) return;

        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.ENDER_DRAGON) {
            EnderDragon dragon = (EnderDragon) entity;
            World world = dragon.getWorld();

            if (world.getEnvironment() == World.Environment.THE_END) {
                Bukkit.broadcast(Component.text("§aDer EnderDragon wurde besiegt! Challenge geschafft! 🎉"));
                stop();
            }
        }
    }
}
