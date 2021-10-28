package me.yoursole.ctf.DataFiles.Items;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.Utils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class HermesBoots implements Listener {
    public static ItemStack item;

    static {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta)boots.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Hermes' Boots");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Item Ability: Messenger");
        lore.add(ChatColor.WHITE + "Allows you to double jump!");
        lore.add(ChatColor.WHITE + "Double Jumping damages the boots by 1 and the wearer by 1.");
        lore.add(ChatColor.BLUE + "Grants +25% Walk Speed when wearing.");
        meta.setLore(lore);
        meta.setColor(Color.fromRGB(0xb3acab));
        meta.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES);
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("Hermes' Speed", 0.25, AttributeModifier.Operation.ADD_SCALAR));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("Hermes' Armor", 1, AttributeModifier.Operation.ADD_NUMBER));
        boots.setItemMeta(meta);
        HermesBoots.item = boots;

        NamespacedKey key = NamespacedKey.fromString("ctf:hermes");

        ShapedRecipe recipe = new ShapedRecipe(key, HermesBoots.item);
        recipe.shape("   ", "F F", "FDF");
        recipe.setIngredient('F', Material.FEATHER);
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setGroup("ctf:hermes");

        Bukkit.addRecipe(recipe);
        GameData.recipeKeys.put("hermes", key);
    }

    @EventHandler
    public void onAttemptFly(PlayerToggleFlightEvent event) {
        if (event.isFlying()) {
            Player player = event.getPlayer();
            ItemStack boots = event.getPlayer().getInventory().getArmorContents()[0];
            if (boots == null) return;
            if (Utils.compareBreakable(boots, HermesBoots.item)) {
                player.damage(1);
                Damageable meta = ((Damageable)boots.getItemMeta());
                meta.setDamage(meta.getDamage() + 1);
                if (meta.getDamage() > boots.getType().getMaxDurability()) {
                    player.getInventory().setBoots(null);
                }
                boots.setItemMeta(meta);

                player.setVelocity(player.getLocation().getDirection().multiply(1.2).setY(0.8));
                event.setCancelled(true);
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }
}
