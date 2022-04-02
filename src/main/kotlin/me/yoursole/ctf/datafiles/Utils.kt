package me.yoursole.ctf.datafiles

import net.minecraft.nbt.CompoundTag
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.math.*

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

    fun Player.doTelekinesis(stack: ItemStack) {
        if (stack.type == Material.AIR) return
        val items = inventory.addItem(CraftItemStack.asCraftCopy(stack).apply { getCTFId() })
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
        if (blockAbove.type == Material.LAVA || blockAbove.type == Material.FIRE || blockAbove.type == Material.WATER) return false
        if (block.type == Material.LAVA || block.type == Material.FIRE || blockAbove.type == Material.WATER) return false
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
    
    fun ItemStack.getCTFAttributes(): CompoundTag = ((this as CraftItemStack).handle ?: net.minecraft.world.item.ItemStack.EMPTY).getOrCreateTagElement("CTFAttributes")

    fun ItemStack.getCTFId(): String = getCTFAttributes().getString("itemId")

    fun ItemStack.setCTFId(id: String) = getCTFAttributes().putString("itemId", id)

    fun Player.getArrowFor(loc: Location): Arrows {
        val a = loc.x - this.location.x
        val c = sqrt(a * a + (loc.z - this.location.z).pow(2.0))
        var angleBetween = asin(a / c) * 180 / 3.14
        if (this.location.z < loc.z) {
            angleBetween = 180 - abs(angleBetween)
            if (this.location.x > loc.x) {
                angleBetween *= -1.0
            }
        }
        val angle = Math.toRadians(this.location.yaw.toDouble())
        val x = cos(angle)
        var angleFacing = 180 - acos(x) * 180 / 3.14
        if (this.location.direction.x < 0) {
            angleFacing *= -1.0
        }
        var combined = angleFacing - angleBetween
        if (abs(combined) > 180) {
            combined = if (combined > 0) {
                -1 * (360 - combined)
            } else {
                360 - abs(combined)
            }
        }
        return when (combined) {
            in -22.5..22.5 -> Arrows.UP
            in 22.5..67.5 -> Arrows.UPLEFT
            in 67.5..112.5 -> Arrows.LEFT
            in 112.5..157.5 -> Arrows.DOWNLEFT
            in 157.0..180.0, in -180.0..-157.5 -> Arrows.DOWN
            in -157.5..-112.5 -> Arrows.DOWNRIGHT
            in -112.5..-67.5 -> Arrows.RIGHT
            in -67.5..-22.5 -> Arrows.UPRIGHT
            else -> Arrows.NONE
        }
    }

    enum class Arrows(val char: Char) {
        NONE('|'),
        LEFT('←'),
        UP('↑'),
        RIGHT('→'),
        DOWN('↓'),
        UPLEFT('↖'),
        UPRIGHT('↗'),
        DOWNRIGHT('↘'),
        DOWNLEFT('↙');
    }
}