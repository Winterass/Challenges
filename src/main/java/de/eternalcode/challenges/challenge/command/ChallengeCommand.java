package de.eternalcode.challenges.challenge.command;

import de.eternalcode.challenges.Challenges;
import de.eternalcode.challenges.api.Challenge;
import de.eternalcode.challenges.api.command.AbstractBaseCommand;
import de.eternalcode.challenges.api.command.CommandContext;
import de.eternalcode.challenges.api.command.CommandInfo;
import de.eternalcode.challenges.api.services.ChallengeService;
import de.eternalcode.challenges.utils.InventoryBuilder;
import de.eternalcode.challenges.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

@CommandInfo(
        name = "challenge",
        description = "Base command for challenge management",
        maxArgs = 1,
        usage = "open's the challenge menu",
        aliases = {"challenges", "chal"},
        permission = "eternal.challenge"
)
public class ChallengeCommand extends AbstractBaseCommand {

    private final ChallengeService challengeService;

    public ChallengeCommand() {
        this.challengeService = Challenges.getSimpleChallengeService();
    }

    @Override
    public void execute(CommandContext context) throws CommandException {
        if (!(context.getSender() instanceof Player player)) {
            throw new CommandException("Dieser Befehl kann nur von Spielern ausgeführt werden.");
        }

        openChallengeMenu(player);
    }

    private void openChallengeMenu(Player player) {
        InventoryBuilder gui = InventoryBuilder.create(
                Component.text("Challenge Menu"),
                InventoryBuilder.LayoutType.FIVE_ROWS
        );

        int slot = 10;
        for (Challenge challenge : challengeService.getChallenges()) {
            if(slot >= 45) break;

            if (slot == 17 || slot == 18)
                slot = 19;
            else if (slot == 26 || slot == 27)
                slot = 28;

            ItemStack item = new ItemBuilder(challenge.getIconMaterial()).build();
            ItemMeta meta = item.getItemMeta();
            meta.displayName(challenge.getName());
            meta.lore(Collections.singletonList(challenge.getDescription()));
            item.setItemMeta(meta);

            gui.setItem(slot++, item, event -> {
                challengeService.startChallenge(challenge);
                player.closeInventory();
            });
        }

        gui.open(player);
    }
}
