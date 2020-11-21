package fr.jules.pvp;

import fr.jules.pvp.action.Commands;
import fr.jules.pvp.action.MainEvent;
import fr.jules.pvp.action.PlayerAction;
import org.bukkit.Difficulty;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin extends JavaPlugin {

    private final String[] commands = {
            "start",
            "stop",
            "reset",
            "pvp"
    };

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        System.out.println("Starting Pvp plugin start");
        for(String cmd : commands){
            getCommand(cmd).setExecutor(new Commands());
        }
        getServer().getWorld("world").setDifficulty(Difficulty.PEACEFUL);
        getServer().getWorld("world").setAnimalSpawnLimit(1);
        getServer().getWorld("world").setAutoSave(false);
        getServer().getPluginManager().registerEvents(new MainEvent(this),this);
        getServer().getPluginManager().registerEvents( new PlayerAction(),this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Stopping Pvp plugin start");
    }
}
