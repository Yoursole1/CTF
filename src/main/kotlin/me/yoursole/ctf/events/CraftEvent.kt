package me.yoursole.ctf.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent

object CraftEvent : Listener {
    @EventHandler
    fun onPrepareCraft(event: PrepareItemCraftEvent?) {
/*        if (event.getRecipe() == null) {
            HumanEntity player = event.getView().getPlayer();
            ItemStack[] matrix = event.getInventory().getMatrix();
            if (Arrays.stream(matrix).allMatch(itemStack -> itemStack == null || itemStack.getType().getKey().getKey().endsWith("_leaves"))) {
                int leaves = Arrays.stream(matrix).filter(Objects::nonNull).mapToInt(ItemStack::getAmount).sum();
                if (leaves > 0 && leaves % 8 == 0) {
                    ItemStack clone = CheapApple.item.clone();
                    clone.setAmount(leaves/8);
                    event.getInventory().setResult(clone);
                }
            }
        }*/
    }

    @EventHandler
    fun onCraft(event: CraftItemEvent?) {
/*        event.getWhoClicked().sendMessage("craft");
        if (event.getCurrentItem().isSimilar(CheapApple.item)) {
            event.getClickedInventory().clear();
        }*/
    }

    @EventHandler
    fun onAnvil(event: PrepareAnvilEvent?) {
    }
}