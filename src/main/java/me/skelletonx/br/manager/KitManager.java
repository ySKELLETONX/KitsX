package me.skelletonx.br.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import me.skelletonx.br.model.Kit;

import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitManager {

    private final JavaPlugin plugin;
    private final Map<String, Kit> kits = new HashMap<>();

    // Armazena: UUID do Jogador -> (Nome do Kit -> Tempo que o cooldown ACABA em milissegundos)
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    private final Gson gson;
    private final Path kitsFile;

    public KitManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        Path pluginFolder = plugin.getDataDirectory();
        Path modsFolder = pluginFolder.getParent();
        this.kitsFile = modsFolder.resolve("KitsX").resolve("kits.json");

        loadKits();
    }

    // --- LÓGICA DE COOLDOWN ---

    public boolean isOnCooldown(Player player, Kit kit) {
        if (kit.getCooldown() <= 0) return false; // Sem cooldown configurado

        UUID uuid = player.getUuid();
        if (!cooldowns.containsKey(uuid)) return false;

        Map<String, Long> playerCooldowns = cooldowns.get(uuid);
        if (!playerCooldowns.containsKey(kit.getName())) return false;

        long endTime = playerCooldowns.get(kit.getName());
        return System.currentTimeMillis() < endTime; // Retorna true se ainda estiver no tempo
    }

    public String getRemainingTime(Player player, Kit kit) {
        if (!isOnCooldown(player, kit)) return "0s";

        long endTime = cooldowns.get(player.getUuid()).get(kit.getName());
        long remainingMillis = endTime - System.currentTimeMillis();
        long seconds = remainingMillis / 1000;

        // Formatação simples (ex: 1h 20m ou apenas 50s)
        if (seconds >= 3600) {
            return (seconds / 3600) + "h " + ((seconds % 3600) / 60) + "m";
        } else if (seconds >= 60) {
            return (seconds / 60) + "m " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
        }
    }

    public void applyCooldown(Player player, Kit kit) {
        if (kit.getCooldown() <= 0) return;

        // Calcula quando o cooldown vai acabar (Agora + Segundos * 1000)
        long endTime = System.currentTimeMillis() + (kit.getCooldown() * 1000);

        cooldowns.computeIfAbsent(player.getUuid(), k -> new HashMap<>())
                .put(kit.getName(), endTime);
    }

    // --- FIM DA LÓGICA DE COOLDOWN ---

    public void loadKits() {
        if (!Files.exists(kitsFile)) {
            saveDefaultKits();
            return;
        }

        try (Reader reader = Files.newBufferedReader(kitsFile)) {
            Type listType = new TypeToken<List<Kit>>(){}.getType();
            List<Kit> loadedKits = gson.fromJson(reader, listType);

            kits.clear();
            if (loadedKits != null) {
                for (Kit kit : loadedKits) {
                    kits.put(kit.getName().toLowerCase(), kit);
                }
            }
            System.out.println("Carregados " + kits.size() + " kits.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar kits.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveDefaultKits() {
        try {
            if (kitsFile.getParent() != null) {
                Files.createDirectories(kitsFile.getParent());
            }

            List<Kit> defaults = List.of(
                    // Adicionei o cooldown de 60 segundos no exemplo padrão
                    new Kit("iniciante", "kits.iniciante", "sword_stone", 60, List.of(
                            new Kit.KitItem("Weapon_Spear_Thorium", 1),
                            new Kit.KitItem("Weapon_Bomb_Potion_Poison", 30)
                    ))
            );

            try (Writer writer = Files.newBufferedWriter(kitsFile)) {
                gson.toJson(defaults, writer);
                System.out.println("Arquivo kits.json criado em: " + kitsFile.toAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Erro fatal ao salvar kits.json padrão.");
            e.printStackTrace();
        }
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public Map<String, Kit> getAllKits() {
        return kits;
    }

    public void giveKitToPlayer(Player player, Kit kit) {
        Inventory inventory = player.getInventory();
        ItemContainer container = inventory.getCombinedBackpackStorageHotbar();

        for (Kit.KitItem itemConfig : kit.getItems()) {
            if (itemConfig.getId() != null && !itemConfig.getId().isEmpty()) {
                ItemStack itemStack = new ItemStack(itemConfig.getId(), itemConfig.getAmount());
                container.addItemStack(itemStack);
            }
        }
        player.sendInventory();
    }
}