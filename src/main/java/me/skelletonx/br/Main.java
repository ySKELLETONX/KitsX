package me.skelletonx.br;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import me.skelletonx.br.command.KitsCommand;
import me.skelletonx.br.manager.KitManager;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;

public class Main extends JavaPlugin {

    private KitManager kitManager;

    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        this.kitManager = new KitManager(this);

        this.getCommandRegistry().registerCommand(new KitsCommand(kitManager));

        this.getCommandRegistry().registerCommand((AbstractCommand)new KitsCommand(kitManager));

        System.out.println("Plugin de Kits iniciado com sucesso!");
    }

    public KitManager getKitManager() {
        return kitManager;
    }
}