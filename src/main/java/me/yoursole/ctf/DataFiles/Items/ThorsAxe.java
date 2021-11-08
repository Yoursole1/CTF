package me.yoursole.ctf.DataFiles.Items;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ThorsAxe implements Listener {
    public static ItemStack item;

    public static HashMap<UUID, Integer> hitMap = new HashMap<>();

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            Player damager = (Player) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();
            if (Utils.compareBreakable(damager.getInventory().getItemInMainHand(), ThorsAxe.item)) {
                int h = hitMap.compute(damager.getUniqueId(), (player, hits) -> (hits == null ? 0 : hits) + 1);
                if (h == 4) {
                    hitMap.put(damager.getUniqueId(), 0);
                    target.setHealth(target.getHealth() - 1)    ;
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
                    target.getWorld().strikeLightningEffect(target.getLocation());
                }
            }
        }
    }

    static {
        ItemStack axe = new ItemStack(Material.STONE_AXE);
        ItemMeta meta = axe.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Thor's Axe");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Item Ability: Smite");
        lore.add(ChatColor.WHITE + "Harness the power of Thor!.");
        lore.add(ChatColor.WHITE + "Every 3 hits, your next hit will gain smite.");
        lore.add(ChatColor.WHITE + "Smite strikes the target with lightning, dealing 1 true damage.");
        lore.add(ChatColor.WHITE + "True damage ignores all protection.");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.DURABILITY, 2, true);
        axe.setItemMeta(meta);
        ThorsAxe.item = axe;

        NamespacedKey key = NamespacedKey.fromString("ctf:thorsaxe");

        ShapedRecipe recipe = new ShapedRecipe(key, ThorsAxe.item);
        recipe.shape("OI ", "IB ", " B ");
        recipe.setIngredient('O', Material.OBSIDIAN);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('B', Material.BONE);
        recipe.setGroup("ctf:thorsaxe");

        Bukkit.addRecipe(recipe);
        GameData.recipeKeys.put("thorsaxe", key);
    }
}
