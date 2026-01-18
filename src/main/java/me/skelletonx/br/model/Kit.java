package me.skelletonx.br.model;

import java.util.List;

public class Kit {
    private String name;
    private String permission;
    private String icon;
    private long cooldown; // O tempo em segundos
    private List<KitItem> items;

    public Kit() {}

    // Atualizei o construtor para incluir o cooldown
    public Kit(String name, String permission, String icon, long cooldown, List<KitItem> items) {
        this.name = name;
        this.permission = permission;
        this.icon = icon;
        this.cooldown = cooldown;
        this.items = items;
    }

    public String getName() { return name; }
    public String getPermission() { return permission; }
    public String getIcon() { return icon; }
    public long getCooldown() { return cooldown; } // Getter necessário
    public List<KitItem> getItems() { return items; }

    public static class KitItem {
        private String id;
        private int amount;

        public KitItem(String id, int amount) {
            this.id = id;
            this.amount = amount;
        }

        public String getId() { return id; }
        public int getAmount() { return amount; }
    }
}