package me.yoursole.ctf.datafiles

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

object Utils {
    fun compareBreakable(first: ItemStack?, second: ItemStack?): Boolean {
        var similar = false
        if (first == null || second == null) {
            return similar
        }
        val sameTypeId = first.type == second.type
        val sameHasItemMeta = first.hasItemMeta() == second.hasItemMeta()
        val sameEnchantments = first.enchantments == second.enchantments
        var sameItemMeta = true
        if (sameHasItemMeta) {
            val m1 = first.itemMeta
            val m2 = second.itemMeta
            if ((m1 is Damageable) xor (m2 is Damageable)) return false
            if (m1 is Damageable && m2 is Damageable) {
                m1.damage = 0
                m2.damage = 0
                sameItemMeta = Bukkit.getItemFactory().equals(m1, m2)
            }
        }
        if (sameTypeId && sameHasItemMeta && sameEnchantments && sameItemMeta) {
            similar = true
        }
        return similar
    }

    fun Player.doTelekinesis(vararg stack: ItemStack) {
        val items = inventory.addItem(*stack)
        if (items.isNotEmpty()) {
            for (item in items.values) {
                val drop = world.dropItemNaturally(location, item)
                drop.pickupDelay = 5
                drop.owner = uniqueId
                drop.thrower = uniqueId
            }
        }
    }

    fun Location.isSafeSpawn(): Boolean {
        val block = world.getBlockAt(this)
        val blockAbove = world.getBlockAt(clone().add(0.0, 1.0, 0.0))
        val blockBelow = world.getBlockAt(clone().add(0.0, -1.0, 0.0))
        if (!blockBelow.isSolid || block.isSolid || blockAbove.isSolid) return false
        if (blockAbove.type == Material.LAVA || blockAbove.type == Material.FIRE) return false
        if (block.type == Material.LAVA || block.type == Material.FIRE) return false
        return true
    }

    fun Location.findSafeSpawnOrMake(): Location {
        if (this.isSafeSpawn()) return this
        for (x in -5..5) {
            for (y in -5..5) {
                for (z in -5..5) {
                    val new = clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                    if (new.isSafeSpawn()) return new
                }
            }
        }
        world.getBlockAt(clone().add(1.0, -1.0, 0.0)).type = Material.BEDROCK
        world.getBlockAt(clone().add(-1.0, -1.0, 0.0)).type = Material.BEDROCK
        world.getBlockAt(clone().add(0.0, -1.0, 1.0)).type = Material.BEDROCK
        world.getBlockAt(clone().add(0.0, -1.0, -1.0)).type = Material.BEDROCK

        world.getBlockAt(clone().add(1.0, 0.0, 0.0)).type = Material.WARPED_SIGN
        world.getBlockAt(clone().add(-1.0, 0.0, 0.0)).type = Material.WARPED_SIGN
        world.getBlockAt(clone().add(0.0, 0.0, 1.0)).type = Material.WARPED_SIGN
        world.getBlockAt(clone().add(0.0, 0.0, -1.0)).type = Material.WARPED_SIGN

        world.getBlockAt(clone().add(1.0, 1.0, 0.0)).type = Material.WARPED_SIGN
        world.getBlockAt(clone().add(-1.0, 1.0, 0.0)).type = Material.WARPED_SIGN
        world.getBlockAt(clone().add(0.0, 1.0, 1.0)).type = Material.WARPED_SIGN
        world.getBlockAt(clone().add(0.0, 1.0, -1.0)).type = Material.WARPED_SIGN

        world.getBlockAt(clone().add(0.0, 2.0, 0.0)).type = Material.BEDROCK
        world.getBlockAt(clone().add(0.0, -1.0, 0.0)).type = Material.BEDROCK
        world.getBlockAt(clone()).type = Material.AIR
        world.getBlockAt(clone().add(0.0, 1.0, 0.0)).type = Material.AIR
        return this
    }

    fun ItemStack.getCTFAttributes() = (this as CraftItemStack).handle.orCreateTag.getCompound("CTFAttributes")

    fun ItemStack.getCTFId() = getCTFAttributes().getString("itemId")

    fun ItemStack.setCTFId(id: String) = getCTFAttributes().setString("itemId", id)
}