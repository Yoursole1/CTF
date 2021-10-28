package me.yoursole.ctf.Events;

import com.google.common.collect.Maps;
import me.yoursole.ctf.DataFiles.GameData;
import me.yoursole.ctf.DataFiles.Utils;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

public class BlockBreak implements Listener {

    private static final Map<Material, Material> smeltable = Maps.newHashMap();
    private static final Map<Material, Float> experience = Maps.newHashMap();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isDropItems() || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        Collection<ItemStack> drops = event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand());
        if (drops.isEmpty()) return;
        for (ItemStack drop : drops) {
            Material smelted = smeltable.get(drop.getType());
            if (smelted != null) {
                ItemStack stack = new ItemStack(smelted);
/*                if (smelted == Material.COPPER_INGOT) {
                    stack.setAmount(getRandom(2, 3));
                }*/
                stack.setAmount(drop.getAmount());

                event.getPlayer().setTotalExperience((int) (event.getPlayer().getTotalExperience() + experience.getOrDefault(smelted, 0f) * stack.getAmount()));
                drop = stack;
            }
            Utils.doTelekinesis(event.getPlayer(), drop);
        }
        event.setDropItems(false);
    }

    static {
        smeltable.put(Material.RAW_IRON, Material.IRON_INGOT);
        smeltable.put(Material.RAW_GOLD, Material.GOLD_INGOT);
        smeltable.put(Material.RAW_COPPER, Material.COPPER_INGOT);
/*        smeltable.put(Material.IRON_ORE, Material.IRON_INGOT);
        smeltable.put(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT);
        smeltable.put(Material.COPPER_ORE, Material.COPPER_INGOT);
        smeltable.put(Material.DEEPSLATE_COPPER_ORE, Material.COPPER_INGOT);
        smeltable.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        smeltable.put(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT);*/

        experience.put(Material.IRON_INGOT, 0.7f);
        experience.put(Material.COPPER_INGOT, 0.7f);
        experience.put(Material.GOLD_INGOT, 1f);
    }
}
