package me.yoursole.ctf.DataFiles;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class Utils {
    public static boolean compareBreakable(ItemStack first, ItemStack second) {
        boolean similar = false;

        if(first == null || second == null){
            return similar;
        }

        boolean sameTypeId = (first.getType() == second.getType());
        boolean sameHasItemMeta = (first.hasItemMeta() == second.hasItemMeta());
        boolean sameEnchantments = (first.getEnchantments().equals(second.getEnchantments()));
        boolean sameItemMeta = true;

        if(sameHasItemMeta) {
            ItemMeta m1 = first.getItemMeta();
            ItemMeta m2 = second.getItemMeta();
            if (m1 instanceof Damageable ^ m2 instanceof Damageable) return false;
            if (m1 instanceof Damageable && m2 instanceof Damageable) {
                ((Damageable)m1).setDamage(0);
                ((Damageable)m2).setDamage(0);
                sameItemMeta = Bukkit.getItemFactory().equals(m1, m2);
            }
        }

        if(sameTypeId && sameHasItemMeta && sameEnchantments && sameItemMeta){
            similar = true;
        }

        return similar;
    }

    public static void doTelekinesis(Player player, ItemStack... stack) {
        Map<Integer, ItemStack> items = player.getInventory().addItem(stack);
        if (!items.isEmpty()) {
            for (ItemStack item : items.values()) {
                Item drop = player.getWorld().dropItemNaturally(player.getLocation(), item);
                drop.setPickupDelay(5);
                drop.setOwner(player.getUniqueId());
                drop.setThrower(player.getUniqueId());
            }
        }
    }

    public static int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
