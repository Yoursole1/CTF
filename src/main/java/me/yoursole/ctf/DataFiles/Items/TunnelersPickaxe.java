package me.yoursole.ctf.DataFiles.Items;

import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class TunnelersPickaxe implements Listener {
    public static ItemStack item;

    private static HashSet<Material> breakable = new HashSet<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Vector face = player.getEyeLocation().getDirection();

        if (Utils.compareBreakable(TunnelersPickaxe.item, player.getInventory().getItemInMainHand())) {
            int distance = (GameData.it == null ? 3 : (player.getUniqueId() == GameData.it.getUniqueId() ? 2 : 5));
            for (int i = 1; i < distance; i++) {
                Block candidate = player.getWorld().getBlockAt(block.getLocation().add(new Vector(Math.round(face.getX()) * i, Math.round(face.getY()) * i, Math.round(face.getZ()) * i)));
                if (breakable.contains(candidate.getType())) {
                    Collection<ItemStack> drops =  candidate.getDrops(player.getInventory().getItemInMainHand());
                    candidate.setType(Material.AIR);
                    Utils.doTelekinesis(player, drops.stream().toArray(ItemStack[]::new));
                }
            }
        }
    }

    static {
        ItemStack pick = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta meta = pick.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Tunneler's Pickaxe");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Item Ability: Tunnel");
        lore.add(ChatColor.WHITE + "Digs 5 blocks forward in the direction you are looking.");
        lore.add(ChatColor.RED + "Can break: Cobblestone, Stone, Gravel, Deepslate, Netherrack");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        pick.setItemMeta(meta);
        TunnelersPickaxe.item = pick;

        NamespacedKey key = NamespacedKey.fromString("ctf:tunnelerspickaxe");

        ShapedRecipe recipe = new ShapedRecipe(key, TunnelersPickaxe.item);
        recipe.shape("IGI", " S ", " S ");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('I', Material.GOLD_INGOT);
        recipe.setIngredient('S', Material.STICK);
        recipe.setGroup("ctf:tunnelerspickaxe");

        Bukkit.addRecipe(recipe);
        GameData.recipeKeys.put("tunnelerspickaxe", key);

        breakable.add(Material.COBBLESTONE);
        breakable.add(Material.DEEPSLATE);
        breakable.add(Material.DRIPSTONE_BLOCK);
        breakable.add(Material.NETHERRACK);
        breakable.add(Material.STONE);
        breakable.add(Material.ANDESITE);
        breakable.add(Material.DIORITE);
        breakable.add(Material.GRANITE);
        breakable.add(Material.GRAVEL);
        breakable.add(Material.SANDSTONE);
        breakable.add(Material.RED_SANDSTONE);
        breakable.add(Material.DIRT);
        breakable.add(Material.GRASS_BLOCK);
        breakable.add(Material.COARSE_DIRT);
    }
}
