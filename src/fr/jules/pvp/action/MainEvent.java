package fr.jules.pvp.action;

import fr.jules.pvp.MainPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class MainEvent implements Listener {

    public static MainPlugin plugin;
    public static List<Player> players = new ArrayList<>();
    private boolean IsFirst = false;
    public static boolean PVP_START = false;
    private int min = 250;

    public MainEvent(MainPlugin Plugin) {
        plugin = Plugin;
        this.Loop();
    }


    private void Loop() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : players) {
                p.setFoodLevel(100);
            }
            if (min == 250 && PVP_START) {
                for (Player p : players) {
                    p.teleport(new Location(p.getWorld(), PlayerAction.X, PlayerAction.Y, PlayerAction.Z));
                }
            }
            if (PVP_START && players.stream().count() > 0) {
                int count=0;
                String name="";
                for (Player p : players) {
                    if(p.getGameMode().equals(GameMode.SURVIVAL)){
                        count++;
                        name=p.getName();
                    }
                }
                if(count==1){
                    min = 250;
                    PVP_START = false;
                    for (Player player : MainEvent.players) {
                        player.setGameMode(GameMode.SURVIVAL);
                        PlayerAction.SetupInventory(player);
                    }
                    PlayerAction.sendMessages(players, name+" win the game !");
                    PlayerAction.sendMessages(players, "Game finish , restarting ...");
                }else{
                    min--;
                    if (min >= 20) {
                        WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
                        wb.setCenter(new Location(players.get(0).getPlayer().getWorld(), 0, 0, 0));
                        wb.setSize(min);
                        wb.setDamageAmount(10.5);
                    } else {
                        min = 250;
                        PVP_START = false;
                        for (Player player : MainEvent.players) {
                            player.setGameMode(GameMode.SURVIVAL);
                            PlayerAction.SetupInventory(player);
                        }
                        PlayerAction.sendMessages(players, "Game finish , restarting ...");
                    }
                }
            }

        }, 0, 20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!this.IsFirst) {
            this.IsFirst = true;
            WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
            wb.setCenter(new Location(e.getPlayer().getWorld(), 0, 0, 0));
            wb.setSize(250);
        }
        PlayerAction.SetupInventory(e.getPlayer());
        if (!players.contains(e.getPlayer())) {
            PlayerAction.sendMessages(players, e.getPlayer().getName() + " join the server !");
            players.add(e.getPlayer());
            e.getPlayer().sendMessage("Welcome !");
        } else {
            e.getPlayer().sendMessage("Welcome back !");
        }
        e.getPlayer().setHealth(0.0);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        players.remove(e.getPlayer());
        PlayerAction.sendMessages(players, e.getPlayer().getName() + " leave the server !");
    }

}
