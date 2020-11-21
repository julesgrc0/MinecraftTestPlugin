package fr.jules.pvp.action;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            switch (cmd.getName().toLowerCase()) {
                case "start":
                    MainEvent.PVP_START = true;
                    PlayerAction.sendMessages(MainEvent.players, pl.getName() + " start the game");
                    break;
                case "stop":
                    PlayerAction.sendMessages(MainEvent.players, pl.getName() + " stop the game !");
                    MainEvent.PVP_START = false;
                    for (Player player : MainEvent.players) {
                        player.setGameMode(GameMode.SURVIVAL);
                        PlayerAction.SetupInventory(player);
                    }
                    WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
                    wb.setCenter(new Location(pl.getWorld(), 0, 0, 0));
                    wb.setSize(250);
                    break;
                case "pvp":
                    String[] help = {
                            "/pvp => for help",
                            "/start => start game",
                            "/stop game",
                    };
                    sender.sendMessage(help);
                    break;
                default:
                    sender.sendMessage(cmd.getName().toLowerCase());
                    break;
            }
        }
        return false;
    }
}
