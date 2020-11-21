package fr.jules.pvp.action;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerAction implements Listener {

    private boolean isFirstRes = false;
    public static double X;
    public static double Y;
    public static double Z;


    public static void sendMessages(List<Player> players, String s) {
        for (Player player : players) {
            player.sendMessage(s);
        }
    }

    private final Material[] Stuff = {
            Material.ARROW,
            Material.WOOD,
            Material.IRON_AXE,
            Material.BOW
    };

    public static void SetupInventory(Player player) {
        Inventory inventory = player.getInventory();
        inventory.clear();

        ItemStack defaultWeapon = new ItemStack(Material.IRON_AXE);
        defaultWeapon.getItemMeta().setDisplayName("IRON_AXE_PVP");
        List<String> Lore = new ArrayList<>();
        Lore.add("Default weapon for pvp and for remove block wood");
        defaultWeapon.getItemMeta().setLore(Lore);
        defaultWeapon.getItemMeta().setUnbreakable(true);
        defaultWeapon.getItemMeta().addEnchant(Enchantment.DAMAGE_ALL, 10, true);

        ItemStack defaultBow = new ItemStack(Material.BOW);
        defaultBow.getItemMeta().setDisplayName("IRON_BOW_PVP");
        List<String> LoreB = new ArrayList<>();
        Lore.add("Default bow for pvp");
        defaultBow.getItemMeta().setLore(LoreB);
        defaultBow.getItemMeta().setUnbreakable(true);

        inventory.addItem(defaultWeapon);
        inventory.addItem(defaultBow);
        inventory.addItem(new ItemStack(Material.ARROW, 64));
        inventory.addItem(new ItemStack(Material.WOOD, 64));
    }

    @EventHandler
    public void ItemBreak(PlayerItemBreakEvent e) {
        e.getPlayer().getInventory().addItem(e.getBrokenItem());
    }

    @EventHandler
    public void BlockBreak(BlockBreakEvent e) {
        if (!e.getBlock().getType().equals(Material.WOOD)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void Death(PlayerDeathEvent e) {
        if (!MainEvent.PVP_START) {
            e.setKeepInventory(true);
            e.getDrops().clear();
        }
    }

    @EventHandler
    private void PlayerMove(PlayerToggleSprintEvent e) {
        if (!this.isFirstRes) {
            this.isFirstRes = true;
            X = e.getPlayer().getLocation().getX();
            Y = e.getPlayer().getLocation().getY();
            Z = e.getPlayer().getLocation().getZ();
        }
        e.getPlayer().sendMessage(e.getPlayer().getLocation().getX() + " ; " + e.getPlayer().getLocation().getY() + " ; " + e.getPlayer().getLocation().getZ());
    }

    @EventHandler
    public void Repsawn(PlayerRespawnEvent e) {
        SetupInventory(e.getPlayer());
        e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0, 0, 0));
        if (MainEvent.PVP_START) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent e) {
        if (!e.getBlock().getType().equals(Material.WOOD)||!MainEvent.PVP_START) {
            e.setCancelled(true);
        } else {
            for (ItemStack item : e.getPlayer().getInventory().getStorageContents()) {
                if (item != null && item.getType().equals(Material.WOOD)) {
                    item.setAmount(64);
                }
            }
        }
    }

    @EventHandler
    public void ThrowArrow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            for (ItemStack item : player.getInventory().getStorageContents()) {
                if (item != null && item.getType().equals(Material.ARROW)) {
                    item.setAmount(64);
                }
            }
        }
    }

    public void PlayerCollect(PlayerPickupItemEvent e) {
        e.getItem().remove();
        e.setCancelled(true);
    }

    public void DropItem(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

}
