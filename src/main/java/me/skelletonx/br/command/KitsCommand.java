package me.skelletonx.br.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.skelletonx.br.manager.KitManager;
import me.skelletonx.br.model.Kit;

import javax.annotation.Nonnull;
import java.awt.Color;

public class KitsCommand extends AbstractPlayerCommand {

    private final KitManager kitManager;
    private final OptionalArg<String> kitNameArg;

    public KitsCommand(KitManager kitManager) {
        super("kit", "Comando de kits");
        this.kitManager = kitManager;
        this.kitNameArg = this.withOptionalArg("nome", "Nome do Kit", ArgTypes.STRING);
    }

    @Override
    protected boolean canGeneratePermission() {
        return true;
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
    ) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        String kitName = context.get(this.kitNameArg);

        if (kitName == null) {
            player.sendMessage(Message.raw("Kits disponíveis (" + kitManager.getAllKits().size() + "):").color(Color.YELLOW));

            if (kitManager.getAllKits().isEmpty()) {
                player.sendMessage(Message.raw("Nenhum kit configurado.").color(Color.RED));
            } else {
                for (String k : kitManager.getAllKits().keySet()) {
                    player.sendMessage(Message.raw("- " + k).color(Color.GREEN));
                }
            }
            return;
        }

        Kit kit = kitManager.getKit(kitName);

        if (kit == null) {
            player.sendMessage(Message.raw("Kit não encontrado: " + kitName).color(Color.RED));
            return;
        }

        // Permissão Check
        if (kit.getPermission() != null && !kit.getPermission().isEmpty()) {
            if (!player.hasPermission(kit.getPermission())) {
                player.sendMessage(Message.raw("Você não tem permissão para este kit.").color(Color.RED));
                return;
            }
        }

        // --- COOLDOWN CHECK ---
        if (kitManager.isOnCooldown(player, kit)) {
            String tempoRestante = kitManager.getRemainingTime(player, kit);
            player.sendMessage(Message.raw("Aguarde " + tempoRestante + " para usar este kit novamente.").color(Color.RED));
            return;
        }

        // Entrega o Kit
        kitManager.giveKitToPlayer(player, kit);

        // --- APLICA O COOLDOWN ---
        kitManager.applyCooldown(player, kit);

        player.sendMessage(Message.raw("Você recebeu o kit: " + kit.getName()).color(Color.GREEN));
    }
}