package de.eternalcode.challenges.challenge.hs;

import de.eternalcode.challenges.Challenges;
import de.eternalcode.challenges.api.command.AbstractBaseCommand;
import de.eternalcode.challenges.api.command.CommandContext;
import de.eternalcode.challenges.api.command.CommandInfo;
import de.eternalcode.challenges.utils.InventoryBuilder;
import de.eternalcode.challenges.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * Command to open the Hide and Seek Ability Menu
 */
@CommandInfo(
        name = "hs",
        description = "Hide and Seek Ability Command",
        maxArgs = 1,
        usage = "open's the Ability menu",
        aliases = {"hideandseek", "hide-seek", "hide-n-seek"},
        permission = "eternal.challenge.hideandseek"
)
public class HideAndSeekAbillityCommand extends AbstractBaseCommand {

    public final AbilityExecutor abilityExecutor;

    public HideAndSeekAbillityCommand() {
        this.abilityExecutor = Challenges.getAbilityExecutor();
    }

    @Override
    public void execute(CommandContext context) throws CommandException {
        if (!(context.getSender() instanceof Player player)) {
            throw new CommandException("Dieser Befehl kann nur von Spielern ausgeführt werden.");
        }

        if(!(Challenges.getSimpleChallengeService().getCurrentChallenge() instanceof HideAndSeekChallenge)) {
            throw new CommandException("Die Hide and Seek Challenge ist nicht aktiv!");
        }
        openAbilityMenu(player);
    }

    public void openAbilityMenu(Player player) {
        InventoryBuilder gui = InventoryBuilder.create(
                Component.text("Abillity Menu"),
                InventoryBuilder.LayoutType.THREE_ROWS
        );

        ItemStack teleport = new ItemBuilder(Material.ENDER_PEARL).build();
        ItemMeta teleportMeta = teleport.getItemMeta();
        teleportMeta.displayName(Component.text("Teleport", NamedTextColor.GOLD));
        teleportMeta.lore(Collections.singletonList(Component.text("Teleportiere dich in einem Umkreis von 20 Blöcken!", NamedTextColor.YELLOW)));
        teleport.setItemMeta(teleportMeta);

        gui.setItem(11, teleport, event -> {
            abilityExecutor.teleportNearRandomPlayer(player);
            player.closeInventory();
        });

        ItemStack speed = new ItemBuilder(Material.SUGAR).build();
        ItemMeta speedMeta = speed.getItemMeta();
        speedMeta.displayName(Component.text("Speed II", NamedTextColor.GOLD));
        speedMeta.lore(Collections.singletonList(Component.text("Erhalte Speed II für 30 Sekunden!", NamedTextColor.YELLOW)));
        speed.setItemMeta(speedMeta);

        gui.setItem(13, speed, event -> {
            abilityExecutor.applySpeed(player);
            player.closeInventory();
        });

        ItemStack fly = new ItemBuilder(Material.FEATHER).build();
        ItemMeta flyMeta = fly.getItemMeta();
        flyMeta.displayName(Component.text("Fliegen", NamedTextColor.GOLD));
        flyMeta.lore(Collections.singletonList(Component.text("Du kannst für 10 Sekunden Fliegen wie im Creative!", NamedTextColor.YELLOW)));
        fly.setItemMeta(flyMeta);

        gui.setItem(15, fly, event -> {
            abilityExecutor.applyFly(player);
            player.closeInventory();
        });

        gui.open(player);
    }
}
