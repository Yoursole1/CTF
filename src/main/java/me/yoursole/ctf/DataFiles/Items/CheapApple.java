package me.yoursole.ctf.DataFiles.Items;

import me.yoursole.ctf.DataFiles.GameData;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheapApple {
    public static ItemStack item;
    static {
        ItemStack apple = new ItemStack(Material.APPLE);
        ItemMeta meta = apple.getItemMeta();

        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.RED + "Cheap Apple");
        lore.add(ChatColor.GRAY + "For when you are feeling broke");
        meta.setLore(lore);
        apple.setItemMeta(meta);
        CheapApple.item = apple;
    }
}
