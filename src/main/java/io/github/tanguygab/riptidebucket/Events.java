package io.github.tanguygab.riptidebucket;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Events implements Listener {

    public final Map<Player, Block> loc = new HashMap<>();
    public final Plugin plugin;

    public Events(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        ItemStack trident = e.getItem();
        Player p = e.getPlayer();
        if (e.getHand() != EquipmentSlot.HAND || trident == null || trident.getType() != Material.TRIDENT) return;
        if (!trident.getEnchantments().containsKey(Enchantment.RIPTIDE) || p.getInventory().getItemInOffHand().getType() != Material.WATER_BUCKET) return;
        if (!e.getAction().toString().contains("RIGHT")) {
            if (e.getAction().toString().contains("LEFT") && loc.containsKey(p) && p.getWorld().getBlockAt(loc.get(p).getLocation()).getType() == Material.WATER) {
                p.getWorld().getBlockAt(loc.get(p).getLocation()).setType(Material.AIR);
                loc.remove(p);
            }
            e.setCancelled(true);
            return;
        }
        if (p.getWorld().getBlockAt(p.getLocation()).getType() == Material.WATER) return;

        p.getWorld().getBlockAt(p.getLocation()).setType(Material.WATER);
        loc.put(p,p.getWorld().getBlockAt(p.getLocation()));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!locChanged(e.getFrom(),e.getTo())) return;
        Player p = e.getPlayer();
        if (loc.containsKey(p) && p.getWorld().getBlockAt(loc.get(p).getLocation()).getType() == Material.WATER)
            e.setCancelled(true);
    }

    public boolean locChanged(Location loc1, Location loc2) {
        if (loc2 == null) return false;
        if (loc1.getWorld() != loc2.getWorld()) return true;
        if (Double.doubleToLongBits(loc1.getX()) != Double.doubleToLongBits(loc2.getX())) return true;
        if (Double.doubleToLongBits(loc1.getY()) != Double.doubleToLongBits(loc2.getY())) return true;
        if (Double.doubleToLongBits(loc1.getZ()) != Double.doubleToLongBits(loc2.getZ())) return true;
        return false;
    }

    @EventHandler
    public void stopWaterSpreading(BlockFromToEvent e) {
        if (loc.containsValue(e.getBlock()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onRiptide(PlayerRiptideEvent e) {
        Player p = e.getPlayer();
        if (loc.containsKey(p)) {
            if (p.getWorld().getBlockAt(loc.get(p).getLocation()).getType() == Material.WATER)
                p.getWorld().getBlockAt(loc.get(p).getLocation()).setType(Material.AIR);
            loc.remove(p);
        }
    }

}
