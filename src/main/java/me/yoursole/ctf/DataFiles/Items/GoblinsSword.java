package me.yoursole.ctf.DataFiles.Items;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class GoblinsSword implements Listener {
    public static ItemStack item;

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null || entity instanceof Player) return;
        if (Utils.compareBreakable(killer.getInventory().getItemInMainHand(), GoblinsSword.item)) {
            if (Utils.getRandom(0, 1) == 0) {
                for (ItemStack item : event.getDrops()) {
                    item.setAmount(item.getAmount() * 2);
                }
            }
        }
    }

    static {
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Goblin's Sword");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Item Ability: Greed");
        lore.add(ChatColor.WHITE + "Grants a 50% chance to double all mob drops.");
        meta.setLore(lore);
        sword.setItemMeta(meta);
        GoblinsSword.item = sword;

        NamespacedKey key = NamespacedKey.fromString("ctf:goblinssword");

        ShapedRecipe recipe = new ShapedRecipe(key, GoblinsSword.item);
        recipe.shape(" G ", " I ", " B ");
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BONE);
        recipe.setGroup("ctf:goblinssword");

        Bukkit.addRecipe(recipe);
        GameData.recipeKeys.put("goblinssword", key);
    }
}
