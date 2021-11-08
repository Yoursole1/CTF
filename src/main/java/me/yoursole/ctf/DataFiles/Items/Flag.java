package me.yoursole.ctf.DataFiles.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Flag {
    public static ItemStack flag;

    static {
        ItemStack flag = new ItemStack(Material.BLUE_BANNER);
        ItemMeta meta = flag.getItemMeta();

        List<String> lore = new ArrayList<>();
        meta.setDisplayName(ChatColor.BLUE+"The Flag");
        lore.add(ChatColor.GOLD+"Keep me safe");
        lore.add(ChatColor.GOLD+"When I am in your inventory you get points");
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.setCustomModelData(1);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        flag.setItemMeta(meta);
        Flag.flag = flag;
    }
}
